package com.example.mydreammusicfinal.Interface;

import android.app.ProgressDialog;

import com.example.mydreammusicfinal.model.Artist;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.Songs;

public class OnItemListener {
    public static interface IOnItemSingleClickListener {
            void onItemClick(Artist Artist);
    }

    public static interface IOnItemPlaylistClickListener {
            void onItemClick(int IDAdapter, Playlists albums);
    }

    public static interface IOnItemKeyArtistClickListenter {
        void onItemClick(String keyArtist);
    }
    public  static interface  IOnItemSongsClickListerner{
        void onItemClick(Songs songs);
    }
    public static interface ItemOnclickShowDialogListener{
        void onItemShowDialog(ProgressDialog progressDialog);
    }
}
