package com.example.mydreammusicfinal.Fragment;

import static com.example.mydreammusicfinal.AUD.AUD.createNewPlaylist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mydreammusicfinal.DataProcessing.LikeSongProcessing;
import com.example.mydreammusicfinal.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;
import java.util.UUID;

public class Fragment_AddNewPlaylist extends Fragment implements View.OnClickListener {
    EditText edtNamePlaylist;
    Button btnCancle, btnCreate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_playlist,container,false);
        setUI(view);
        return view;
    }
    private void setUI(View view) {
        edtNamePlaylist = view.findViewById(R.id.edtNewPlaylist);
        btnCancle = view.findViewById(R.id.btnCancleNewPlaylist);
        btnCreate = view.findViewById(R.id.btnCreateNewPlaylist);
        btnCancle.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnCancleNewPlaylist){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        }else
        if(v.getId() == R.id.btnCreateNewPlaylist){
            String namePlayList = edtNamePlaylist.getText().toString().trim();
            if(namePlayList.isEmpty()){
                Toast.makeText(getContext(), "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
            }else{
                LikeSongProcessing processing = new LikeSongProcessing();
                String idUser = processing.getIDUser();
                String idPlaylist = getUniqueIDPlaylist();
                Log.e("Taggggggggg", "value ID: "+idUser);
                createNewPlaylist(idUser,idPlaylist,namePlayList);
                if (getActivity() != null) {
                    Fragment_YourPlaylist_Manager fragmentYourPlaylist = new Fragment_YourPlaylist_Manager();
                    Bundle bundle = new Bundle();
                    bundle.putString("idUser",idUser);
                    bundle.putString("idPlaylist",idPlaylist);
                    bundle.putString("namePlaylist",namePlayList);
                    fragmentYourPlaylist.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.view_pager, fragmentYourPlaylist)
                            .addToBackStack(null)
                            .commit();
                }
            }
        }
    }

    public String getUniqueIDPlaylist() {
        UUID uniqueKey = UUID.randomUUID();
        return uniqueKey.toString();
    }
}