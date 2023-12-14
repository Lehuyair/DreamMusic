package com.example.mydreammusicfinal.Fragment;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.mydreammusicfinal.Activity.MainActivity;
import com.example.mydreammusicfinal.Adapter.Child_Playlist_ItemAdapter;
import com.example.mydreammusicfinal.Adapter.Playlist_Vertical_Adapter;
import com.example.mydreammusicfinal.Adapter.Single_Adapter;
import com.example.mydreammusicfinal.DataProcessing.getAllArtist;
import com.example.mydreammusicfinal.DataProcessing.getAllPlaylist;
import com.example.mydreammusicfinal.DataProcessing.getAllSongs;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Artist;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.Songs;


import java.util.ArrayList;


public class Fragment_LookingFor extends Fragment implements OnItemListener.IOnItemSingleClickListener, OnItemListener.IOnItemPlaylistClickListener {
    Child_Playlist_ItemAdapter adapterSongs;
    Single_Adapter adapterSingle;
    androidx.appcompat.widget.SearchView searchView;
    LinearLayoutManager linearLayoutManager;
    Playlist_Vertical_Adapter adapterPlaylist;
    RecyclerView rvSongs, rvArtist, rvPlaylist;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__looking_for, container, false);;
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setStatusBar();
        }
        setUI(view);
        setUISearchView();
        onClickListener();
        return view;
    }



    private void setUI(View view) {
        rvSongs = view.findViewById(R.id.rv_LookingForSongs);
        rvArtist = view.findViewById(R.id.rv_LookingForartist);
        rvPlaylist = view.findViewById(R.id.rv_LookingForPlaylist);
        searchView =view.findViewById(R.id.searchView);
    }
    private void setUISearchView() {
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        searchEditText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.white));
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        searchView.setIconified(false);
        searchView.post(() -> {
            searchView.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
        });
    }
    private void onClickListener() {
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                queryToFirebase(newText);
                return true;
            }
        });
    }
    private void queryToFirebase(String searchText) {
        showProgressDialog();
        getAllSongs task1 = new getAllSongs(searchText, new CallBackListener.SongsCallBack() {
                @Override
                public void onCallbackSong(ArrayList<Songs> list) {
                    updateRecyclerViewSongs(list);
                    dismissProgressDialog();
                }
            });
        task1.execute();
            getAllPlaylist task2 = new getAllPlaylist(searchText, new CallBackListener.PlaylistCallBack() {
                @Override
                public void onCallbackAlbum(ArrayList<Playlists> list) {
                    updateRecyclerViewPlaylist(list);
                }
            });
        task2.execute();
        getAllArtist task3 = new getAllArtist(searchText, new CallBackListener.ArtistCallBack() {
            @Override
            public void onCallBackArtist(ArrayList<Artist> list) {
                updateRecyclerViewArtist(list);
            }
        });
        task3.execute();
    }
        private void updateRecyclerViewSongs(ArrayList<Songs> list) {
            linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            adapterSongs = new Child_Playlist_ItemAdapter(getContext(),list);
            rvSongs.setLayoutManager(linearLayoutManager);
            rvSongs.setAdapter(adapterSongs);
    }
    private void updateRecyclerViewArtist(ArrayList<Artist> list) {
            linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            adapterSingle = new Single_Adapter(getContext(),list);
            adapterSingle.setOnItemClickListener(Fragment_LookingFor.this);
            rvArtist.setLayoutManager(linearLayoutManager);
            rvArtist.setAdapter(adapterSingle);
    }
    private void updateRecyclerViewPlaylist(ArrayList<Playlists> list) {
            linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            adapterPlaylist = new Playlist_Vertical_Adapter (getContext(),list);
            adapterPlaylist.setOnItemClickListener(Fragment_LookingFor.this);
            rvPlaylist.setLayoutManager(linearLayoutManager);
            rvPlaylist.setAdapter(adapterPlaylist);
    }




    @Override
    public void onItemClick(Artist Artist) {
        if (getActivity() != null) {
            Bundle bundle = new Bundle();
            bundle.putString("KeyArtist", Artist.getKeyArtist());
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
    public void onItemClick(int IDAdapter, Playlists albums) {
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
    private void showProgressDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang tải...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}