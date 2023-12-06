package com.example.mydreammusicfinal.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreammusicfinal.DataProcessing.getDataPlaylistByKey;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.PhotoAdvertisment;

import java.util.ArrayList;

public class ViewPagerAdvertismentAdapter extends RecyclerView.Adapter<ViewPagerAdvertismentAdapter.PhotoViewHolder>{
    private ArrayList<PhotoAdvertisment> list;
    private OnItemListener.IOnItemPlaylistClickListener listener;
    public ViewPagerAdvertismentAdapter(ArrayList<PhotoAdvertisment> list) {
        this.list = list;
    }
    public void setOnItemClickListener(OnItemListener.IOnItemPlaylistClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_advertisment_item,parent,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoAdvertisment photoAdvertisment = list.get(position);
        if(photoAdvertisment == null){
            return;
        }
        holder.imgPhoto .setImageResource(photoAdvertisment.getPhotoResource());
        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillerDataByKeyAlbum(photoAdvertisment.getKeyPlaylist());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list !=null){
            return list.size();
        }
        return 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPhoto;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo_advertisement);
        }
    }

    public void fillerDataByKeyAlbum(String keyPlaylist){
        getDataPlaylistByKey task = new getDataPlaylistByKey(keyPlaylist, new CallBackListener.PlaylistCallBack(){
            @Override
            public void onCallbackAlbum(ArrayList<Playlists> list) {
                if (listener != null) {
                    listener.onItemClick(1,list.get(0));
                }
            }
        });
        task.execute();
    }
}
