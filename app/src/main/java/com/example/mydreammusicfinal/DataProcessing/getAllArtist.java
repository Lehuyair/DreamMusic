package com.example.mydreammusicfinal.DataProcessing;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Artist;
import com.example.mydreammusicfinal.model.Songs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class getAllArtist extends AsyncTask<Void, Void, Void> {
    String searchText;
    private CallBackListener.ArtistCallBack callback;

    public getAllArtist( String SearchText, CallBackListener.ArtistCallBack Calllback) {
        this.callback = Calllback;
        this.searchText = SearchText;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild("isArtist").equalTo(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Artist> list = new ArrayList<>();
                for(DataSnapshot userSnapShot: dataSnapshot.getChildren()){
                    String key = userSnapShot.getKey();
                    String nameUsers = (String) userSnapShot.child("nameUsers").getValue();
                    String imageURL = (String) userSnapShot.child("imageUsers").getValue();
                    Artist artist = new Artist(nameUsers,imageURL,key);
                    if (artist.getNameArtist().toLowerCase().startsWith(searchText.toLowerCase())) {
                        list.add(artist);
                    }
                    callback.onCallBackArtist(list);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return null;
    }


}