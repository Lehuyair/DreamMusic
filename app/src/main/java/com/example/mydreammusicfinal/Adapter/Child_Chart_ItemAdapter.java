package com.example.mydreammusicfinal.Adapter;

import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
import static com.example.mydreammusicfinal.MyApplication.clickStartService;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreammusicfinal.Activity.MainActivity;
import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Songs;

import java.util.ArrayList;

public class Child_Chart_ItemAdapter extends RecyclerView.Adapter<Child_Chart_ItemAdapter.Child_Chart_ItemHolder>{
    Context context;
    ArrayList<Songs> list;
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
    public Child_Chart_ItemAdapter(Context context, ArrayList<Songs> list) {
        this.context = context;
        this.list = list;
    }

    public void updateData(ArrayList<Songs> newData) {
        this.list.clear();
        this.list.addAll(newData);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Child_Chart_ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_chart_item,parent,false);
            return  new Child_Chart_ItemHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Child_Chart_ItemHolder holder, @SuppressLint("RecyclerView") int position) {
        registerReceiver();
        Songs mSong = list.get(position);
        holder.tvNameSong.setText(mSong.getSongName());
        holder.tvIndex.setText((position+1)+"");
        holder.tvNameArtist.setText(mSong.getAritstName());
        GlideModule.loadSongImage(context,holder.imgSongs,mSong.getImageURL());
        holder.rl_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                positionSongPlaying = position;
                MyService.setListSongPlaying(list);
                clickStartService(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class     Child_Chart_ItemHolder extends RecyclerView.ViewHolder{
        ImageView imgSongs;
        TextView tvIndex, tvNameSong, tvNameArtist;
        RelativeLayout rl_Container;
        public Child_Chart_ItemHolder(@NonNull View itemView) {
            super(itemView);
            imgSongs =itemView.findViewById(R.id.img_ChildChart);
            tvIndex =itemView.findViewById(R.id.tv_indexChart);
            tvNameArtist =itemView.findViewById(R.id.tv_Name_Artist_Chart);
            tvNameSong =itemView.findViewById(R.id.tv_Name_Song_Chart);
            rl_Container = itemView.findViewById(R.id.rl_child_chart);
        }
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
