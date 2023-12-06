package com.example.mydreammusicfinal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Artist;

import java.util.ArrayList;

public class Single_Adapter extends RecyclerView.Adapter<Single_Adapter.Single_holder>{
    Context context;
    ArrayList<Artist> list;
    OnItemListener.IOnItemSingleClickListener listener;
    public void setOnItemClickListener(OnItemListener.IOnItemSingleClickListener Listener){
        this.listener = Listener;
    }
    public Single_Adapter(Context context, ArrayList<Artist> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public Single_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.fragment_artist_item,parent,false);
        return new Single_holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Single_holder holder, int position) {
        Artist artist = list.get(position);
        if(artist == null){
            return;
        }
        holder.nameArtist.setText(artist.getNameArtist());
        GlideModule.loadSongImage(context,holder.imgAritst,artist.getImageURL());
        holder.rl_container_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(artist);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Single_holder extends RecyclerView.ViewHolder {
        ImageView imgAritst;
        TextView nameArtist;
        RelativeLayout rl_container_single;
        public Single_holder(@NonNull View itemView) {
            super(itemView);
            nameArtist = itemView.findViewById(R.id.tvName_Artist);
            imgAritst = itemView.findViewById(R.id.img_Artist);
            rl_container_single = itemView.findViewById(R.id.rl_container_single);
        }
    }
}
