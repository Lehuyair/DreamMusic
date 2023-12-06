package com.example.mydreammusicfinal.Adapter;

import static com.example.mydreammusicfinal.Local_Data.DataPlaylistRecentProcess.addRecentPlaylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.Fragment.Fragment_ForYou;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Playlists;

import java.util.ArrayList;


public class Playlist_Grid_Adapter extends RecyclerView.Adapter<Playlist_Grid_Adapter.ForYou_TrendHolder>{
    Context context;
    ArrayList<Playlists> list;
    private OnItemListener.IOnItemPlaylistClickListener listener;
    public Playlist_Grid_Adapter(Context context, ArrayList<Playlists> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ForYou_TrendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_grid_item,parent,false);
        return new ForYou_TrendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForYou_TrendHolder holder, int position) {
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
        }
    }
    public void setOnItemClickListener(OnItemListener.IOnItemPlaylistClickListener listener) {
        this.listener = listener;
    }

}
