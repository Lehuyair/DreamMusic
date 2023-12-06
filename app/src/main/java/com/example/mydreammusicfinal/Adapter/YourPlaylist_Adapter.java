package com.example.mydreammusicfinal.Adapter;

import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
import static com.example.mydreammusicfinal.MyApplication.clickStartService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreammusicfinal.DataProcessing.getDataPlaylistSongsByKeyPlaylist;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.Songs;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class YourPlaylist_Adapter extends RecyclerView.Adapter<YourPlaylist_Adapter.YourPlaylist_Holder> {
    Context context;
    ArrayList<Playlists> list;
    String idUser;
    private OnItemListener.IOnItemPlaylistClickListener listener;
    public YourPlaylist_Adapter(Context context, ArrayList<Playlists> list) {
        this.context = context;
        this.list = list;
    }
    public void setOnItemClickListener(OnItemListener.IOnItemPlaylistClickListener Listener) {
        this.listener = Listener;
    }

    public void setIDUSER(String IDUser){
        this.idUser = IDUser;
    }
    @NonNull
    @Override
    public YourPlaylist_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_yourplaylist_item,parent,false);
        return new YourPlaylist_Holder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull YourPlaylist_Holder holder, int position) {
        final int[] amount = {0};
        Playlists albums = list.get(position);
        if(albums == null){return;}
        holder.tvNameAlbums.setText(albums.getNameAlbum());
            holder.imgAlbums.setImageResource(R.drawable.img_yourplaylist);
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(2,albums);
            }
        });
        getDataPlaylistSongsByKeyPlaylist task = new getDataPlaylistSongsByKeyPlaylist("users/"+idUser+"/playlists",albums.getKeyAlbum(), new CallBackListener.SongsCallBack() {
            @Override
            public void onCallbackSong(ArrayList<Songs> list) {
                amount[0] = list.size();
                holder.tvAmount.setText(amount[0] + " Songs");
            }
        });
        task.execute();

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_moreplaylist);
                TextView namePlaylist = bottomSheetDialog.findViewById(R.id.nameMorePlaylist);
                ImageView imagePlaylist = bottomSheetDialog.findViewById(R.id.imgMorePlaylist);
                LinearLayout lnPlay = bottomSheetDialog.findViewById(R.id.ln_play);
                LinearLayout lnRemove = bottomSheetDialog.findViewById(R.id.ln_Remove);
                namePlaylist.setText(albums.getNameAlbum());
                imagePlaylist.setImageResource(R.drawable.img_yourplaylist);
                bottomSheetDialog.show();
                lnPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDataPlaylistSongsByKeyPlaylist task = new getDataPlaylistSongsByKeyPlaylist("users/"+idUser+"/playlists",albums.getKeyAlbum(), new CallBackListener.SongsCallBack() {
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
                lnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(3,albums);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class YourPlaylist_Holder extends RecyclerView.ViewHolder {
        ImageView imgAlbums,imgMore;
        TextView tvNameAlbums,tvAmount;
        public YourPlaylist_Holder(@NonNull View itemView) {
            super(itemView);
            imgAlbums = itemView.findViewById(R.id.img_yourPlaylist_item);
            imgMore = itemView.findViewById(R.id.img_moreYourPlaylist);
            tvNameAlbums = itemView.findViewById(R.id.tv_NameYourPlaylistItem);
            tvAmount = itemView.findViewById(R.id.tv_AmountSongs_item);
        }
    }
}
