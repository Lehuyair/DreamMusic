package com.example.mydreammusicfinal.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.mydreammusicfinal.Activity.MainActivity;
import com.example.mydreammusicfinal.Adapter.Playlist_Grid_Adapter;
import com.example.mydreammusicfinal.Adapter.Playlist_Horizento_Adapter;
import com.example.mydreammusicfinal.DataProcessing.getDataPlaylistByTag;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Playlists;

import java.util.ArrayList;

public class Fragment_Discovery extends Fragment implements OnItemListener.IOnItemPlaylistClickListener {
    RecyclerView rcvPickForYou, rvNewRelease, rvVMusic, rvElectronic;
    Playlist_Grid_Adapter adapterPickForYou;
    Playlist_Horizento_Adapter adapterHorizento;
    RelativeLayout rl_LookingFor;
    ProgressDialog progressDialog;
    private OnItemListener.IOnItemPlaylistClickListener listener;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment__discovery, container, false);
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setStatusBar();
        }
        setUI(view);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getDataPlaylistByTag task = new getDataPlaylistByTag(getContext(),"Pick For You", new CallBackListener.PlaylistCallBack() {
            @Override
            public void onCallbackAlbum(ArrayList<Playlists> list) {
                adapterPickForYou = new Playlist_Grid_Adapter(getContext(),list);
                adapterPickForYou.setOnItemClickListener(Fragment_Discovery.this);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                rcvPickForYou.setLayoutManager(gridLayoutManager);
                rcvPickForYou.setAdapter(adapterPickForYou);
            }
        });
        task.execute();
        fillPlaylistHorizento("Trend",rvNewRelease);
        fillPlaylistHorizento("V-Music",rvVMusic);
        fillPlaylistHorizento("Electronic",rvElectronic);
        lookingForOnclickListenter();
        return view;
    }

    private void lookingForOnclickListenter() {
        rl_LookingFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Fragment_LookingFor fragment1 = new Fragment_LookingFor();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.view_pager, fragment1)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

    }

    private void setUI(View view) {
        rcvPickForYou =view.findViewById(R.id.rcvPickForYou);
        rl_LookingFor = view.findViewById(R.id.rl_LookingFor);
        rvNewRelease = view.findViewById(R.id.rv_NewRelease);
        rvVMusic = view.findViewById(R.id.rv_VMusic);
        rvElectronic = view.findViewById(R.id.rv_Electronic);
        progressDialog = new ProgressDialog(getContext());
    }

    public void fillPlaylistHorizento(String tag,RecyclerView recyclerView){
        getDataPlaylistByTag task = new getDataPlaylistByTag(getContext(),tag, new CallBackListener.PlaylistCallBack() {
            @Override
            public void onCallbackAlbum(ArrayList<Playlists> list) {
                adapterHorizento = new Playlist_Horizento_Adapter(getContext(),list);
                adapterHorizento.setOnItemClickListener(Fragment_Discovery.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapterHorizento);
                progressDialog.dismiss();
            }
        });
        task.execute();
    }


    @Override
    public void onItemClick(int IDAdapter,Playlists albums) {
        if(IDAdapter == 1){
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
}