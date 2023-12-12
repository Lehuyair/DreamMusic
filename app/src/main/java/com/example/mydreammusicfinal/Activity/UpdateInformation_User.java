package com.example.mydreammusicfinal.Activity;

import static com.example.mydreammusicfinal.Constance.Constance.KEY_NAME_USER_REGISTRY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydreammusicfinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

public class UpdateInformation_User extends AppCompatActivity {
    private static final int REQUEST_ADD_IMAGE_CODE = 3444;
    ImageView imgUpdate,imgBack;
    TextView tvName,tvSkip;
    public Uri uriAvatar;
    Button btnUpdate;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information_user);
        setUI();
        String nameUser = getIntent().getStringExtra(KEY_NAME_USER_REGISTRY);
        tvName.setText(nameUser);
        imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_ADD_IMAGE_CODE);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tvName.getText().toString().trim();
                if(uriAvatar == null){
                    Toast.makeText(UpdateInformation_User.this, "Vui lòng chon hình ảnh", Toast.LENGTH_SHORT).show();
                }else{
                    updateProfile(name,uriAvatar);
                }
            }
        });
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tvName.getText().toString().trim();
                updateProfile(name,null);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateInformation_User.this, SignUpActivity.class));
            }
        });
    }

    private void setUI() {
        btnUpdate = findViewById(R.id.btnUpdate);
        tvSkip = findViewById(R.id.tvSkip);
        imgBack = findViewById(R.id.imgBack);
        imgUpdate = findViewById(R.id.imgUpdateUser);
        tvName = findViewById(R.id.tvName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ADD_IMAGE_CODE && resultCode == RESULT_OK ){
             uriAvatar = data.getData();
            try {
                InputStream ip =getContentResolver().openInputStream(uriAvatar);
                Bitmap bitmap = BitmapFactory.decodeStream(ip);
                imgUpdate.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void updateProfile(String name,Uri image){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(image)
                .build();
        user.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UpdateInformation_User.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateInformation_User.this, MainActivity.class));
                        }
                    }
                });
    }
}