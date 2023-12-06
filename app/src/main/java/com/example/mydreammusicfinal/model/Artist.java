package com.example.mydreammusicfinal.model;

public class Artist {
    private String nameArtist;
    private String imageURL;
    private String  keyArtist;

    public Artist(String nameArtist, String imageURL, String keyArtist) {
        this.nameArtist = nameArtist;
        this.imageURL = imageURL;
        this.keyArtist = keyArtist;
    }

    public Artist() {
    }

    public String getKeyArtist() {
        return keyArtist;
    }

    public void setKeyArtist(String keyArtist) {
        this.keyArtist = keyArtist;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
