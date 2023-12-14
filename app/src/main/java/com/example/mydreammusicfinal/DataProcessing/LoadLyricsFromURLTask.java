package com.example.mydreammusicfinal.DataProcessing;


import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.LyricLine;
import com.example.mydreammusicfinal.model.Songs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

public static Map<String, ArrayList<LyricLine>> lyricCache = new HashMap<>();
public static String LyricCacheString  ;

    public static void loadLyricsIntoCache(String lyricsURL) {
        if (!lyricCache.containsKey(lyricsURL)) {
            LoadLyricsFromURLTask load = new LoadLyricsFromURLTask();
            load.setOnURLLoadingListener(new LoadLyricsFromURLTask.OnURLLoadingListener() {
                @Override
                public void onURLLoaded(ArrayList<LyricLine> list) {
                    lyricCache.put(lyricsURL, list);
                }

                @Override
                public void onURLTXT(String valueString) {
                    LyricCacheString = valueString;
                }

                @Override
                public void onURLLoadFailed(String errorMessage) {
                }
            });
            load.execute(lyricsURL);
        }
    }

    public static ArrayList<LyricLine> getLyricsFromCache(String lyricsURL) {
        return lyricCache.get(lyricsURL);
    }

    public static void loadLyricsExpended(Songs obj, LinearLayout containerTextLyrics, int progress, ScrollView scrollView,Context context) {
        Resources r = containerTextLyrics.getContext().getResources();
        float textSizeInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
        ArrayList<LyricLine> cachedLyrics = getLyricsFromCache(obj.getLyricsURL());
        containerTextLyrics.removeAllViews();

        if (cachedLyrics != null) {
            ArrayList<TextView> lyricTextViews = new ArrayList<>();
            for (LyricLine lyricLine : cachedLyrics) {
                TextView newTextView = new TextView(containerTextLyrics.getContext());
                newTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeInPx);
                newTextView.setText(lyricLine.getLyric());
                containerTextLyrics.addView(newTextView);
                lyricTextViews.add(newTextView);
            }
            highlightLyricsBasedOnProgress(lyricTextViews, cachedLyrics, progress);
        } else {
            loadLyricsIntoCache(obj.getLyricsURL());
            if (cachedLyrics == null && LyricCacheString != null) {
                TextView newTextView = new TextView(containerTextLyrics.getContext());
                newTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeInPx);
                newTextView.setText(LyricCacheString);
                containerTextLyrics.addView(newTextView);
            }
        }
    }
    public static void highlightLyricsBasedOnProgress(ArrayList<TextView> lyricTextViews, ArrayList<LyricLine> cachedLyrics, int progress) {
        int lastHighlightedIndex = -1; // Lưu trữ chỉ số của lời bài hát được cuộn đến gần nhất
        for (int i = 0; i < cachedLyrics.size(); i++) {
            LyricLine lyricLine = cachedLyrics.get(i);
            if (progress >= (lyricLine.getTime() - 1000)) {
                lastHighlightedIndex = i;

                // Kiểm tra nếu màu sắc đã được thay đổi, nếu không thì mới thực hiện thay đổi
                if (lyricTextViews.get(i).getCurrentTextColor() != Color.WHITE) {
                    lyricTextViews.get(i).setTextColor(Color.WHITE);
                }
            } else {
                // Trả về màu mặc định nếu không phải là lời bài hát hiện tại
                if (lyricTextViews.get(i).getCurrentTextColor() != Color.BLACK) {
                    lyricTextViews.get(i).setTextColor(Color.BLACK);
                }
            }
        }

    }
    public static void loadLyricsCompact(Songs obj, int currentPosition, TextView textView){
        ArrayList<LyricLine> cachedLyrics = getLyricsFromCache(obj.getLyricsURL());
        textView.setText("Single: "+obj.getAritstName());
        if (cachedLyrics != null) {
                for (int i = cachedLyrics.size() - 1; i >= 0; i--) {
                    LyricLine lyricLine = cachedLyrics.get(i);
                    if (currentPosition >= (lyricLine.getTime() - 1000)) {
                        String currentLyric = lyricLine.getLyric();
                        textView.setText(currentLyric);
                        return;
                    }
                }
        } else {
            loadLyricsIntoCache(obj.getLyricsURL());
            if(cachedLyrics == null){
                if(LyricCacheString  != null){
                    textView.setText(LyricCacheString);
                }
            }
        }
    }

}
