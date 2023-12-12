package com.example.mydreammusicfinal.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.example.mydreammusicfinal.Activity.MainActivity;
import com.example.mydreammusicfinal.Adapter.Playlist_Grid_Adapter;
import com.example.mydreammusicfinal.Adapter.ForYou_Chart_Adapter;
import com.example.mydreammusicfinal.Adapter.Playlist_Horizento_Adapter;
import com.example.mydreammusicfinal.Adapter.ViewPagerAdvertismentAdapter;
import com.example.mydreammusicfinal.DataProcessing.getDataPlaylistByTag;
import com.example.mydreammusicfinal.Interface.CallBackListener;
import com.example.mydreammusicfinal.Interface.OnItemListener;
import com.example.mydreammusicfinal.R;
import com.example.mydreammusicfinal.model.Playlists;
import com.example.mydreammusicfinal.model.PhotoAdvertisment;
import com.example.mydreammusicfinal.transformer.DepthPageTransformer;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class Fragment_ForYou extends Fragment implements OnItemListener.IOnItemPlaylistClickListener {
    private ViewPager2 viewPagerAdvertisement;
    public static ArrayList<String> listChart;
    private ViewPagerAdvertismentAdapter adapterAdvertisement;
    private Playlist_Horizento_Adapter adapterTrend;
    Playlist_Grid_Adapter pickForYou_adapter;
    ProgressDialog progressDialog;

    private RecyclerView rvChart;
    private RecyclerView rvPop, rvDreamMusicChoice;
    private CircleIndicator3 circleIndicator;
    ArrayList<PhotoAdvertisment> listAdvertisement;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPagerAdvertisement.getCurrentItem()==  listAdvertisement.size()-1){
                viewPagerAdvertisement.setCurrentItem(0);
            }else {
                viewPagerAdvertisement.setCurrentItem(viewPagerAdvertisement.getCurrentItem()+1);
            }
        }
    };



    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foryou,container,false);
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setStatusBar();
        }
        getListChart();
        setUI(view);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        handleAdivertismentLayout(view);
        handleTrendData();
        handleDreamMusicChoiceData();
        handleChartData();
        return view;
    }



    private void setUI(View view) {
        progressDialog = new ProgressDialog(getContext());
        viewPagerAdvertisement = view.findViewById(R.id.vg_Advertisment);
        circleIndicator = view.findViewById(R.id.circle_indicator);
        adapterAdvertisement = new ViewPagerAdvertismentAdapter(getListPhoto());
        rvPop = view.findViewById(R.id.rv_Pop);
        rvDreamMusicChoice =view.findViewById(R.id.rv_DreamMusicChoice);
        rvChart = view.findViewById(R.id.rv_Chart);

    }

    private ArrayList<PhotoAdvertisment> getListPhoto(){
        listAdvertisement = new ArrayList<>();
        listAdvertisement.add(new PhotoAdvertisment("HayTraoChoAnh",R.drawable.advertisment_sontung));
        listAdvertisement.add(new PhotoAdvertisment("AiCungPhaiBatDauTuDauDo",R.drawable.advertisment_hieuthuhai));
        listAdvertisement.add(new PhotoAdvertisment("DanhDoi",R.drawable.advertisment_obito));
        listAdvertisement.add(new PhotoAdvertisment("30",R.drawable.advertisment_adele));
        return listAdvertisement;
    }
    public void  getListChart(){
        listChart = new ArrayList<>();
        listChart.add("Top 10 Viet Nam");
        listChart.add("Top 10 Rap Viet");
        listChart.add("Top 10 US-  UK");
        listChart.add("Top 10 Korean");
    }

    private void handleAdivertismentLayout(View view) {
        adapterAdvertisement.setOnItemClickListener(Fragment_ForYou.this);
        viewPagerAdvertisement.setAdapter(adapterAdvertisement);
        circleIndicator.setViewPager(viewPagerAdvertisement);
        viewPagerAdvertisement.setTranslationX(-1 * view.getWidth() * viewPagerAdvertisement.getCurrentItem());
        viewPagerAdvertisement.setPageTransformer(new DepthPageTransformer());
        viewPagerAdvertisement.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,3500);
            }
        });
    }

    private void handleDreamMusicChoiceData() {
        getDataPlaylistByTag taskDream = new getDataPlaylistByTag(getContext(),"Dream Music's Choice", new CallBackListener.PlaylistCallBack() {
            @Override
            public void onCallbackAlbum(ArrayList<Playlists> list) {
                pickForYou_adapter = new Playlist_Grid_Adapter(getContext(),list);
                pickForYou_adapter.setOnItemClickListener(Fragment_ForYou.this);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                rvDreamMusicChoice.setLayoutManager(gridLayoutManager);
                rvDreamMusicChoice.setAdapter(pickForYou_adapter);
                progressDialog.dismiss();
            }
        });
        taskDream.execute();
    }

    private void handleTrendData() {
        getDataPlaylistByTag task = new getDataPlaylistByTag(getContext(),"Pop", new CallBackListener.PlaylistCallBack() {
            @Override
            public void onCallbackAlbum(ArrayList<Playlists> list) {
                adapterTrend = new Playlist_Horizento_Adapter(getContext(),list);
                adapterTrend.setOnItemClickListener(Fragment_ForYou.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                rvPop.setLayoutManager(linearLayoutManager);
                rvPop.setAdapter(adapterTrend);
            }
        });
        task.execute();
    }
    private void handleChartData() {
        rvChart.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        ForYou_Chart_Adapter adapterChart = new ForYou_Chart_Adapter(getContext(),listChart);
        rvChart.setAdapter(adapterChart);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();

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
