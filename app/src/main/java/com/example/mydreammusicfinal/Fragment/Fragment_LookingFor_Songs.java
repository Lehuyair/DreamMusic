package com.example.mydreammusicfinal.Fragment;



import static android.app.PendingIntent.getActivity;

import static com.example.mydreammusicfinal.AUD.AUD.createNewSong;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;


public class Fragment_LookingFor_Songs extends Fragment implements OnItemListener.IOnItemSongsClickListerner{
    Child_Playlist_ItemAdapter adapterSongs;
    androidx.appcompat.widget.SearchView searchView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvSongs;
    String idUser, idPlaylist,namePlaylist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__looking_for_songs, container, false);;
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setStatusBar();
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            idUser = bundle.getString("idUser");
            idPlaylist= bundle.getString("idPlaylist");
            namePlaylist =  bundle.getString("namePlaylist");
        }
        setUI(view);
        setUISearchView();
        onClickListener();
        return view;
    }



    private void setUI(View view) {
        rvSongs = view.findViewById(R.id.rv_LookingForSongs);
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
        getAllSongs task1 = new getAllSongs(searchText, new CallBackListener.SongsCallBack() {
            @Override
            public void onCallbackSong(ArrayList<Songs> list) {
                updateRecyclerViewSongs(list);
            }
        });
        task1.execute();
    }


    private void updateRecyclerViewSongs(ArrayList<Songs> list) {
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        adapterSongs = new Child_Playlist_ItemAdapter(getContext(),list);
        adapterSongs.setIsAddSongs(true);
        adapterSongs.setOnItemSongListener(Fragment_LookingFor_Songs.this);
        rvSongs.setLayoutManager(linearLayoutManager);
        rvSongs.setAdapter(adapterSongs);
    }


    @Override
    public void onItemClick(Songs songs) {
        if (getActivity() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("newSongs", songs);
            bundle.putString("namePlaylist",namePlaylist);
            bundle.putString("idUser",idUser);
            bundle.putString("idPlaylist",idPlaylist);
            Fragment_YourPlaylist_Manager fragment1 = new Fragment_YourPlaylist_Manager();
            fragment1.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.view_pager, fragment1)
                    .addToBackStack(null)
                    .commit();
            if(idUser != null && idPlaylist != null){
                createNewSong(idUser,idPlaylist,songs.getSongKey());
            }
        }
    }

}