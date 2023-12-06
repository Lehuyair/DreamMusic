package com.example.mydreammusicfinal.Local_Data;

import android.util.Log;

import com.example.mydreammusicfinal.model.Playlists;

import java.util.ArrayList;
import java.util.Collections;

public class DataPlaylistRecentProcess {
    public static ArrayList<Playlists> getListDataRecently(){
        ArrayList<Playlists> listRecently = null;
        try{
            if(Data_local_Manager.getRecentArraylist() != null) {
                listRecently = Data_local_Manager.getRecentArraylist();
            }
        }catch (Exception e){
            listRecently = new ArrayList<>();
        }
        return listRecently;
    }
    public static void addRecentPlaylist(Playlists playlists) {
        ArrayList<Playlists>  listRecently = getListDataRecently();
        Boolean flag =  false;
        for (int i = 0; i < listRecently.size(); i++) {
            if(listRecently.get(i).getKeyAlbum().equals(playlists.getKeyAlbum())){
                flag = true;
                listRecently.remove(i);
                listRecently.add(0,playlists);
                Data_local_Manager.setRecentArraylist(listRecently);
            };
        }
        if(flag == false){
            listRecently.add(0,playlists);
            Data_local_Manager.setRecentArraylist(listRecently);
        }
    }

}
