package com.example.mydreammusicfinal.Fragment;

import static com.example.mydreammusicfinal.AUD.AUD.removePlaylist;
import static com.example.mydreammusicfinal.MediaPlayerManager.MyService.positionSongPlaying;
import static com.example.mydreammusicfinal.MyApplication.clickStartService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreammusicfinal.Activity.LoginActivity;
import com.example.mydreammusicfinal.Activity.MainActivity;
import com.example.mydreammusicfinal.Activity.SplashActivity;
import com.example.mydreammusicfinal.Adapter.Playlist_Vertical_Adapter;
import com.example.mydreammusicfinal.Adapter.YourPlaylist_Adapter;
import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.DataProcessing.LikeSongProcessing;
import com.example.mydreammusicfinal.DataProcessing.getDataFavoriteSongsMusic;
import com.example.mydreammusicfinal.DataProcessing.getPlaylistbyIDUser;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.Local_Data.Data_local_Manager;
import com.example.mydreammusicfinal.MediaPlayerManager.MyService;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.Songs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Fragment_Me extends Fragment implements OnItemListener.IOnItemPlaylistClickListener {
    TextView tvName,tvLogout;
    static TextView tvAmountSong;
    Button btn_Login_fromMe;
    ImageView imgAvatar,imgPlay_LikedSong;
    static RelativeLayout rl_container_likedSong;
    RecyclerView rvRecently,rvYourPlaylist;
    static ArrayList<Playlists> listRecently;
    public static Playlist_Vertical_Adapter adapterRecently;
    YourPlaylist_Adapter adapterYourPlaylist;
    LinearLayout ln_addNewPlaylist;
    RelativeLayout rl_UnLogin;
    String IDUser;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me,container,false);
        initUI(view);
        checkUser();
        try {
            ShowUserInformation(view.getContext(),tvName,imgAvatar);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        OnClickListener();
        getDataFavoriteFromUser();
        updatePlaylistRecent();
        updateYourPlaylist();
        return view;
    }



    private void initUI(View view) {
        tvLogout = view.findViewById(R.id.tv_Logout);
        tvName = view.findViewById(R.id.tv_Username);
        imgAvatar = view.findViewById(R.id.img_Avatar);
        rvRecently = view.findViewById(R.id.rv_PlaylistRecent);
        rvYourPlaylist = view.findViewById(R.id.rv_YourPlaylist_Me);
        tvAmountSong = view.findViewById(R.id.tv_AmountSongs);
        rl_container_likedSong = view.findViewById(R.id.rl_yourFavorite);
        imgPlay_LikedSong = view.findViewById(R.id.img_playRecently);
        ln_addNewPlaylist = view.findViewById(R.id.ln_addYourPlaylist);
        rl_UnLogin = view.findViewById(R.id.rl_UnLogin);
        btn_Login_fromMe = view.findViewById(R.id.login_fromMe);
        LikeSongProcessing processing = new LikeSongProcessing();
        IDUser = processing.getIDUser();
    }
    private void OnClickListener() {
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.LogOut(getContext());
            }
        });
        ln_addNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Fragment_AddNewPlaylist fragmentAddNewPlaylist = new Fragment_AddNewPlaylist();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.view_pager, fragmentAddNewPlaylist)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }
    public void ShowUserInformation(Context context, TextView Name, ImageView avatar) throws FileNotFoundException {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
            return;
        }
        String name = firebaseUser.getDisplayName();
        Uri avatarUri = firebaseUser.getPhotoUrl();
        Name.setText(name);
        if(avatarUri != null){
            GlideModule.loadSongImage(context,avatar,avatarUri.toString());
        }
    }

    public static void DataPlaylistRecentlyProcess(){
        try{
            if(Data_local_Manager.getRecentArraylist() != null) {
                listRecently = Data_local_Manager.getRecentArraylist();
            }
        }catch (Exception e){
            listRecently = new ArrayList<>();
        }
    }



    public void updateYourPlaylist() {
        getPlaylistbyIDUser task = new getPlaylistbyIDUser(IDUser, new CallBackListener.PlaylistCallBack() {
            @Override
            public void onCallbackAlbum(ArrayList<Playlists> list) {
                adapterYourPlaylist = new YourPlaylist_Adapter(getContext(),list);
                adapterYourPlaylist.setIDUSER(IDUser);
                adapterYourPlaylist.setOnItemClickListener(Fragment_Me.this);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
                rvYourPlaylist.setLayoutManager(gridLayoutManager);
                rvYourPlaylist.setAdapter(adapterYourPlaylist);
            }
        });
        task.execute();
    }
    public void getDataFavoriteFromUser(){
        if(IDUser != null){
            getDataFavoriteSongsMusic task1 = new getDataFavoriteSongsMusic(IDUser, new CallBackListener.SongsCallBack() {
                @Override
                public void onCallbackSong(ArrayList<Songs> list) {
                    if(list.size()==0){
                        rl_container_likedSong.setVisibility(View.GONE);
                    }else{
                        rl_container_likedSong.setVisibility(View.VISIBLE);
                        tvAmountSong.setText(list.size()+ " Songs");
                        rl_container_likedSong.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Gson gson = new Gson();
                                String json = gson.toJson(list);
                                Bundle bundle = new Bundle();
                                bundle.putString("LikedPlaylist_JSON", json);
                                Fragment_Playlist_Screen fragment1 = new Fragment_Playlist_Screen();
                                fragment1.setArguments(bundle);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.view_pager, fragment1)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                        imgPlay_LikedSong.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                positionSongPlaying = 0;
                                MyService.setListSongPlaying(list);
                                clickStartService(getContext());
                            }
                        });
                    }
                }
            });
            task1.execute();
        }
    }
    public  void updatePlaylistRecent(){
        DataPlaylistRecentlyProcess();
        if(listRecently.size()!=0){
            adapterRecently = new Playlist_Vertical_Adapter(getContext(),listRecently);
            adapterRecently.setOnItemClickListener(Fragment_Me.this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
            rvRecently.setLayoutManager(gridLayoutManager);
            rvRecently.setAdapter(adapterRecently);
        }
    }
    @Override
    public void onItemClick(int IDAdapter,Playlists albums) {
        if(IDAdapter == 1){
            if (getActivity() != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("albums", albums);
                bundle.putString("idUser",IDUser);
                Fragment_Playlist_Screen fragment1 = new Fragment_Playlist_Screen();
                fragment1.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.view_pager, fragment1)
                        .addToBackStack(null)
                        .commit();
            }
        }
        if(IDAdapter == 2){
            if (getActivity() != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("albums", albums);
                Fragment_YourPlaylist_Manager fragment2 = new Fragment_YourPlaylist_Manager();
                fragment2.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.view_pager, fragment2)
                        .addToBackStack(null)
                        .commit();
            }
        }
        if(IDAdapter == 3){
            if(getActivity()!= null){
                removePlaylist(IDUser, albums.getKeyAlbum());
                Toast.makeText(getContext(), "Delete Sucessfully", Toast.LENGTH_SHORT).show();
                updateYourPlaylist();
            }
        }
    }
    public void checkUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            rl_UnLogin.setVisibility(View.VISIBLE);
            btn_Login_fromMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
        }else{
            rl_UnLogin.setVisibility(View.GONE);
        }

    }

}