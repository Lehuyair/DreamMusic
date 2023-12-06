package com.example.mydreammusicfinal.Activity;
import com.example.mydreammusicfinal.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextView tvError, tvFortgot, tvCountCheck;
    LinearLayout lnSignUp, lnWithinAcount;
    EditText edtID, edtPW;
    CheckBox chkRemember;
    Button btnLogin;
    ProgressBar progressBar;
    public final String TAG = "LoginActivity.class";
    public static int endex;
    public static final String User_ID = "UserID";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.color_Main1));
        initUI();
        setOnclick();


    }


    private void initUI() {
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        edtID = findViewById(R.id.edtIDLogin);
        edtPW = findViewById(R.id.edtPWLogin);
        chkRemember = findViewById(R.id.chkRemember);
        edtID.setText(sharedPreferences.getString("IDLogin", ""));
        edtPW.setText(sharedPreferences.getString("PWLogin", ""));
        chkRemember.setChecked(sharedPreferences.getBoolean("checked", false));
        btnLogin = findViewById(R.id.btnLogin);
        tvFortgot = findViewById(R.id.tvForgot);
        lnSignUp = findViewById(R.id.lnSignUp);
        lnWithinAcount = findViewById(R.id.lnWithinAcount);
        progressBar = findViewById(R.id.progress_Login);
    }

    private void setOnclick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String IDcheck = edtID.getText().toString().trim();
                String PWCheck = edtPW.getText().toString();
                if (IDcheck.isEmpty() || PWCheck.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email hoặc password không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else {
                    login(IDcheck, PWCheck);

                }
            }
        });
        lnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        lnWithinAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }
        });
    }

    private void login(String ID, String PW) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(ID, PW)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Sai Email hoặc Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}