package com.example.mydreammusicfinal.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.mydreammusicfinal.Activity.MainActivity;
import com.example.mydreammusicfinal.DataProcessing.GlideModule;
import com.example.mydreammusicfinal.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_ViewProfile extends Fragment {

    private CircleImageView imgAvatarProfile;
    private TextView txtname, txtEmaill;
    ImageView imgback;
    Uri uri;
    private LinearLayout linearOut, linearChangePassword, layoutmyprofile;

    public Fragment_ViewProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__view_profile, container, false);
        initUI(view);
        eventClickListener();
        updateProfileImage();

        try {
            ShowUserInformation(view.getContext(), txtname, txtEmaill, imgAvatarProfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return view;
    }

    private void initUI(View view) {
        imgback = view.findViewById(R.id.backButton);
        imgAvatarProfile = view.findViewById(R.id.imgavataprofile);
        txtname = view.findViewById(R.id.txtnameprofile);
        txtEmaill = view.findViewById(R.id.txtEmaillprofile);
        linearOut = view.findViewById(R.id.layoutOut);
        linearChangePassword = view.findViewById(R.id.ChangePassword);
        layoutmyprofile = view.findViewById(R.id.layoutmyprofile);
    }

    public  void ShowUserInformation(Context context, TextView emailTextView, TextView nameTextView, ImageView avatarImageView) throws FileNotFoundException {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        String email = firebaseUser.getEmail();
        String name = firebaseUser.getDisplayName();
        uri = firebaseUser.getPhotoUrl();

        if (emailTextView != null) {
            emailTextView.setText(email);
        }

        if (nameTextView != null) {
            nameTextView.setText(name);
        }

        if (uri != null && avatarImageView != null) {
            GlideModule.loadSongImage(context, avatarImageView, uri.toString());
        }
    }

    private void eventClickListener() {
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Fragment_Me fragment1 = new Fragment_Me();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.view_pager, fragment1)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        layoutmyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateProfileBottomSheet();
            }
        });



        linearOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.LogOut(getContext());
            }
        });



    }



    public void updateProfileImage() {
        if (imgAvatarProfile != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Glide.with(requireContext())
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.img_logo) // Drawable mặc định nếu không có ảnh
                        .error(R.drawable.img_logo) // Drawable nếu có lỗi khi tải ảnh
                        .into(imgAvatarProfile);
            }
        }
    }
    private void showUpdateProfileBottomSheet() {
        FragmentUpdateProfile bottomSheetDialog = new FragmentUpdateProfile();
        bottomSheetDialog.show(getChildFragmentManager(), bottomSheetDialog.getTag());
    }


}
