package com.example.mydreammusicfinal.Interface;

import com.example.mydreammusicfinal.model.Artist;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.Songs;

import java.util.ArrayList;

public class CallBackListener {
    public static interface SongsCallBack {
        void onCallbackSong(ArrayList<Songs> list);
    }

    public static interface PlaylistCallBack {
        void onCallbackAlbum(ArrayList<Playlists> list);
    }

    public static interface ArtistObjectCallBack {
        void onCallBackObjectArtist(Artist artist);
    }
    public static interface  ArtistCallBack{
        void onCallBackArtist(ArrayList<Artist> list);
    }
}
