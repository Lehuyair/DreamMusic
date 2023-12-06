package com.example.mydreammusicfinal.Local_Data;

import android.content.Context;
import android.util.Log;

import com.example.mydreammusicfinal.model.Playlists;

import java.util.ArrayList;
import java.util.Collections;

public class Data_local_Manager {
    private static final String KEY_DATA_CHECKREAPEAT = "Data_Repeat";
    private static final String KEY_DATA_CHECKSHUFFLE = "Data_Shuffle";
    private static final String KEY_DATA_RECENTPLAYLIST = "Data_RecentPlaylist";
    private static Data_local_Manager instance;
    private Shared_Preferences_MEDIA_DATA sharedPreferencesMediaData;

    public static void init(Context context){
        instance = new Data_local_Manager();
        instance.sharedPreferencesMediaData = new Shared_Preferences_MEDIA_DATA(context);
    }
     public static Data_local_Manager getInstance(){
        if(instance == null){
            instance = new Data_local_Manager();
        }
        return instance;
     }
     public static void setRepeatTrack(Boolean isCheck){
        Data_local_Manager.getInstance().sharedPreferencesMediaData.putBooleanValue(KEY_DATA_CHECKREAPEAT,isCheck);
     }
     public static Boolean getRepeatTrack(){
        return  Data_local_Manager.getInstance().sharedPreferencesMediaData.getBooleanValue(KEY_DATA_CHECKREAPEAT);
     }
     public static void setShuffleTrack(Boolean isCheck){
        Data_local_Manager.getInstance().sharedPreferencesMediaData.putBooleanValue(KEY_DATA_CHECKSHUFFLE,isCheck);
     }
     public static Boolean getShuffleTrack(){
        return  Data_local_Manager.getInstance().sharedPreferencesMediaData.getBooleanValue(KEY_DATA_CHECKSHUFFLE);
     }
     public  static void setRecentArraylist(ArrayList<Playlists> list){
        Data_local_Manager.getInstance().sharedPreferencesMediaData.putArrayListRecentValue(KEY_DATA_RECENTPLAYLIST,list);
     }
     public static ArrayList<Playlists> getRecentArraylist(){
        return Data_local_Manager.getInstance().sharedPreferencesMediaData.getArrayListRecentValue(KEY_DATA_RECENTPLAYLIST);
     }

}
