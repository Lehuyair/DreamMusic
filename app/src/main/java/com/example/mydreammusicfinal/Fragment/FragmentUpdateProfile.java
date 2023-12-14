package com.example.mydreammusicfinal.Fragment;



import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;

import com.example.mydreammusicfinal.Activity.MainActivity;
import com.example.mydreammusicfinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentUpdateProfile extends BottomSheetDialogFragment {


    private static final int READ_EXTERNAL_STORAGE = 65;
    private static final int REQUEST_ADD_IMAGE_CODE = 3444;
    private EditText edtFullname, edtEmail;
    private Button btnUpdate;
    private CircleImageView imgAvatar;
    private ImageView  imgback;

    private TextView newtextemail;
    private Uri uriAvatar;
    FirebaseUser user;
    Fragment_ViewProfile fragmentViewProfile = new Fragment_ViewProfile();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        initUI(view);
        setUserInfo();
        setClickListener();

        if(getDialog() != null){
            getDialog().setOnShowListener(dialog -> {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);
                }
            });
        }


        return view;
    }

    private void initUI(View view) {

        imgAvatar = view.findViewById(R.id.imgavataupprofile);
        edtFullname = view.findViewById(R.id.edt_fullnameupde);
        edtEmail = view.findViewById(R.id.edt_emaildupde);
        btnUpdate = view.findViewById(R.id.btn_update);
        imgback = view.findViewById(R.id.backButtonupdate);
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    private void setUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            edtFullname.setText(user.getDisplayName());
            edtEmail.setText(user.getEmail());
            Glide.with(requireContext())
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.img_logo) // Ảnh giữ chỗ trong quá trình tải
                    .error(R.drawable.img_logo) // Ảnh để hiển thị khi có lỗi
                    .into(imgAvatar);
        }
    }

    private void setClickListener() {

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dismiss();
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_ADD_IMAGE_CODE);
                pickImage();


            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtFullname.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                updateProfile(name, email, uriAvatar);

                if (getActivity() != null) {
                    Fragment_ViewProfile fragment1 = new Fragment_ViewProfile();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.view_pager, fragment1)
                            .addToBackStack(null)
                            .commit();
                }

            }
        });
    }


    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_ADD_IMAGE_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_IMAGE_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            uriAvatar = data.getData();

            try {
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(uriAvatar);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgAvatar.setImageBitmap(bitmap);
                Glide.with(requireContext())
                        .load(uriAvatar)
                        .placeholder(R.drawable.img_logo)
                        .error(R.drawable.img_logo)
                        .into(imgAvatar);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateProfile(String name, String email, Uri image) {

        if (user == null) {
            return;
        }

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(image)
                .build();

        user.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Update Successfully", Toast.LENGTH_SHORT).show();


                        }
                    }
                });


    }
}











