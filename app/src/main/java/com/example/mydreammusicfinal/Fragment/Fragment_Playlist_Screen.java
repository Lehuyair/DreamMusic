package com.example.mydreammusicfinal.Fragment;
import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
import static com.example.mydreammusicfinal.MyApplication.clickStartService;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.mydreammusicfinal.Activity.MainActivity;
import com.example.mydreammusicfinal.Adapter.Child_Playlist_ItemAdapter;
import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.DataProcessing.getDataPlaylistSongsByKeyPlaylist;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.Local_Data.Data_local_Manager;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.Songs;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Fragment_Playlist_Screen extends Fragment implements OnItemListener.IOnItemKeyArtistClickListenter, View.OnClickListener {
    ImageView imgAvatar, imgBack, imgPlay, imgCover,imgShuffle;
    TextView tvNamePlaylist;
    Child_Playlist_ItemAdapter trendItemAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RelativeLayout rl_container_playlist;
     ArrayList<Songs> listPlaylist;
    String JsonPlaylist;
    Playlists albums;
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_screen,container,false);
        registerReceiver();
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setStatusBar();
        }
        setUI(view);
        checkedCheckedShuffle();
        Bundle bundle = getArguments();
        if (bundle != null) {
            albums = (Playlists) bundle.getSerializable("albums"); // Lấy đối tượng Song từ Bundle
            JsonPlaylist = (String) bundle.getString("LikedPlaylist_JSON");
        }
        if(albums != null) {
            tvNamePlaylist.setText(albums.getNameAlbum());
            rl_container_playlist.setBackgroundColor(Color.parseColor(albums.getColorCode()));
            GlideModule.loadSongImage(getContext(), imgAvatar, albums.getImageURL());
            GlideModule.loadSongImage(getContext(), imgCover, albums.getImageBGURL());
            String yourPlaylistName = albums.getKeyAlbum();
            recyclerView.setBackgroundColor(Color.parseColor(albums.getColorCode()));
            getDataPlaylistSongsByKeyPlaylist task = new getDataPlaylistSongsByKeyPlaylist("playlists",yourPlaylistName, new CallBackListener.SongsCallBack() {
                @Override
                public void onCallbackSong(ArrayList<Songs> list) {
                    trendItemAdapter = new Child_Playlist_ItemAdapter(getContext(), list);
                    trendItemAdapter.setOnItemListener(Fragment_Playlist_Screen.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(trendItemAdapter);
                    listPlaylist = list;
                }
            });
            task.execute();
        }
        if(JsonPlaylist != null ){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Songs>>() {}.getType();
            listPlaylist = gson.fromJson(JsonPlaylist, type);
            tvNamePlaylist.setText("Your Liked Songs");
            imgAvatar.setImageResource(R.drawable.img_redheart);
            trendItemAdapter = new Child_Playlist_ItemAdapter(getContext(), listPlaylist);
            trendItemAdapter.setIsFavorite(true);
            trendItemAdapter.setOnItemListener(Fragment_Playlist_Screen.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(trendItemAdapter);
        }
        imgShuffle.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        return view;
    }

    private void checkedCheckedShuffle() {
        if(Data_local_Manager.getShuffleTrack()){
            imgShuffle.setImageResource(R.drawable.icon_shuffle_selected);
        }else{
            imgShuffle.setImageResource(R.drawable.ic_shuffle_none);
        }
    }

    private void setUI(View view) {
        recyclerView = view.findViewById(R.id.rv_Playlist);
        imgAvatar= view.findViewById(R.id.imgAvatarPlaylist);
        imgBack = view.findViewById(R.id.imgBackPlaylist);
        imgPlay = view.findViewById(R.id.imgPlay_Playlist);
        rl_container_playlist = view.findViewById(R.id.rl_container_playlist);
        imgCover = view.findViewById(R.id.imgCoverImage);
        tvNamePlaylist = view.findViewById(R.id.tvNamePlaylist);
        imgShuffle = view.findViewById(R.id.imgShuffle_Playlist);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void onItemClick(String keyArtist) {
        if (getActivity() != null) {
            Bundle bundle = new Bundle();
            bundle.putString("KeyArtist", keyArtist);
            Fragment_Artist fragment1 = new Fragment_Artist();
            fragment1.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.view_pager, fragment1)
                    .addToBackStack(null)
                    .commit();
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imgBackPlaylist){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        }
        if(v.getId() ==R.id.imgPlay_Playlist){
            showProgressDialog();
            positionSongPlaying = 0;
            MyService.setListSongPlaying(listPlaylist);
            clickStartService(getContext());
        }
        if(v.getId() == R.id.imgShuffle_Playlist){
            if(Data_local_Manager.getShuffleTrack()== false){
                Data_local_Manager.setShuffleTrack(true);
                imgShuffle.setImageResource(R.drawable.icon_shuffle_selected);
            }else{
                Data_local_Manager.setShuffleTrack(false);
                imgShuffle.setImageResource(R.drawable.ic_shuffle_none);
            }
        }
    }
    private void showProgressDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang tải...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void registerReceiver() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(MessageReciever, new IntentFilter("DismissDialog"));
    }
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}