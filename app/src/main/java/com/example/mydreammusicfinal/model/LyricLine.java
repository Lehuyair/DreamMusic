package com.example.mydreammusicfinal.model;

public class LyricLine {
    int time;
    String lyric;

    public LyricLine(int time, String lyric) {
        this.time = time;
        this.lyric = lyric;
    }

    public LyricLine() {
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
}
