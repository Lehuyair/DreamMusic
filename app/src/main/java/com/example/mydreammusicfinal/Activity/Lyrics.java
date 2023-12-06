package com.example.mydreammusicfinal.Activity;


import static com.example.mydreammusicfinal.DataProcessing.LoadLyricsFromURLTask.loadLyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Songs;

public class Lyrics extends AppCompatActivity {
    ImageView imgClose;
    TextView tvNameArtist, tvNameSong, tvContent;
    RelativeLayout rlLyrics;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Bundle bundle = getIntent().getExtras();
        Songs songs = (Songs) bundle.get("mSong");
        rlLyrics =findViewById(R.id.rlLyrics);
        imgClose = findViewById(R.id.imgDownLyrics);
        tvNameArtist = findViewById(R.id.nameArtistLyrics);
        tvNameSong = findViewById(R.id.nameSongLyrics);
        tvContent = findViewById(R.id.tvContentLyrics);
        rlLyrics.setBackgroundColor(Color.parseColor(songs.getColorCode()));
        tvNameArtist.setText(songs.getAritstName());
        tvNameSong.setText(songs.getSongName());
        loadLyrics(songs,true,0,tvContent);
        imgClose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
               overridePendingTransition(R.anim.slideup,R.anim.slidedown);
           }
       });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}