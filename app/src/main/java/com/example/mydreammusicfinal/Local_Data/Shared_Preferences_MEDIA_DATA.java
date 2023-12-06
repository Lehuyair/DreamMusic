package com.example.mydreammusicfinal.Local_Data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mydreammusicfinal.model.Playlists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Shared_Preferences_MEDIA_DATA {
    SharedPreferences sharedPreferences;
    Context context;
    private static final String Preferences_Data_Boolean = "Media_SHARED_BooleanValue";
    private static final String Preferences_Data_String = "Media_SHARED_StringValue";
    private static final String Preferences_Data_Arraylist= "Media_SHARED_ArrayListValue";

    public Shared_Preferences_MEDIA_DATA(Context context) {
        this.context = context;
    }
    public void putBooleanValue(String key, Boolean value){
        sharedPreferences =  context.getSharedPreferences(Preferences_Data_Boolean, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public boolean getBooleanValue(String key){
        sharedPreferences =  context.getSharedPreferences(Preferences_Data_Boolean, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }
    public  void putStringValue(String key, String value){
        sharedPreferences =  context.getSharedPreferences(Preferences_Data_String, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public String getStringValue(String key){
        sharedPreferences =  context.getSharedPreferences(Preferences_Data_String, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

    public void putArrayListRecentValue(String key, ArrayList<Playlists>list){
        sharedPreferences =  context.getSharedPreferences(Preferences_Data_Arraylist, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key,json);
        editor.apply();
    }
    public ArrayList<Playlists> getArrayListRecentValue(String key){
        sharedPreferences =  context.getSharedPreferences(Preferences_Data_Arraylist, Context.MODE_PRIVATE);
        ArrayList<Playlists> list = new ArrayList<>();
        String json = sharedPreferences.getString(key, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Playlists>>() {}.getType();
            list = gson.fromJson(json, type);
        }
        return list;
    }
}
