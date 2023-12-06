package com.example.mydreammusicfinal.DataProcessing;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Playlists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class getDataPlaylistByUser extends AsyncTask<Void, Void, Void> {
    private String mKeyArtist;
    private CallBackListener.PlaylistCallBack callback;

    public getDataPlaylistByUser(String keyArtist, CallBackListener.PlaylistCallBack callback) {
        this.mKeyArtist = keyArtist;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference artistRef = FirebaseDatabase.getInstance().getReference().child("users").child(mKeyArtist).child("playlists");;
        ArrayList<Playlists> listAlbum = new ArrayList<>();
        artistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot PlaylistSnapShot : dataSnapshot.getChildren()) {
                    String PlaylistKey = PlaylistSnapShot.getKey();
                    DatabaseReference playlistDetailRef = FirebaseDatabase.getInstance().getReference().child("playlists").child(PlaylistKey);
                    playlistDetailRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String keyPlayList = (String) snapshot.getKey();
                            String namePlayList = (String) snapshot.child("namePlaylist").getValue();
                            String imageURL = (String) snapshot.child("imageURL").getValue();
                            String imageBGURL = snapshot.child("ImageBackgroundURL").getValue(String.class);
                            String colorCode = snapshot.child("colorCode").getValue(String.class);
                            DataSnapshot usersSnapshot = snapshot.child("users");
                            final ArrayList<String> userIds = new ArrayList<>();
                            final String[] totalName = {""};
                            final int[] totalUsers = {0};
                            for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                                totalUsers[0]++;
                                String userID = userSnapshot.getKey();
                                DatabaseReference refUser = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
                                refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                                            listAlbum.add(new Playlists(namePlayList,imageURL,totalName[0],keyPlayList,imageBGURL,colorCode));
                                            callback.onCallbackAlbum(listAlbum);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
            }
        });
//
//

        return null;
    }
}