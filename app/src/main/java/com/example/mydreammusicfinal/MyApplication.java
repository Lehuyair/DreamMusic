package com.example.mydreammusicfinal;

import static com.example.mydreammusicfinal.Constance.Constance.ACTION_START;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_ACTION_STATUS_SERVICE;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import com.example.mydreammusicfinal.Local_Data.Data_local_Manager;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {
    public static final String Channel_ID_Name = "Channel_ID_MediaController";
    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
    }
    public void createChannelNotification(){
        Data_local_Manager.init(getApplicationContext());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(Channel_ID_Name,"Channel_ID_MediaController_Name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if(manager!=null){
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void clickStartService(Context context) {
        Intent intent = new Intent(context, MyService.class);
        intent.putExtra(KEY_ACTION_STATUS_SERVICE,ACTION_START);
        context.startService(intent);
    }
    public static  void sendActionToService(Context context,int Action, int songPosition){
        Intent intent = new Intent(context, MyService.class);
        intent.putExtra(KEY_ACTION_STATUS_SERVICE,Action);
        context.startService(intent);
    }

    public static Boolean  checkUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return false;
        }else{
            return true;
        }
    }
}
