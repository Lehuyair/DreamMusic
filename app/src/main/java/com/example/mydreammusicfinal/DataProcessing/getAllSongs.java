package com.example.mydreammusicfinal.DataProcessing;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Songs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class getAllSongs extends AsyncTask<Void,Void,Void> {
    String searchText;
    private CallBackListener.SongsCallBack callback;

    public getAllSongs( String SearchText, CallBackListener.SongsCallBack callback) {
        this.callback = callback;
        this.searchText = SearchText;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Songs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Songs> list = new ArrayList<>();
                for(DataSnapshot songSnapShot: dataSnapshot.getChildren()){
                    Long countListenLong = (Long) songSnapShot.child("count").getValue();
                    String colorCode =  (String) songSnapShot.child("colorCode").getValue();
                    String nameSong = (String) songSnapShot.child("nameSong").getValue();
                    String songURL = (String) songSnapShot.child("songURL").getValue();
                    String imageURL = (String) songSnapShot.child("imageURL").getValue();
                    String lyricURL = (String) songSnapShot.child("lyricsURL").getValue();
                    String keySong = songSnapShot.getKey();
                    final ArrayList<String> userIds = new ArrayList<>();
                    final int[] totalUsers = {0};
                    ArrayList<String> ArtistKey = new ArrayList<>();
                    Iterable<DataSnapshot> usersSnapshots = songSnapShot.child("users").getChildren();
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

                                    if ((songs.getSongName().toLowerCase().startsWith(searchText.toLowerCase()))) {
                                        list.add(songs);
                                    }
                                    callback.onCallbackSong(list);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return null;
    }
}