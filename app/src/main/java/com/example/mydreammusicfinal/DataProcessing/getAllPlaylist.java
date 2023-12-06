package com.example.mydreammusicfinal.DataProcessing;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Artist;
import com.example.mydreammusicfinal.model.Playlists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class getAllPlaylist extends AsyncTask<Void, Void, Void> {
    String searchText;
    private CallBackListener.PlaylistCallBack callback;

    public getAllPlaylist( String SearchText, CallBackListener.PlaylistCallBack callback) {
        this.callback = callback;
        this.searchText = SearchText;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<Playlists> list = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("playlists");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot playlistSnapShot : dataSnapshot.getChildren()) {
                        String nameAlbum = playlistSnapShot.child("namePlaylist").getValue(String.class);
                        String imageURL = playlistSnapShot.child("imageURL").getValue(String.class);
                        String imageBGURL = playlistSnapShot.child("ImageBackgroundURL").getValue(String.class);
                        String colorCode = playlistSnapShot.child("colorCode").getValue(String.class);
                        String keyAlbum = playlistSnapShot.getKey();
                        final ArrayList<String> userIds = new ArrayList<>();
                        final int[] totalUsers = {0};
                        Iterable<DataSnapshot> usersSnapshots = playlistSnapShot.child("users").getChildren();
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
                                        Playlists playlist = new Playlists(nameAlbum, imageURL, totalName[0], keyAlbum,imageBGURL,colorCode);
                                        if (playlist.getNameAlbum().toLowerCase().startsWith(searchText.toLowerCase())) {
                                            list.add(playlist);
                                        }
                                        callback.onCallbackAlbum(list);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("FirebaseError", "Error: " + databaseError.getMessage());
                                }
                            });
                        }
                    }
                } else {

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return null;
    }
}