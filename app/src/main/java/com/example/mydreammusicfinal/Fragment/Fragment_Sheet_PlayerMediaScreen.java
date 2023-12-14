package com.example.mydreammusicfinal.Fragment;

import static com.example.mydreammusicfinal.Constance.Constance.ACTION_NEXT;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_PAUSE;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_PREVIOUS;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_RESUME;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_START;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_ACTION_MUSIC;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_BROADCAST_TO_MEDIASCREEN;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_CURRENT_SONG;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_MAXSEEKBAR;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_PROGRESS_MEDIA;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_SEEKBAR_UPDATE;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_STATUS_PLAYER;
import static com.example.mydreammusicfinal.DataProcessing.LoadLyricsFromURLTask.loadLyricsCompact;
import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.mediaPlayer;
import static com.example.mydreammusicfinal.MyApplication.checkUser;
import static com.example.mydreammusicfinal.MyApplication.sendActionToService;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mydreammusicfinal.Activity.Lyrics;
import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.DataProcessing.LikeSongProcessing;
import com.example.mydreammusicfinal.DataProcessing.LoadLyricsFromURLTask;
import com.example.mydreammusicfinal.Local_Data.Data_local_Manager;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.LyricLine;
import com.example.mydreammusicfinal.model.Songs;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Fragment_Sheet_PlayerMediaScreen extends BottomSheetDialogFragment {
    public LikeSongProcessing likeSongProcessing;
    public  TextView tvNameSong;
    public TextView tvArtist;
    public TextView tvCurrentTime;
    public TextView tvEndTime;
    public static TextView tvLyrics1;
    public ImageView btnPrev, btnPlay, btnNext,btnShuffle, btnLoop, imgExpand, imgClose,imgLike;
    public LinearLayout lnScreenMediaPlayer;
    public LinearLayout lnLyrics;
    public  ImageView imageBackground;
    public  SeekBar seekBar;
    public  SimpleDateFormat sdf ;
    public Songs msong;
    private boolean isPlaying;
    private int ACTIONMEDIA;
    public static int maxSeekBar;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle == null){
                return;
            }
            isPlaying = (boolean) bundle.get(KEY_STATUS_PLAYER);
            ACTIONMEDIA = (int) bundle.get(KEY_ACTION_MUSIC);
            msong = (Songs) bundle.getSerializable(KEY_CURRENT_SONG) ;
            handleLayoutMusic(ACTIONMEDIA);
        }
    };
    private BroadcastReceiver seekBarReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null ) {
                int progress = intent.getIntExtra(KEY_PROGRESS_MEDIA, 0);
                maxSeekBar = intent.getIntExtra(KEY_MAXSEEKBAR, 0);
                updateSeekBar(progress, maxSeekBar);
                tvEndTime.setText(sdf.format(maxSeekBar).toString());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playingsong,container,false);
        Bundle bundle = this.getArguments();
        mapping(view);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter(KEY_BROADCAST_TO_MEDIASCREEN));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(seekBarReceiver, new IntentFilter(KEY_SEEKBAR_UPDATE));
        if (bundle != null) {
            msong = (Songs) bundle.getSerializable("Song_Data");
            ACTIONMEDIA = (int) bundle.getInt("Action_Song");
            isPlaying = (boolean) bundle.getBoolean("mediaStatus",true);
            handleLayoutMusic(ACTIONMEDIA);
        }
        sdf = new SimpleDateFormat("mm:ss");
        if(getDialog() != null){
            getDialog().setOnShowListener(dialog -> {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);
                }
            });
        }
        btnLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data_local_Manager.getRepeatTrack()== false){
                    Data_local_Manager.setRepeatTrack(true);
                    btnLoop.setImageResource(R.drawable.ic_repeat_one);
                }else{
                    Data_local_Manager.setRepeatTrack(false);
                    btnLoop.setImageResource(R.drawable.ic_repeat_none);
                }

            }
        });
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data_local_Manager.getShuffleTrack()== false){
                    Data_local_Manager.setShuffleTrack(true);
                    btnShuffle.setImageResource(R.drawable.icon_shuffle_selected);
                }else{
                    Data_local_Manager.setShuffleTrack(false);
                    btnShuffle.setImageResource(R.drawable.ic_shuffle_none);
                }
            }
        });
        return view;
    }
    private void setUIControllerStatus() {
        checkLikedSong();
        if(Data_local_Manager.getRepeatTrack()){
            btnLoop.setImageResource(R.drawable.ic_repeat_one);
        }else{
            btnLoop.setImageResource(R.drawable.ic_repeat_none);
        }
        if(Data_local_Manager.getShuffleTrack()){
            btnShuffle.setImageResource(R.drawable.icon_shuffle_selected);
        }else{
            btnShuffle.setImageResource(R.drawable.ic_shuffle_none);
        }
    }


    public void mapping(View view){
        tvNameSong = view.findViewById(R.id.tv_NameSong_Foryou);
        tvArtist = view.findViewById(R.id.tvNameArtist_Foryou);
        tvCurrentTime = view.findViewById(R.id.tvCurrent_Foryou);
        tvEndTime = view.findViewById(R.id.tvEnd_Foryou);
        btnPrev = view.findViewById(R.id.img_previous_Foryou);
        btnNext = view.findViewById( R.id.img_next_Foryou);
        btnLoop = view.findViewById(R.id.img_loop_Foryou);
        btnShuffle = view.findViewById( R.id.img_shuffle_Foryou);
        btnPlay = view.findViewById(R.id.img_Play_Foryou);
        seekBar = view.findViewById(R.id.seekBar_Foryou);
        tvLyrics1 = view.findViewById(R.id.lyrics_1);
        imageBackground =view.findViewById(R.id.imgBackground);
        imgClose = view.findViewById(R.id.img_down);
        lnScreenMediaPlayer = view.findViewById(R.id.ln_screenMediaPlayer);
        lnLyrics = view.findViewById(R.id.ln_lyrics);
        imgExpand = view.findViewById(R.id.imgExand);
        imgLike = view.findViewById(R.id.img_like_Foryou);
        likeSongProcessing = new LikeSongProcessing();
    }
    private void handleLayoutMusic(int action) {
        switch (action){
            case ACTION_START:
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
            case ACTION_PREVIOUS:
                showInformationCompactMedia();
                setStatusCompactMediaPlayerController();
                break;
        }
    }
    private void showInformationCompactMedia(){
        if( msong == null){
            return;
        }
        setUIControllerStatus();

        if(getActivity() != null){
            GlideModule.loadSongImage(getContext(), imageBackground,msong.getImageURL());
        }
        if(msong.getColorCode()!=null){
            lnScreenMediaPlayer.setBackgroundColor(Color.parseColor(msong.getColorCode()));
            lnLyrics.setBackgroundColor(Color.parseColor(msong.getColorCode()));
        }
        tvNameSong.setText(msong.getSongName());
        tvArtist.setText(msong.getAritstName());
        ItemControllerOnClickListener();
    }

    private void ItemControllerOnClickListener() {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    btnPlay.setImageResource(R.drawable.ic_pause2);
                    sendActionToService(getContext(),ACTION_PAUSE, MyService.positionSongPlaying);
                }else{
                    btnPlay.setImageResource(R.drawable.ic_play32);
                    sendActionToService(getContext(),ACTION_RESUME, MyService.positionSongPlaying);
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionToService(getContext(),ACTION_NEXT, MyService.positionSongPlaying);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionToService(getContext(),ACTION_PREVIOUS, MyService.positionSongPlaying);
            }
        });
        imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Lyrics.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("mSong",msong);
                bundle.putBoolean("isPlaying",isPlaying);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slideup,R.anim.slidedown);
            }
        });
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUser()){
                    if(!msong.isLiked()){
                        likeSongProcessing.addSongTofavorite(msong);
                        imgLike.setImageResource(R.drawable.ic_heart_selected);
                        msong.setLiked(true);
                    }else{
                        likeSongProcessing.removeSongFromFavorite(msong);
                        imgLike.setImageResource(R.drawable.ic_heart_none);
                        msong.setLiked(false);
                    }
                }else{
                    Toast.makeText(getContext(), "Vui lòng đăng nhập để sử dụng tính năng này!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("KeyArtist", msong.getArtistKey());
                    Fragment_Artist fragment1 = new Fragment_Artist();
                    fragment1.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.view_pager, fragment1)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
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

    public void updateSeekBar(int progressBar, int maxBar){

        seekBar.setMax(maxBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int current = seekBar.getProgress();
                    loadLyricsCompact(msong,current,tvLyrics1);
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
                tvCurrentTime.setText(sdf.format(mediaPlayer.getCurrentPosition()).toString());
    }

    private void setStatusCompactMediaPlayerController(){
        if(isPlaying){
            btnPlay.setImageResource(R.drawable.ic_pause2);
        }else {
            btnPlay.setImageResource(R.drawable.ic_play32);
        }
    }




    private void animateTextView(TextView textView) {
           int textViewHeight = textView.getHeight();
           textView.setTranslationY(textViewHeight);
           ObjectAnimator animator = ObjectAnimator.ofFloat(textView, View.TRANSLATION_Y, 0);
           animator.setDuration(500); // Điều chỉnh thời gian theo ý muốn
           animator.start();
    }
    @Override
    public void onStart() {
        super.onStart();
        if(getDialog() != null){
            getDialog().setOnShowListener(dialog -> {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);
                }
            });
        }
    }
}
