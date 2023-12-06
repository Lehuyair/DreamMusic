package com.example.mydreammusicfinal.model;

import java.io.Serializable;
import java.util.Objects;

public class Playlists implements Serializable {
    private String nameAlbum;
    private String imageURL;
    private String nameArtist;
    private String keyAlbum;
    private String imageBGURL;
    private String colorCode;

    public Playlists() {
    }

    public Playlists(String nameAlbum, String imageURL, String nameArtist) {
        this.nameAlbum = nameAlbum;
        this.imageURL = imageURL;
        this.nameArtist = nameArtist;
    }

    public Playlists(String nameAlbum, String imageURL, String nameArtist, String keyAlbum, String imageBGURL, String colorCode) {
        this.nameAlbum = nameAlbum;
        this.imageURL = imageURL;
        this.nameArtist = nameArtist;
        this.keyAlbum = keyAlbum;
        this.imageBGURL = imageBGURL;
        this.colorCode = colorCode;
    }
    public  Playlists(String keyPlaylist, String namePlaylist){
        this.nameAlbum = namePlaylist;
        this.keyAlbum = keyPlaylist;
    }

    public String getImageBGURL() {
        return imageBGURL;
    }

    public void setImageBGURL(String imageBGURL) {
        this.imageBGURL = imageBGURL;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public String getKeyAlbum() {
        return keyAlbum;
    }

    public void setKeyAlbum(String keyAlbum) {
        this.keyAlbum = keyAlbum;
    }


    public boolean equals(Playlists obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Playlists other = (Playlists) obj;
        return Objects.equals(nameAlbum, other.nameAlbum) &&
                Objects.equals(nameArtist, other.nameArtist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameAlbum, nameArtist);
    }
}



