package com.example.mydreammusicfinal.MediaPlayerManager;

import static com.example.mydreammusicfinal.Constance.Constance.KEY_ACTION_STATUS_SERVICE;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_ACTION_TORECIEVER_STATUS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class myReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int actionMusic = intent.getIntExtra(KEY_ACTION_TORECIEVER_STATUS, 0);
        Intent intentService = new Intent(context,MyService.class);
        intentService.putExtra(KEY_ACTION_STATUS_SERVICE,actionMusic);
        context.startService(intentService);

    }
}
