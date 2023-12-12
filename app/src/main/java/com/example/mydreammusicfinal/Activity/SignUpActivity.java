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
    String gmailRegex = "^[a-zA-Z0-9_]+@gmail\\.com$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setStatusBar();
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
                String email = edtID.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                String password = edtPW.getText().toString().trim();
                String pwConfirm = edtConfrimPW.getText().toString().trim();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                if(checkVertification(email, name, password, pwConfirm)==false){

                }else{
                    progressBar.setVisibility(View.VISIBLE);
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
            }

            private Boolean checkVertification(String email, String name, String password, String pwConfirm) {
                int count = 0;
                if(email.isEmpty() || name.isEmpty() || password.isEmpty() || pwConfirm.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Vui lòng không để trống !", Toast.LENGTH_SHORT).show();
                    count ++;
                }else
                if (!email.matches(gmailRegex)){
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập đúng định dang gmail !", Toast.LENGTH_SHORT).show();
                    count ++;
                }else
                if(password.length() < 6){
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập mật khẩu tối thiểu 6 ký tự !", Toast.LENGTH_SHORT).show();
                    count ++;

                }else
                if(!password.equals(pwConfirm)){
                    Toast.makeText(SignUpActivity.this, "Mật khẩu xác nhận chưa đúng !", Toast.LENGTH_SHORT).show();
                    count ++;
                }

                if(count ==0){
                    return true;
                }else {
                    return false;
                }
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