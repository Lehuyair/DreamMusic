package com.example.mydreammusicfinal.DataProcessing;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Playlists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class getDataPlaylistByKey extends AsyncTask<Void, Void, Void> {
    private String mKeyPlaylist;
    private CallBackListener.PlaylistCallBack callback;

    public getDataPlaylistByKey(String KeyPlaylist, CallBackListener.PlaylistCallBack callback) {
        this.mKeyPlaylist = KeyPlaylist;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<Playlists> listAlbum = new ArrayList<>();
        DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference().child("playlists").child(mKeyPlaylist);
        playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                        String nameAlbum = dataSnapshot.child("namePlaylist").getValue(String.class);
                        String imageURL = dataSnapshot.child("imageURL").getValue(String.class);
                        String imageBGURL = dataSnapshot.child("ImageBackgroundURL").getValue(String.class);
                        String colorCode = dataSnapshot.child("colorCode").getValue(String.class);
                        String keyAlbum = dataSnapshot.getKey();
                        final ArrayList<String> userIds = new ArrayList<>();
                        final int[] totalUsers = {0};
                        Iterable<DataSnapshot> usersSnapshots = dataSnapshot.child("users").getChildren();
                        for (DataSnapshot userSnapshot : usersSnapshots) {
                            String userId = userSnapshot.getKey();
                            totalUsers[0]++;
                            final String[] totalName = {""};
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String nameUsers = dataSnapshot.child("nameUsers").getValue(String.class);
                                    userIds.add(nameUsers);
                                    for (int i =0; i < userIds.size(); i++){
                                        totalName[0] = userIds.get(0);
                                        if(i >0 ){
                                            totalName[0] +=" ft. ";
                                            totalName[0] += userIds.get(i);
                                        }
                                    }
                                    if (userIds.size() == totalUsers[0]) {
                                        Playlists album = new Playlists(nameAlbum, imageURL, totalName[0], keyAlbum,imageBGURL,colorCode);
                                        listAlbum.add(album);
                                        callback.onCallbackAlbum(listAlbum);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("FirebaseError", "Error: " + databaseError.getMessage());
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