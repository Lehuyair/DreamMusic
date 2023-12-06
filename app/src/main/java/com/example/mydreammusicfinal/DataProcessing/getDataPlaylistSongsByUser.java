package com.example.mydreammusicfinal.DataProcessing;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Songs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class getDataPlaylistSongsByUser extends AsyncTask<Void, Void, Void> {
    private String mKeyArtist;
    private CallBackListener.SongsCallBack callBackSongs;

    public getDataPlaylistSongsByUser(String keyArtist, CallBackListener.SongsCallBack CallBackSongs) {
        this.mKeyArtist = keyArtist;
        this.callBackSongs = CallBackSongs;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<Songs> listPlaylist = new ArrayList<>();
        DatabaseReference artistRef = FirebaseDatabase.getInstance().getReference().child("users").child(mKeyArtist).child("Songs");;
        artistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                        String songKey = songSnapshot.getKey();
                        DatabaseReference songDetailsRef = FirebaseDatabase.getInstance().getReference().child("Songs").child(songKey);
                        songDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot songDataSnapshot) {
                                Long countListenLong = (Long) songDataSnapshot.child("count").getValue();
                                String colorCode =  (String) songDataSnapshot.child("colorCode").getValue();
                                String nameSong = (String) songDataSnapshot.child("nameSong").getValue();
                                String songURL = (String) songDataSnapshot.child("songURL").getValue();
                                String imageURL = (String) songDataSnapshot.child("imageURL").getValue();
                                String lyricURL = (String) songDataSnapshot.child("lyricsURL").getValue();
                                String keySong = songDataSnapshot.getKey();
                                final ArrayList<String> userIds = new ArrayList<>();
                                final int[] totalUsers = {0};
                                ArrayList<String> ArtistKey = new ArrayList<>();
                                Iterable<DataSnapshot> usersSnapshots = songDataSnapshot.child("users").getChildren();
                                for (DataSnapshot userSnapshot : usersSnapshots) {
                                    String userId = userSnapshot.getKey(); // Lấy key làm userId
                                    ArtistKey.add(userId);
                                    totalUsers[0]++;
                                    final String[] totalName = {""};
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String nameUsers = dataSnapshot.child("nameUsers").getValue(String.class);
                                            userIds.add(nameUsers);
                                            if (userIds.size() == totalUsers[0]) {
                                                for (int i =0; i < userIds.size(); i++){
                                                    totalName[0] = userIds.get(0);
                                                    if(i >0 ){
                                                        totalName[0] +=" ft. ";
                                                        totalName[0] += userIds.get(i);
                                                    }
                                                }
                                                Songs songs = new Songs(totalName[0], nameSong, countListenLong, colorCode, songURL, imageURL, lyricURL,keySong,ArtistKey.get(0));
                                                listPlaylist.add(songs);
                                                callBackSongs.onCallbackSong(listPlaylist);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Xử lý khi có lỗi xảy ra trong quá trình truy vấn dữ liệu
                                            Log.e("FirebaseError", "Error: " + databaseError.getMessage());
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
        return null;
    }
}