package com.example.mydreammusicfinal.model;

public class PhotoAdvertisment {
    private String keyPlaylist;
    private int photoResource;



    public PhotoAdvertisment() {
    }

    public PhotoAdvertisment(String keyPlaylist, int photoResource) {
        this.keyPlaylist = keyPlaylist;
        this.photoResource = photoResource;
    }

    public String getKeyPlaylist() {
        return keyPlaylist;
    }

    public void setKeyPlaylist(String keyPlaylist) {
        this.keyPlaylist = keyPlaylist;
    }

    public int getPhotoResource() {
        return photoResource;
    }

    public void setPhotoResource(int photoResource) {
        this.photoResource = photoResource;
    }
}
