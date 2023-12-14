package com.example.mydreammusicfinal.Fragment;

import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
import static com.example.mydreammusicfinal.MyApplication.clickStartService;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mydreammusicfinal.Adapter.Child_Playlist_ItemAdapter;
import com.example.mydreammusicfinal.Adapter.Playlist_Vertical_Adapter;
import com.example.mydreammusicfinal.DataProcessing.LikeSongProcessing;
import com.example.mydreammusicfinal.DataProcessing.getSongsbyIDUsersAndIDPlaylist;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.Local_Data.Data_local_Manager;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.Songs;

import java.util.ArrayList;


public class Fragment_YourPlaylist_Manager extends Fragment implements View.OnClickListener, OnItemListener.IOnItemKeyArtistClickListenter  {
    ImageView imgAvatar, imgBack, imgPlay;
    ImageView imgSuffle;
    TextView tvNamePlaylist;
    RecyclerView  rvYourPlaylist;
    LinearLayout ln_addNewSong;
    LinearLayoutManager linearLayoutManager;
    Child_Playlist_ItemAdapter adapterSongs;
    String namePlaylist;
    String idUser;
    String idPlaylist;
    Songs newSongs;
    ArrayList<Songs> listPlaylist;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__your_playlist__manager, container, false);
        registerReceiver();
        setUI(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
                 idUser = bundle.getString("idUser");
                idPlaylist= bundle.getString("idPlaylist");
                namePlaylist =  bundle.getString("namePlaylist");
                newSongs = (Songs) bundle.getSerializable("newSongs");
                if(namePlaylist == null ){
                    idUser = new LikeSongProcessing().getIDUser();
                    Playlists playlists = (Playlists) bundle.getSerializable("albums");
                    idPlaylist = playlists.getKeyAlbum();
                    namePlaylist = playlists.getNameAlbum();
                }
        }
        if(namePlaylist != null){
            tvNamePlaylist.setText(namePlaylist);
        }
        getSongsbyIDUsersAndIDPlaylist task = new getSongsbyIDUsersAndIDPlaylist(idUser, idPlaylist, new CallBackListener.SongsCallBack() {
            @Override
            public void onCallbackSong(ArrayList<Songs> list) {
                adapterSongs = new Child_Playlist_ItemAdapter(getContext(), list);
                linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                adapterSongs.setOnItemListener(Fragment_YourPlaylist_Manager.this);
                rvYourPlaylist.setLayoutManager(linearLayoutManager);
                rvYourPlaylist.setAdapter(adapterSongs);
                listPlaylist = list;
            }
        });
        task.execute();
        return view;
    }
    private void setUI(View view) {
        imgAvatar = view .findViewById(R.id.imgAvatar_YourPlaylist);
        imgBack = view .findViewById( R.id.imgBack_YourPlaylist);
        imgPlay = view.findViewById(R.id.imgPlay_YourPlaylist);
        imgSuffle = view.findViewById(R.id.imgShuffle_YourPlaylist);
        tvNamePlaylist = view.findViewById(R.id.tvName_YourPlaylist);
        rvYourPlaylist = view.findViewById(R.id.rv__YourPlaylist);
        ln_addNewSong = view.findViewById(R.id.ln_addNewSong);
        imgBack.setOnClickListener(this);
        ln_addNewSong.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
        imgSuffle.setOnClickListener(this);
        checkShuffle();
    }
    private void checkShuffle() {
        if(Data_local_Manager.getShuffleTrack()){
            imgSuffle.setImageResource(R.drawable.icon_shuffle_selected);
        }else{
            imgSuffle.setImageResource(R.drawable.ic_shuffle_none);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imgBack_YourPlaylist){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        }else
        if(v.getId()== R.id.ln_addNewSong){
            if (getActivity() != null) {
                Fragment_LookingFor_Songs fragment1 = new Fragment_LookingFor_Songs();
                Bundle bundle = new Bundle();
                bundle.putString("idUser",idUser);
                bundle.putString("idPlaylist",idPlaylist);
                bundle.putString("namePlaylist",namePlaylist);
                fragment1.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.view_pager, fragment1)
                        .addToBackStack(null)
                        .commit();
            }
        }
        if(v.getId()==R.id.imgShuffle_YourPlaylist){
            if(Data_local_Manager.getShuffleTrack()== false){
                Data_local_Manager.setShuffleTrack(true);
                imgSuffle.setImageResource(R.drawable.icon_shuffle_selected);
            }else{
                Data_local_Manager.setShuffleTrack(false);
                imgSuffle.setImageResource(R.drawable.ic_shuffle_none);
            }
        }
            if(v.getId() == R.id.imgPlay_YourPlaylist){
                        showProgressDialog();
                        positionSongPlaying = 0;
                        MyService.setListSongPlaying(listPlaylist);
                        clickStartService(getContext());
            }
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