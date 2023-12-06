package com.example.mydreammusicfinal.DataProcessing;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.Songs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class getPlaylistbyIDUser extends AsyncTask<Void, Void, Void> {
    private String idUser;
    private CallBackListener.PlaylistCallBack callBack;

    public getPlaylistbyIDUser(String IDUser, CallBackListener.PlaylistCallBack CallBack) {
        this.idUser = IDUser;
        this.callBack = CallBack;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<Playlists> listPlaylist = new ArrayList<>();
        DatabaseReference artistRef = FirebaseDatabase.getInstance().getReference("users/"+idUser+"/playlists");
        artistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                        String keyPlaylist = Snapshot.getKey();
                        String namePlaylist = (String) Snapshot.child("namePlaylist").getValue();
                        Playlists playlists = new Playlists(keyPlaylist,namePlaylist);
                        listPlaylist.add(playlists);
                        callBack.onCallbackAlbum(listPlaylist);
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