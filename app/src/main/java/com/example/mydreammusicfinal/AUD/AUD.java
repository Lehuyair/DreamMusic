package com.example.mydreammusicfinal.AUD;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AUD {

    public static void AddCountListening(String songKey){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Songs/"+songKey);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long countListenLong = (Long) dataSnapshot.child("count").getValue();
                int newCount = (int) (countListenLong +1);
                DatabaseReference songRef = FirebaseDatabase.getInstance().getReference("Songs/"+songKey+"/count");
                songRef.setValue((newCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public static void createNewPlaylist(String UserID,String IDPlaylist,String namePlayList){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/"+UserID+"/playlists/"+IDPlaylist+"/namePlaylist");
        reference.setValue(namePlayList);
    }
    public static void createNewSong(String UserID,String IDPlaylist,String keySongs){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users/"+UserID+"/playlists/"+IDPlaylist+"/Songs/"+keySongs);
        reference.setValue(true);
    }
    public static void removePlaylist(String UserID,String keyPlaylist){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users/"+UserID+"/playlists/"+keyPlaylist);
        reference.removeValue();
    }
}
