package com.example.mydreammusicfinal.Adapter;

import static com.example.mydreammusicfinal.Activity.MainActivity.imgLike;
import static com.example.mydreammusicfinal.Fragment.Fragment_Me.DataPlaylistRecentlyProcess;
import static com.example.mydreammusicfinal.Fragment.Fragment_Me.adapterRecently;
import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
import static com.example.mydreammusicfinal.MyApplication.checkUser;
import static com.example.mydreammusicfinal.MyApplication.clickStartService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.DataProcessing.LikeSongProcessing;
import com.example.mydreammusicfinal.Fragment.Fragment_Me;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Songs;

import java.util.ArrayList;

public class Child_Playlist_ItemAdapter extends RecyclerView.Adapter<Child_Playlist_ItemAdapter.Child_Chart_ItemHolder> {
    Context context;
    ArrayList<Songs> list;
    LikeSongProcessing likeSongProcessing;
    OnItemListener.IOnItemKeyArtistClickListenter listenter;
    OnItemListener.IOnItemSongsClickListerner listenerSong;
    Boolean isAdd = false;
    public void setListPlaylist(ArrayList<Songs> List){
        this.list = List;
        notifyDataSetChanged();
    }
    public  void setIsAdd(Boolean isAdd){
        this.isAdd = isAdd;
    }

    public Child_Playlist_ItemAdapter(Context context, ArrayList<Songs> list) {
        this.context = context;
        this.list = list;
    }
    public void setOnItemListener(OnItemListener.IOnItemKeyArtistClickListenter Listenter){
        this.listenter = Listenter;
    }
    public void setOnItemSongListener(OnItemListener.IOnItemSongsClickListerner ListenerSong){
        this.listenerSong = ListenerSong;
    }

    @NonNull
    @Override
    public Child_Chart_ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_playlist,parent,false);
            return  new Child_Chart_ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Child_Chart_ItemHolder holder, @SuppressLint("RecyclerView") int position) {
        Songs msong = list.get(position);
        holder.tvNameSong.setText(msong.getSongName());
        holder.tvNameArtist.setText(msong.getAritstName());
        GlideModule.loadSongImage(context,holder.imgSongs,msong.getImageURL());
        likeSongProcessing = new LikeSongProcessing();
        holder.tvNameArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenter.onItemClick(msong.getArtistKey());
            }
        });
        likeSongProcessing.checkLiked(msong, new LikeSongProcessing.OnDataReceivedListener() {
            @Override
            public void onDataReceived(boolean isLiked) {
                msong.setLiked(isLiked);
                if (msong.isLiked()) {
                    holder.imgLike.setImageResource(R.drawable.ic_heart_selected);
                } else {
                    holder.imgLike.setImageResource(R.drawable.ic_heart_none);
                }
            }
        });
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUser()){
                    if(!msong.isLiked()){
                        likeSongProcessing.addSongTofavorite(msong);
                        imgLike.setImageResource(R.drawable.ic_heart_selected);
                        holder.imgLike.setImageResource(R.drawable.ic_heart_selected);
                    }else{
                        likeSongProcessing.removeSongFromFavorite(msong);
                        holder.imgLike.setImageResource(R.drawable.ic_heart_none);
                        imgLike.setImageResource(R.drawable.ic_heart_none);
                        list.remove(position);
                        notifyDataSetChanged();
                        setListPlaylist(list);
                    }
                }else{
                    Toast.makeText(context, "Vui lòng đăng nhập để sử dụng tính năng này!", Toast.LENGTH_SHORT).show();
                }
            }
        });

            holder.rl_Container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isAdd){
                        positionSongPlaying = position;
                        MyService.setListSongPlaying(list);
                        clickStartService(context);
                    }else{
                        listenerSong.onItemClick(msong);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class     Child_Chart_ItemHolder extends RecyclerView.ViewHolder{
        ImageView imgSongs,imgLike;
        TextView tvIndex, tvNameSong, tvNameArtist;
        RelativeLayout rl_Container;
        public Child_Chart_ItemHolder(@NonNull View itemView) {
            super(itemView);
            imgSongs =itemView.findViewById(R.id.img_ChildPlaylist);
            tvNameArtist =itemView.findViewById(R.id.tvNameArtist_Playlist);
            tvNameSong =itemView.findViewById(R.id.tvNameSong_Playlist);
            imgLike = itemView.findViewById(R.id.img_LikePlaylist);
            rl_Container= itemView.findViewById(R.id.rl_Child_Playlist);
        }
    }



}
