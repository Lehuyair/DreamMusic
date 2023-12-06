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

public class getDataFavoriteSongsMusic extends AsyncTask<Void, Void, Void> {
    private String idUser;
    private CallBackListener.SongsCallBack callback;

    public getDataFavoriteSongsMusic(String iDUser, CallBackListener.SongsCallBack callback) {
        this.idUser = iDUser;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<Songs> listRecent = new ArrayList<>();
        DatabaseReference keyRef = FirebaseDatabase.getInstance().getReference().child("users").
                child(idUser).child("FavoriteSongs").child("Songs");
        keyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                        String songKey = songSnapshot.getKey(); // Lấy key của bài hát
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
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String nameUsers = dataSnapshot.child("nameUsers").getValue(String.class);
                                            userIds.add(nameUsers);
                                            if (userIds.size() == totalUsers[0]) {
                                                Songs songs = new Songs(userIds.get(0), nameSong, countListenLong, colorCode, songURL, imageURL, lyricURL,keySong,ArtistKey.get(0));
                                                listRecent.add(songs);
                                                callback.onCallbackSong(listRecent);
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