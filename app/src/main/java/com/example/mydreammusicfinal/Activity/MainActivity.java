package com.example.mydreammusicfinal.Activity;


import static com.example.mydreammusicfinal.Constance.Constance.ACTION_NEXT;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_PAUSE;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_RESUME;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_START;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_ACTION_MUSIC;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_ACTION_STATUS_SERVICE;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_MAXSEEKBAR;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_PROGRESS_MEDIA;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_SEEKBAR_UPDATE;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_SEND_DATA_TO_ACTIGVITY;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_STATUS_PLAYER;
import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.listSongPlaying;
import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.mediaPlayer;
import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
import static com.example.mydreammusicfinal.MyApplication.checkUser;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mydreammusicfinal.Adapter.Child_Playlist_ItemAdapter;
import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.DataProcessing.LikeSongProcessing;
import com.example.mydreammusicfinal.Fragment.Fragment_Discovery;
import com.example.mydreammusicfinal.Fragment.Fragment_ForYou;
import com.example.mydreammusicfinal.Fragment.Fragment_Me;
import com.example.mydreammusicfinal.Fragment.Fragment_Sheet_PlayerMediaScreen;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.MyApplication;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Songs;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView bottomNavigationView;
    public static LinearLayout viewPager;
    RelativeLayout rl_MediaCompact;
    public static TextView tvNameArtist, tvNameSong, tvStatusInternet;
    public static ImageView imgSong, imgLike, imgPlay, imgNext;
    public static SeekBar seekBar;
    private boolean isPlaying;
    private int ACTIONMEDIA;
    LikeSongProcessing likeSongProcessing;
    FragmentManager fragmentManager;
    public static Songs msong;
    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            isPlaying = (boolean) bundle.get(KEY_STATUS_PLAYER);
            ACTIONMEDIA = (int) bundle.get(KEY_ACTION_MUSIC);
            handleLayoutMusic(ACTIONMEDIA);
        }
    };
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUI();
        setViewDefault();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(KEY_SEND_DATA_TO_ACTIGVITY));
        LocalBroadcastManager.getInstance(this).registerReceiver(seekBarReceiver, new IntentFilter(KEY_SEEKBAR_UPDATE));
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
        isDataFromNotification();
        setTextItemNavigation();
        bottomNavigationController();

    }
    public void setStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    public void setUI() {
        tvStatusInternet = findViewById(R.id.tvStatusInternet);
        rl_MediaCompact = findViewById(R.id.rl_mediaCompact_Controller);
        tvNameArtist = findViewById(R.id.tv_NameArtist_CompactController);
        tvNameSong = findViewById(R.id.tv_NameSong_CompactController);
        imgSong = findViewById(R.id.img_CompactController);
        imgLike = findViewById(R.id.imgLike_CompactController);
        imgPlay = findViewById(R.id.imgPlay_CompactController);
        imgNext = findViewById(R.id.imgNext_CompactController);
        seekBar = findViewById(R.id.seekbar_CompactController);
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_nav);


    }
    private void setTextItemNavigation() {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SpannableString spannable = new SpannableString(menuItem.getTitle());
            spannable.setSpan(new AbsoluteSizeSpan(11, true), 0, spannable.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(spannable);
        }
    }
    private void setViewDefault() {
        fragmentManager = getSupportFragmentManager();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, new Fragment_ForYou())
                .commit();
    }
    private void bottomNavigationController() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_Home) {
                    setStatusBar();
                    replaceFragment(new Fragment_ForYou() );
                }
                if (item.getItemId() == R.id.nav_Discovery) {
                    setStatusBar();
                    replaceFragment(new Fragment_Discovery());
                }
                if (item.getItemId() == R.id.nav_Me) {
                    setStatusBar();
                    replaceFragment(new Fragment_Me() );
                }
                return true;
            }
        });
    }

    private void clickStopService() {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        clickStopService();
        unregisterReceiver(networkChangeReceiver);
    }

    private void handleLayoutMusic(int action) {
        switch (action) {
            case ACTION_START:
                rl_MediaCompact.setVisibility(View.VISIBLE);
                showInformationCompactMedia();
                setStatusCompactMediaPlayerController();
                break;
            case ACTION_PAUSE:
                setStatusCompactMediaPlayerController();
                break;
            case ACTION_RESUME:
                setStatusCompactMediaPlayerController();
                break;
            case ACTION_NEXT:
                showInformationCompactMedia();
                setStatusCompactMediaPlayerController();
                break;
        }
    }
    private void isDataFromNotification(){
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            Songs songData = (Songs) bundle.getSerializable("Song_Data");
            int actionSong = bundle.getInt("Action_Song");
            boolean mediaStatus = bundle.getBoolean("mediaStatus");
            if(songData != null){
                gotoSheetMediaScreen(songData,actionSong,mediaStatus);
            }
            handleLayoutMusic(actionSong);

        }
    }

    private void showInformationCompactMedia() {
        msong = listSongPlaying.get(positionSongPlaying);
        if (msong == null) {
            return;
        }
        rl_MediaCompact.setBackgroundColor(Color.parseColor(msong.getColorCode()));
        checkLikedSong();
        GlideModule.loadSongImage(this, imgSong, msong.getImageURL());
        tvNameSong.setText(msong.getSongName());
        tvNameArtist.setText(msong.getAritstName());
        OnclicListenerMediaCompactController();
    }


    private void setStatusCompactMediaPlayerController() {
        if (isPlaying) {
            imgPlay.setImageResource(R.drawable.ic_pause2);
        } else {
            imgPlay.setImageResource(R.drawable.ic_play32);
        }
    }
    private void checkLikedSong() {
        likeSongProcessing = new LikeSongProcessing();
        likeSongProcessing.checkLiked(msong, new LikeSongProcessing.OnDataReceivedListener() {
            @Override
            public void onDataReceived(boolean isLiked) {
                msong.setLiked(isLiked);
                if (msong.isLiked()) {
                    imgLike.setImageResource(R.drawable.ic_heart_selected);
                } else {
                    imgLike.setImageResource(R.drawable.ic_heart_none);
                }
            }
        });
    }
    private void OnclicListenerMediaCompactController() {
        rl_MediaCompact.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgLike.setOnClickListener(this);
    }
    public void updateSeekBar(int progressBar, int maxBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(progressBar);
            }
        }, 1000);
    }

    private void sendActionToService(int Action, int songPosition) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(KEY_ACTION_STATUS_SERVICE, Action);
        startService(intent);
    }

    public static void LogOut(Context context) {
        FirebaseAuth.getInstance().signOut();
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void replaceFragment(Fragment f){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.view_pager,f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.imgPlay_CompactController){
            if (isPlaying) {
                sendActionToService(ACTION_PAUSE, MyService.positionSongPlaying);
            } else {
                sendActionToService(ACTION_RESUME, MyService.positionSongPlaying);
            }
        }else

        if(v.getId()==R.id.imgNext_CompactController){
            sendActionToService(ACTION_NEXT, MyService.positionSongPlaying);
        }else

        if(v.getId()==R.id.imgLike_CompactController){
            if(checkUser()) {
                if (!msong.isLiked()) {
                    likeSongProcessing.addSongTofavorite(msong);
                    imgLike.setImageResource(R.drawable.ic_heart_selected);
                    msong.setLiked(true);
                }else{
                    likeSongProcessing.removeSongFromFavorite(msong);
                    imgLike.setImageResource(R.drawable.ic_heart_none);
                    msong.setLiked(false);
                }
            }else{
                Toast.makeText(this, "Vui lòng đăng nhập để có thể sử dụng tính năng này!", Toast.LENGTH_SHORT).show();
            }
        }else

        if(v.getId()==R.id.rl_mediaCompact_Controller){
            gotoSheetMediaScreen(msong,ACTION_START,isPlaying);
        }
    }
    private void gotoSheetMediaScreen(Songs songData, int Action, Boolean IsPlaying){
        Fragment_Sheet_PlayerMediaScreen mediaPlayerScreen = new Fragment_Sheet_PlayerMediaScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Song_Data", songData);
        bundle.putInt("Action_Song", Action);
        bundle.putBoolean("mediaStatus", IsPlaying);
        mediaPlayerScreen.setArguments(bundle);
        mediaPlayerScreen.show(getSupportFragmentManager(), mediaPlayerScreen.getTag());
    }
    public static class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isNetworkAvailable(context)) {
                Toast.makeText(context, "Your Internet is connected !", Toast.LENGTH_SHORT).show();
                tvStatusInternet.setVisibility(View.GONE);
            } else {
                tvStatusInternet.setText("Your internet is disconnected !");
                tvStatusInternet.setVisibility(View.VISIBLE);

            }
        }

        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
            return false;
        }
    }
}