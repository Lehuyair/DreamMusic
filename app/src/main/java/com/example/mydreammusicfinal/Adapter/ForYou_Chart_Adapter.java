package com.example.mydreammusicfinal.Adapter;

import static com.example.mydreammusicfinal.Constance.Constance.ACTION_START;
import static com.example.mydreammusicfinal.Constance.Constance.KEY_ACTION_STATUS_SERVICE;
import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreammusicfinal.DataProcessing.getDataChartSongs;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Songs;

import java.util.ArrayList;

public class ForYou_Chart_Adapter extends RecyclerView.Adapter<ForYou_Chart_Adapter.ForYou_ChartHolder>{
    Context context;
    ArrayList<String> list;
    Child_Chart_ItemAdapter adapter;
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
    public ForYou_Chart_Adapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ForYou_ChartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chart_list_item,parent,false);
        return new ForYou_ChartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForYou_ChartHolder holder, int position) {
        registerReceiver();
        String name = list.get(position);
        holder.tv_NameChart.setText(name);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        holder.rv_ListChart.setLayoutManager(linearLayoutManager);
        if(position==0){
            fillterByGenre("VN",holder.rv_ListChart,holder.img_playChart);
        }
        if(position==1){
            fillterByGenre("RV",holder.rv_ListChart,holder.img_playChart);
        }
        if(position==2){
            fillterByGenre("US",holder.rv_ListChart,holder.img_playChart);
        }
        if(position==3){
            fillterByGenre("KR",holder.rv_ListChart,holder.img_playChart);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ForYou_ChartHolder extends RecyclerView.ViewHolder {
        TextView tv_NameChart;
        ImageView img_playChart;
        RecyclerView rv_ListChart;
        public ForYou_ChartHolder(@NonNull View itemView) {
            super(itemView);
            tv_NameChart = itemView.findViewById(R.id.tv_NameOfTop);
            rv_ListChart = itemView.findViewById(R.id.rv_listChart);
            img_playChart = itemView.findViewById(R.id.img_PlayChart);
        }
    }

private void clickStartService() {
    Intent intent = new Intent(context, MyService.class);
    intent.putExtra(KEY_ACTION_STATUS_SERVICE,ACTION_START);
    context.startService(intent);
}
public void fillterByGenre(String genre,RecyclerView rv, ImageView img){
    getDataChartSongs task = new getDataChartSongs(genre, new CallBackListener.SongsCallBack() {
        @Override
        public void onCallbackSong(ArrayList<Songs> list) {
            adapter = new Child_Chart_ItemAdapter(context,list);
            rv.setAdapter(adapter);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    positionSongPlaying = 0;
                    MyService.setListSongPlaying(list);
                    clickStartService();
                }
            });
        }
    });
    task.execute();

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
