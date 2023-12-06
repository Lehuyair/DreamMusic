package com.example.mydreammusicfinal.model;

import java.io.Serializable;
import java.util.Objects;

public class Songs implements Serializable {
    private String aritstName;
    private String songName;
    private long count;
    private String colorCode;
    private String songURL;
    private String imageURL;

    private String lyricsURL;
    private String songKey;
    private String artistKey;
    private boolean isLiked; // Thêm trường để lưu trữ trạng thái liked

    // Các phương thức getter và setter cho trường isLiked
    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }


    public Songs() {
    }



    public Songs(String aritstName, String songName, long count, String colorCode, String songURL, String imageURL, String lyricsURL, String songKey) {
        this.aritstName = aritstName;
        this.songName = songName;
        this.count = count;
        this.colorCode = colorCode;
        this.songURL = songURL;
        this.imageURL = imageURL;
        this.lyricsURL = lyricsURL;
        this.songKey = songKey;
    }
    public Songs(String aritstName, String songName, long count, String colorCode, String songURL, String imageURL, String lyricsURL, String songKey,String ArtistKey) {
        this.aritstName = aritstName;
        this.songName = songName;
        this.count = count;
        this.colorCode = colorCode;
        this.songURL = songURL;
        this.imageURL = imageURL;
        this.lyricsURL = lyricsURL;
        this.songKey = songKey;
        this.artistKey = ArtistKey;
    }
    public String getArtistKey() {
        return artistKey;
    }

    public void setArtistKey(String artistKey) {
        this.artistKey = artistKey;
    }

    public String getSongKey() {
        return songKey;
    }

    public void setSongKey(String songKey) {
        this.songKey = songKey;
    }
    public String getAritstName() {
        return aritstName;
    }

    public void setAritstName(String aritstName) {
        this.aritstName = aritstName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getSongURL() {
        return songURL;
    }

    public void setSongURL(String songURL) {
        this.songURL = songURL;
    }

    public String getLyricsURL() {
        return lyricsURL;
    }

    public void setLyricsURL(String lyricsURL) {
        this.lyricsURL = lyricsURL;
    }


}
