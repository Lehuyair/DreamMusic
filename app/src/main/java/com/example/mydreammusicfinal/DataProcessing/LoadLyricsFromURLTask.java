package com.example.mydreammusicfinal.DataProcessing;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.mydreammusicfinal.model.LyricLine;
import com.example.mydreammusicfinal.model.Songs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoadLyricsFromURLTask extends AsyncTask<String, Void, String> {
    public interface OnURLLoadingListener {
        void onURLLoaded(ArrayList<LyricLine> list);
        void onURLTXT(String valueString);
        void onURLLoadFailed(String errorMessage);
    }



    public OnURLLoadingListener Listener;


    public void setOnURLLoadingListener(OnURLLoadingListener Listener) {
        this.Listener = Listener;
    }


    @Override
    protected String doInBackground(String... strings) {
        String urlString = strings[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                reader.close();
                return stringBuilder.toString();
            } else {
                return "Không thể kết nối đến URL";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Lỗi: " + e.getMessage();
        }
    }
    public  void SynctimeLyricsProcessing( String result) {
        ArrayList<LyricLine> listLyric = new ArrayList<>();
        String[] lines = result.split("\n");
        for (String line : lines) {
            String[] parts = line.split("]");
            if (parts.length > 1) {
                String time = parts[0].substring(1);
                String lyrics = parts[1].trim();
                int timeInMilliseconds = convertTimeToMilliseconds(time);
                listLyric.add(new LyricLine(timeInMilliseconds,lyrics));
                Listener.onURLLoaded(listLyric);
            }else{
                Listener.onURLTXT(result);
            }
        }
    }
    private int convertTimeToMilliseconds(String time) {
        String[] timeParts = time.split(":");
        if (timeParts.length == 2) {
            String minutePart = timeParts[0].substring(1);
            String[] secondsParts = timeParts[1].split("\\.");
            int minutes = Integer.parseInt(minutePart);
            int seconds = Integer.parseInt(secondsParts[0]);
            int milliseconds = Integer.parseInt(secondsParts[1]);
            return (minutes * 60 + seconds) * 1000 + milliseconds;
        }
        return 0;
    }

    @Override
    protected void onPostExecute(String result) {
            SynctimeLyricsProcessing(result);
    }
    public static void loadLyrics(Songs obj, Boolean isExpand, int currentPosition, TextView textView){
        LoadLyricsFromURLTask load = new LoadLyricsFromURLTask();
        load.setOnURLLoadingListener(new LoadLyricsFromURLTask.OnURLLoadingListener() {
            @Override
            public void onURLLoaded( ArrayList<LyricLine> list) {
                textView.setText("Single: "+obj.getAritstName());
                if(!isExpand){
                    for (LyricLine lyricLine : list) {
                        if (currentPosition >= lyricLine.getTime()-2200) {
                            textView.setText(lyricLine.getLyric());
                        }
                    }
                }else{
                    StringBuilder stringBuilder = new StringBuilder();
                    for (LyricLine lyricLine : list){
                        stringBuilder.append(lyricLine.getLyric()+"\n");
                    }
                    textView.setText(stringBuilder);
                }
            }

            @Override
            public void onURLTXT(String valueString) {
                textView.setText(valueString);
            }

            @Override
            public void onURLLoadFailed(String errorMessage) {

            }
        });
        load.execute(obj.getLyricsURL());
    }
}