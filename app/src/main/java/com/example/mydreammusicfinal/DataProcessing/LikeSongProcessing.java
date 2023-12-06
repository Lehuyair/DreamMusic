package com.example.mydreammusicfinal.DataProcessing;

import android.util.Log;

import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.model.Songs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;
import java.util.UUID;

public class LikeSongProcessing {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference songref, emailref;
    String email;
    public interface OnDataReceivedListener {
        void onDataReceived(boolean isLiked);
    }
    public  LikeSongProcessing (){
        database = FirebaseDatabase.getInstance();

    };
    public   String getIDUser(){
         mAuth = FirebaseAuth.getInstance();
         currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            email = currentUser.getEmail();
            return  userId;
        } else {
            return  null;
        }
    }
    public void addSongTofavorite(Songs songs){
        String idUser = getIDUser();
        if(idUser != null || idUser.isEmpty()){
            songref = database.getReference("users/"+idUser+"/FavoriteSongs/Songs/"+songs.getSongKey());
            songref.setValue(true);
        }
    }
    public void removeSongFromFavorite(Songs songs){
        String idUser = getIDUser();
        if(idUser != null || idUser.isEmpty()){
            songref = database.getReference("users/"+idUser+"/FavoriteSongs/Songs/"+songs.getSongKey());
            songref.removeValue();
        }
    }
    public String getIDSongs() {
            UUID uniqueKey = UUID.randomUUID();
            return uniqueKey.toString();
    }
    public void checkLiked(Songs songs, OnDataReceivedListener listener){
        String IDUser = getIDUser();
        if(IDUser!=null) {
            getDataFavoriteSongsMusic task = new getDataFavoriteSongsMusic(IDUser, new CallBackListener.SongsCallBack() {
                @Override
                public void onCallbackSong(ArrayList<Songs> list) {
                    boolean isLiked = false;
                    for(Songs x: list){
                        {
                            if( x.getSongKey().equalsIgnoreCase(songs.getSongKey())){
                                isLiked = true;
                                break;
                            }
                        }
                    }
                    listener.onDataReceived(isLiked);
                }
            });
            task.execute();
        }
    }
}
