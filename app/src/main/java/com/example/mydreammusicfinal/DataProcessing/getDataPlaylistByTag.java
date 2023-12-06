package com.example.mydreammusicfinal.DataProcessing;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Playlists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class getDataPlaylistByTag extends AsyncTask<Void, Void, Void> {
    private String tagPlaylist;
    private CallBackListener.PlaylistCallBack callback;
    Context context;
    ProgressDialog progressDialog;

    public getDataPlaylistByTag(Context Context,String tag, CallBackListener.PlaylistCallBack callback) {
        this.context = Context;
        this.tagPlaylist = tag;
        this.callback = callback;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference albumsRef = FirebaseDatabase.getInstance().getReference().child("playlists");
        Query query = albumsRef.orderByChild("Tag").equalTo(tagPlaylist);
        ArrayList<Playlists> listAlbum = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    String keyPlayList = (String) songSnapshot.getKey();
                    String namePlayList = (String) songSnapshot.child("namePlaylist").getValue();
                    String imageURL = (String) songSnapshot.child("imageURL").getValue();
                    String imageBGURL = songSnapshot.child("ImageBackgroundURL").getValue(String.class);
                    String colorCode = songSnapshot.child("colorCode").getValue(String.class);
                    DataSnapshot usersSnapshot = songSnapshot.child("users");
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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//
//

        return null;
    }
}