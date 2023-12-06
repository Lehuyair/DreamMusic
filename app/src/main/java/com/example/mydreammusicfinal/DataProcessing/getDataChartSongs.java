package com.example.mydreammusicfinal.DataProcessing;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Songs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class getDataChartSongs extends AsyncTask<Void, Void, Void> {
    private String genre;
    private CallBackListener.SongsCallBack callback;
    public getDataChartSongs(String Genre, CallBackListener.SongsCallBack callback) {
        this.genre = Genre;
        this.callback = callback;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        final ArrayList<Songs>[] listPlaylist = new ArrayList[]{new ArrayList<>()};
        ArrayList<Songs> listnormal = new ArrayList<>();
        DatabaseReference databaseRefVN = FirebaseDatabase.getInstance().getReference("Songs");
        Query query = databaseRefVN.orderByChild("genres/"+genre).equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                DataSnapshot usersSnapshot = songDataSnapshot.child("users");
                                final ArrayList<String> userIds = new ArrayList<>();
                                final ArrayList<String> keyArtist = new ArrayList<>();
                                final int[] totalUsers = {0};
                                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                                    totalUsers[0]++;
                                    String userId = userSnapshot.getKey();
                                    keyArtist.add(userId);
                                    final String[] totalName = {""};
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String nameUsers = dataSnapshot.child("nameUsers").getValue(String.class);
                                            userIds.add(nameUsers);
                                            if(userIds.size() == totalUsers[0]){
                                                for (int i =0; i < userIds.size(); i++){
                                                    totalName[0] = userIds.get(0);
                                                    if(i >0 ){
                                                        totalName[0] +=" ft. ";
                                                        totalName[0] += userIds.get(i);
                                                    }
                                                }
                                                Songs songs = new Songs(totalName[0],nameSong,countListenLong,colorCode,songURL,imageURL,lyricURL,keySong,keyArtist.get(0));
                                                listnormal.add(songs);
                                                Collections.sort(listnormal, new Comparator<Songs>() {
                                                    @Override
                                                    public int compare(Songs o2, Songs o1) {
                                                        return Long.compare(o1.getCount(),o2.getCount());
                                                    }
                                                });
                                                listPlaylist[0] = new ArrayList<>(listnormal.subList(0, Math.min(listnormal.size(), 10)));
                                                callback.onCallbackSong(listPlaylist[0]);
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
                                // Xử lý khi có lỗi xảy ra
                            }
                        });
                    }
                } else {
                    // Handle no data in the playlist
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

