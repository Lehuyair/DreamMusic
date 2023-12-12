package com.example.mydreammusicfinal.Activity;


import static com.example.mydreammusicfinal.Constance.Constance.ACTION_PAUSE;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_RESUME;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_MAXSEEKBAR;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_PROGRESS_MEDIA;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_SEEKBAR_UPDATE;
import static com.example.mydreammusicfinal.DataProcessing.LoadLyricsFromURLTask.loadLyricsCompact;
import static com.example.mydreammusicfinal.DataProcessing.LoadLyricsFromURLTask.loadLyricsExpended;
import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.mediaPlayer;
import static com.example.mydreammusicfinal.MyApplication.sendActionToService;


import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mydreammusicfinal.DataProcessing.LoadLyricsFromURLTask;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.LyricLine;
import com.example.mydreammusicfinal.model.Songs;

import java.util.ArrayList;

public class Lyrics extends AppCompatActivity {

    ImageView imgClose, imgPlay;
    TextView tvNameArtist, tvNameSong, tvContent;
    RelativeLayout rlLyrics;
    TextView tvTime;
    LinearLayout ln_container_lyrics;
    ScrollView ScrollView_;
    SeekBar seekBar;
    SimpleDateFormat sdf;
    Boolean isPlaying;
    Songs songs;
    private BroadcastReceiver seekBarReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int progress = intent.getIntExtra(KEY_PROGRESS_MEDIA,0);
                int maxSeekBar = intent.getIntExtra(KEY_MAXSEEKBAR,0);
                updateSeekBar(progress, maxSeekBar);
                if(seekBar!= null){
                    seekBar.setMax(maxSeekBar);
                }
            }
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Bundle bundle = getIntent().getExtras();
        songs = (Songs) bundle.get("mSong");
        isPlaying = bundle.getBoolean("isPlaying");
        setUI();
        LocalBroadcastManager.getInstance(this).registerReceiver(seekBarReceiver, new IntentFilter(KEY_SEEKBAR_UPDATE));
        setStatusCompactMediaPlayerController();
//        rlLyrics.setBackgroundColor(Color.parseColor(songs.getColorCode()));
        tvNameArtist.setText(songs.getAritstName());
        tvNameSong.setText(songs.getSongName());
        imgClose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
               overridePendingTransition(R.anim.slideup,R.anim.slidedown);
           }
       });
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    imgPlay.setImageResource(R.drawable.ic_play32);
                    sendActionToService(getApplicationContext(),ACTION_PAUSE, MyService.positionSongPlaying);
                    isPlaying = false;
                }else{
                    imgPlay.setImageResource(R.drawable.ic_pause2);
                    sendActionToService(getApplicationContext(),ACTION_RESUME, MyService.positionSongPlaying);
                    isPlaying = true;
                }
            }
        });

    }

    private void setUI() {
        rlLyrics =findViewById(R.id.rlLyrics);
        imgClose = findViewById(R.id.imgDownLyrics);
        tvNameArtist = findViewById(R.id.nameArtistLyrics);
        tvNameSong = findViewById(R.id.nameSongLyrics);
        tvContent = findViewById(R.id.tvContentLyrics);
        ln_container_lyrics = findViewById(R.id.ln_container_lyrics);
        tvTime = findViewById(R.id.tvCurrentTime_Lyrics);
        ScrollView_ = findViewById(R.id.ScrollView_);
        seekBar = findViewById(R.id.seekbar_Lyrics);
        imgPlay = findViewById(R.id.img_Play_lyrics);
        sdf = new SimpleDateFormat("mm:ss");

    }
    private void setStatusCompactMediaPlayerController(){
        if(isPlaying){
            imgPlay.setImageResource(R.drawable.ic_pause2);
        }else {
            imgPlay.setImageResource(R.drawable.ic_play32);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void updateSeekBar(int progressBar, int maxBar) {
        seekBar.setMax(maxBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int current = seekBar.getProgress();
                    loadLyricsExpended(songs,ln_container_lyrics,current,ScrollView_,getApplicationContext());

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });
        seekBar.setProgress(progressBar);
        tvTime.setText(sdf.format(mediaPlayer.getCurrentPosition()).toString());
    }


}