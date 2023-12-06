package com.example.mydreammusicfinal.Activity;

import static com.example.mydreammusicfinal.Constance.Constance.KEY_NAME_USER_REGISTRY;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mydreammusicfinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    ProgressBar progressBar;
    EditText edtName, edtID, edtPW, edtConfrimPW;
    Button btnAdd, btnCancle;
    String TAG = "SignUpActivity.class";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        InitUI();
        setClickListener();
    }


    private void InitUI() {
        edtID = findViewById(R.id.edtIDAdd);
        edtName = findViewById(R.id.edtAddName);
        edtPW = findViewById(R.id.edtPW);
        edtConfrimPW = findViewById(R.id.edtconfirmPW);
        btnAdd = findViewById(R.id.btnSaveAdd);
        progressBar = findViewById(R.id.progress_SignUp);
        btnCancle = findViewById(R.id.btnCancleAdd);
    }
    private void setClickListener() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatusBar();
                progressBar.setVisibility(View.VISIBLE);
                String email = edtID.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                String password = edtPW.getText().toString().trim();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(SignUpActivity.this, UpdateInformation_User.class);
                                        intent.putExtra(KEY_NAME_USER_REGISTRY,name);
                                        startActivity(intent);
                                        finishAffinity();
                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.w(TAG, "create User With Email : failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }


        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
    private void setStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }


}