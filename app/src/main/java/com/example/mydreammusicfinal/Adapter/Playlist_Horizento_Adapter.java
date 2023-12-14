package com.example.mydreammusicfinal.Adapter;

import static com.example.mydreammusicfinal.Local_Data.DataPlaylistRecentProcess.addRecentPlaylist;
import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
import static com.example.mydreammusicfinal.MyApplication.clickStartService;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.DataProcessing.getDataPlaylistSongsByKeyPlaylist;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.Songs;

import java.util.ArrayList;


public class Playlist_Horizento_Adapter extends RecyclerView.Adapter<Playlist_Horizento_Adapter.ForYou_TrendHolder>{
    Context context;
    ArrayList<Playlists> list;
    private OnItemListener.IOnItemPlaylistClickListener listener;
    public Playlist_Horizento_Adapter(Context context, ArrayList<Playlists> list) {
        this.context = context;
        this.list = list;
    }
    ProgressDialog progressDialog;

    private BroadcastReceiver MessageReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            if(message != null){
                dismissProgressDialog();
            }
        }
    };

    @NonNull
    @Override
    public ForYou_TrendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_playlist_horizento,parent,false);
        return new ForYou_TrendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForYou_TrendHolder holder, int position) {
        registerReceiver();
        Playlists albums = list.get(position);
        if(albums == null){return;}
        holder.tvNameAlbums.setText(albums.getNameAlbum());
            GlideModule.loadSongImage(context,holder.imgAlbums,albums.getImageURL());
            holder.itemView.setOnClickListener(view -> {
                if (listener != null) {
                listener.onItemClick(1,albums);
                addRecentPlaylist(albums);
            }
            });
        holder.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                getDataPlaylistSongsByKeyPlaylist task = new getDataPlaylistSongsByKeyPlaylist("playlists",albums.getKeyAlbum(), new CallBackListener.SongsCallBack() {
                    @Override
                    public void onCallbackSong(ArrayList<Songs> list) {
                        positionSongPlaying = 0;
                        MyService.setListSongPlaying(list);
                        clickStartService(context);
                    }
                });
                task.execute();
            }
        });


    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ForYou_TrendHolder extends RecyclerView.ViewHolder {
    ImageView imgAlbums,imgPlay;
    TextView tvNameAlbums;
        public ForYou_TrendHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbums = itemView.findViewById(R.id.img_album);
            tvNameAlbums = itemView.findViewById(R.id.tv_Name_Album);
            imgPlay = itemView.findViewById(R.id.imgPlayAlbum);
        }
    }
    public void setOnItemClickListener(OnItemListener.IOnItemPlaylistClickListener listener) {
        this.listener = listener;
    }
    private void showProgressDialog(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Đang tải...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void registerReceiver() {
        LocalBroadcastManager.getInstance(context).registerReceiver(MessageReciever, new IntentFilter("DismissDialog"));
    }
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
