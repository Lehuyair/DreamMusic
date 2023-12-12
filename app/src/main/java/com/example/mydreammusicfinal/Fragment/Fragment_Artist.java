package com.example.mydreammusicfinal.Fragment;

import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
import static com.example.mydreammusicfinal.MyApplication.clickStartService;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreammusicfinal.Adapter.Child_Playlist_ItemAdapter;
import com.example.mydreammusicfinal.Adapter.Playlist_Vertical_Adapter;
import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.DataProcessing.getDataPlaylistByUser;
import com.example.mydreammusicfinal.DataProcessing.getDataPlaylistSongsByUser;
import com.example.mydreammusicfinal.DataProcessing.getInformationArtistFromKey;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.Local_Data.Data_local_Manager;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Artist;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.Songs;

import java.util.ArrayList;

public class Fragment_Artist extends Fragment  implements OnItemListener.IOnItemPlaylistClickListener {
    ImageView imgAvatar, imgBack, imgPlay, imgSuffle;
    TextView tvNameArtist,tvAlbumMessage;
    RecyclerView rv_Songs, rv_albums;
    LinearLayoutManager linearLayoutManager;
    Child_Playlist_ItemAdapter adapterSongs;
    Playlist_Vertical_Adapter adapterAlbums ;
    String keyArtist;
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
        View view = inflater.inflate(R.layout.fragment_artist_screen, container,false);
        registerReceiver();
        setUI(view);
        checkShuffle();
        Bundle bundle = getArguments();
        if (bundle != null) {
            keyArtist =  bundle.getString("KeyArtist");
        }
        if(keyArtist != null){
            getInformationArtistFromKey taskArtist = new getInformationArtistFromKey(keyArtist, new CallBackListener.ArtistObjectCallBack() {
                @Override
                public void onCallBackObjectArtist(Artist artist) {
                    tvNameArtist.setText(artist.getNameArtist());
                    GlideModule.loadSongImage(getContext(),imgAvatar,artist.getImageURL());
                }
            });
            taskArtist.execute();
            getDataPlaylistSongsByUser taskSongs = new getDataPlaylistSongsByUser(keyArtist, new CallBackListener.SongsCallBack() {
                @Override
                public void onCallbackSong(ArrayList<Songs> list) {
                    linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                    adapterSongs = new Child_Playlist_ItemAdapter(getContext(),list);
                    rv_Songs.setLayoutManager(linearLayoutManager);
                    rv_Songs.setAdapter(adapterSongs);
                    imgPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showProgressDialog();
                            positionSongPlaying = 0;
                            MyService.setListSongPlaying(list);
                            clickStartService(getContext());
                        }
                    });
                }
            });
            taskSongs.execute();
            getDataPlaylistByUser taskAlbums = new getDataPlaylistByUser(keyArtist, new CallBackListener.PlaylistCallBack() {
                @Override
                public void onCallbackAlbum(ArrayList<Playlists> list) {
                    if(list!=null || list .size() != 0){
                        rv_albums.setVisibility(View.VISIBLE);
                        tvAlbumMessage.setVisibility(View.INVISIBLE);
                        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                        adapterAlbums = new Playlist_Vertical_Adapter(getContext(),list);
                        adapterAlbums.setOnItemClickListener(Fragment_Artist.this);
                        rv_albums.setLayoutManager(linearLayoutManager);
                        rv_albums.setAdapter(adapterAlbums);
                    }

                }
            });
            taskAlbums.execute();
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            });
            imgSuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Data_local_Manager.getShuffleTrack()== false){
                        Data_local_Manager.setShuffleTrack(true);
                        imgSuffle.setImageResource(R.drawable.icon_shuffle_selected);
                    }else{
                        Data_local_Manager.setShuffleTrack(false);
                        imgSuffle.setImageResource(R.drawable.ic_shuffle_none);
                    }
                }
            });
        }
        return view;
    }

    private void setUI(View view) {
        imgAvatar = view .findViewById(R.id.imgAvatarArtist);
        imgBack = view .findViewById( R.id.imgBackArtist);
        imgPlay = view.findViewById(R.id.imgPlay_Artist);
        imgSuffle = view.findViewById(R.id.imgShuffle_Artist);
        tvNameArtist = view.findViewById(R.id.tvNameArtist);
        rv_Songs = view.findViewById(R.id.rv_Artist_Songs);
        rv_albums = view.findViewById(R.id.rv_Artist_Albums);
        tvAlbumMessage = view.findViewById(R.id.tvAlbumMessage);
    }
    private void checkShuffle() {
        if(Data_local_Manager.getShuffleTrack()){
            imgSuffle.setImageResource(R.drawable.icon_shuffle_selected);
        }else{
            imgSuffle.setImageResource(R.drawable.ic_shuffle_none);
        }
    }
    @Override
    public void onItemClick(int IDAdapter,Playlists albums) {
        if(IDAdapter ==1 ){
            if (getActivity() != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("albums", albums);
                Fragment_Playlist_Screen fragment1 = new Fragment_Playlist_Screen();
                fragment1.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.view_pager, fragment1)
                        .addToBackStack(null)
                        .commit();
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
