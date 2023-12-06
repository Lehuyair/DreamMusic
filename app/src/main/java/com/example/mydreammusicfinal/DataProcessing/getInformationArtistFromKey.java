package com.example.mydreammusicfinal.DataProcessing;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Artist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class getInformationArtistFromKey extends AsyncTask<Void, Void, Void> {
    private String mKeyArtist;
    private CallBackListener.ArtistObjectCallBack callback;

    public getInformationArtistFromKey(String keyArtist, CallBackListener.ArtistObjectCallBack callback) {
        this.mKeyArtist = keyArtist;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference artistRef = FirebaseDatabase.getInstance().getReference("users/" +mKeyArtist);
        artistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String key = dataSnapshot.getKey();
                    String userName = (String)dataSnapshot.child("nameUsers").getValue();
                    String imageURL = (String)dataSnapshot.child("imageUsers").getValue();
                    Artist artist = new Artist(userName,imageURL,key);
                    callback.onCallBackObjectArtist(artist);
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