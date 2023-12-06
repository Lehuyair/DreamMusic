package com.example.mydreammusicfinal.Adapter;

import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
import static com.example.mydreammusicfinal.MyApplication.clickStartService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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


public class Playlist_Vertical_Adapter extends RecyclerView.Adapter<Playlist_Vertical_Adapter.Vertical_Holder>{
    Context context;
    ArrayList<Playlists> list;
    private OnItemListener.IOnItemPlaylistClickListener listener;
    public Playlist_Vertical_Adapter(Context context, ArrayList<Playlists> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public Vertical_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_vertical_item,parent,false);
        return new Vertical_Holder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull Vertical_Holder holder, int position) {
        final int[] amount = {0};
        Playlists albums = list.get(position);
        if(albums == null){return;}
        holder.tvNameAlbums.setText(albums.getNameAlbum());
        if(albums.getImageURL()==null){
            holder.imgAlbums.setImageResource(R.id.imgAvatar_YourPlaylist);
        }
        GlideModule.loadSongImage(context,holder.imgAlbums,albums.getImageURL());
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(1,albums);
            }
        });
        getDataPlaylistSongsByKeyPlaylist task = new getDataPlaylistSongsByKeyPlaylist("playlists",albums.getKeyAlbum(), new CallBackListener.SongsCallBack() {
            @Override
            public void onCallbackSong(ArrayList<Songs> list) {
                amount[0] = list.size();
                holder.tvAmount.setText(amount[0] + " Songs");
            }
        });
        task.execute();

        holder.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public class Vertical_Holder extends RecyclerView.ViewHolder {
        ImageView imgAlbums,imgPlay;
        TextView tvNameAlbums,tvAmount;
        public Vertical_Holder(@NonNull View itemView) {
            super(itemView);
            imgAlbums = itemView.findViewById(R.id.img_Recent);
            imgPlay = itemView.findViewById(R.id.img_playRecently);
            tvNameAlbums = itemView.findViewById(R.id.tv_NameRecent);
            tvAmount = itemView.findViewById(R.id.tv_AmountSongs);
        }
    }
    public void setOnItemClickListener(OnItemListener.IOnItemPlaylistClickListener listener) {
        this.listener = listener;
    }

}
