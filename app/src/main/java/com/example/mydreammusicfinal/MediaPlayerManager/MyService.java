package com.example.mydreammusicfinal.MediaPlayerManager;



import static com.example.mydreammusicfinal.Constance.Constance.ACTION_CLEAR;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_NEXT;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_PAUSE;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_PREVIOUS;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_RESUME;
import static com.example.mydreammusicfinal.Constance.Constance.ACTION_START;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_ACTION_MUSIC;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_ACTION_STATUS_SERVICE;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_ACTION_TORECIEVER_STATUS;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_BROADCAST_TO_MEDIASCREEN;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_CURRENT_SONG;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_MAXSEEKBAR;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_PROGRESS_MEDIA;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_SEEKBAR_UPDATE;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_SEND_DATA_TO_ACTIGVITY;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_STATUS_PLAYER;
import static com.example.mydreammusicfinal.MyApplication.Channel_ID_Name;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mydreammusicfinal.Activity.MainActivity;
import com.example.mydreammusicfinal.Constance.Constance;
import com.example.mydreammusicfinal.Local_Data.Data_local_Manager;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Songs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MyService extends Service {

    public static ArrayList<Songs> listSongPlaying;
    public static int positionSongPlaying = 0;
    private boolean isPlaying;
    public static MediaPlayer mediaPlayer;
    int progress;
    int maxSeekbar;
    Handler handler = new Handler();
    MainActivity m = new MainActivity();


    public static void setListSongPlaying(ArrayList<Songs> list){
        listSongPlaying = list;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!= null){
                    try {
                        if(listSongPlaying == null){
                            listSongPlaying = new ArrayList<>();
                        }
                        if(listSongPlaying.size() != 0){
                            int actionMusic = intent.getIntExtra(KEY_ACTION_STATUS_SERVICE,0);
                            if(actionMusic == ACTION_START){
                                startMusic();
                            }
                            sendNotification(mediaPlayer.getDuration(),mediaPlayer.getCurrentPosition());
                            handlerActionMusic(actionMusic);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        return START_NOT_STICKY;

    }

    private void startMusic() throws IOException {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        } else {
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(listSongPlaying.get(positionSongPlaying).getSongURL());
            mediaPlayer.prepare();
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        isPlaying = true;
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                sendNotification(mediaPlayer.getDuration(),mediaPlayer.getCurrentPosition());
            }
        });
        sendActionToActivity(ACTION_START);
        sendActionToMediaScreen(ACTION_START);
        updateSeekBar.run();
        setCompeleteSong();

    }
    public void handlerActionMusic(int Action) throws IOException {
        switch (Action){
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_NEXT:
                nextSong();
                break;
            case ACTION_PREVIOUS:
                prevSong();
                break;
        }
    }

    private void clearMusic() {
        stopSelf();
        sendActionToActivity(ACTION_CLEAR);
        sendActionToMediaScreen(ACTION_CLEAR);

    }

    private void resumeMusic() {
        if(mediaPlayer != null && isPlaying == false){
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(mediaPlayer.getDuration(),mediaPlayer.getCurrentPosition());
            sendActionToActivity(ACTION_RESUME);
            sendActionToMediaScreen(ACTION_RESUME);
            updateSeekBar.run();
            setCompeleteSong();
        }

    }

    private void pauseMusic() {
        if(mediaPlayer != null && isPlaying == true){
            mediaPlayer.pause();
            isPlaying = false;
            sendNotification(mediaPlayer.getDuration(),mediaPlayer.getCurrentPosition());
            sendActionToActivity(ACTION_PAUSE);
            sendActionToMediaScreen(ACTION_PAUSE);
        }
    }
    public void prevSong() throws IOException {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        if (listSongPlaying.size() > 1) {
            if (positionSongPlaying > 0) {
                positionSongPlaying--;
            } else {
                positionSongPlaying = listSongPlaying.size() - 1;
            }
        } else {
            positionSongPlaying = 0;
        }
        startMusic();
        sendActionToActivity(ACTION_START);
        sendActionToMediaScreen(ACTION_START);
        sendNotification(mediaPlayer.getDuration(),mediaPlayer.getCurrentPosition());
        updateSeekBar.run();
        setCompeleteSong();
    }

    private void nextSong() throws IOException {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        if(Data_local_Manager.getShuffleTrack()){
            shuffleChecked();
        }else{
            if (listSongPlaying.size() > 1 && positionSongPlaying < listSongPlaying.size() - 1) {
                positionSongPlaying++;
            } else {
                positionSongPlaying = 0;
            }
        }

        startMusic();
        sendNotification(mediaPlayer.getDuration(),mediaPlayer.getCurrentPosition());
        sendActionToActivity(ACTION_START);
        sendActionToMediaScreen(ACTION_START);
        updateSeekBar.run();
        setCompeleteSong();

    }
    public void setCompeleteSong(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        try {
                            handleMusicCompletion();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        },1000);
    }
    public void handleMusicCompletion() throws IOException {
        if(Data_local_Manager.getRepeatTrack()){
            startMusic();
        }else{
            try {
                nextSong();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void shuffleChecked(){
        Random random = new Random();
        positionSongPlaying =  (int)random.nextInt(listSongPlaying.size());
    }



    private void sendNotification(int progress, int max) {
        Songs s1 = listSongPlaying.get(positionSongPlaying);
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this,"Tag");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Channel_ID_Name)
                .setSmallIcon(R.drawable.ic_notification)
                .setSubText("Dream Music")
                .setContentTitle(s1.getSongName())
                .setSubText(s1.getAritstName())
                .setContentText(s1.getAritstName())
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0,1,2)
                        .setMediaSession(mediaSessionCompat.getSessionToken()));
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(s1.getImageURL())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        notificationBuilder.setLargeIcon(resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        if(isPlaying){
                    notificationBuilder.addAction(R.drawable.ic_previous,"Previous",getPendingIntent(this,ACTION_PREVIOUS))
                    .addAction(R.drawable.ic_pause,"Play",getPendingIntent(this,ACTION_PAUSE))
                    .addAction(R.drawable.ic_next,"Next",getPendingIntent(this,ACTION_NEXT));
        }else{
            notificationBuilder.addAction(R.drawable.ic_previous,"Previous",getPendingIntent(this,ACTION_PREVIOUS))
                    .addAction(R.drawable.ic_play,"Play",getPendingIntent(this,ACTION_RESUME))
                    .addAction(R.drawable.ic_next,"Next",getPendingIntent(this,ACTION_NEXT));
        }
        notificationBuilder.setProgress(max,progress,false);
        Notification notification = notificationBuilder.build();
        startForeground(1,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private PendingIntent getPendingIntent(Context context, int Action){
        Intent intent = new Intent(this, myReceiver.class);
        intent.putExtra(KEY_ACTION_TORECIEVER_STATUS, Action);
        return  PendingIntent.getBroadcast(context.getApplicationContext(), Action,intent,
                PendingIntent.FLAG_IMMUTABLE);
    }
    private void sendActionToActivity(int action){
        Intent intent = new Intent(KEY_SEND_DATA_TO_ACTIGVITY);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_STATUS_PLAYER,isPlaying);
        bundle.putInt(KEY_ACTION_MUSIC,action);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    private void sendActionToMediaScreen(int action){
        Intent intent = new Intent(KEY_BROADCAST_TO_MEDIASCREEN);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_STATUS_PLAYER,isPlaying);
        bundle.putInt(KEY_ACTION_MUSIC,action);
        bundle.putSerializable(KEY_CURRENT_SONG,listSongPlaying.get(positionSongPlaying));
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                progress = mediaPlayer.getCurrentPosition();
                maxSeekbar = mediaPlayer.getDuration();
                sendSeekBarUpdateToActivity(progress,maxSeekbar);
            }
            handler.postDelayed(this, 1000);
        }
    };
    private void sendSeekBarUpdateToActivity(int progress, int maxSeekBar) {
        Intent intent = new Intent(KEY_SEEKBAR_UPDATE);
        intent.putExtra(KEY_PROGRESS_MEDIA, progress);
        intent.putExtra(KEY_MAXSEEKBAR, maxSeekBar);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
