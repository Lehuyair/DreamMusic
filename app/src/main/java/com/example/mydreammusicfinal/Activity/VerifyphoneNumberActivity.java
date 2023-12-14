package com.example.mydreammusicfinal.Activity;

import static com.example.mydreammusicfinal.Constance.Constance.KEY_NAME_USER_REGISTRY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydreammusicfinal.Fragment.Fragment_Me;
import com.example.mydreammusicfinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

public class VerifyphoneNumberActivity extends AppCompatActivity {
    Context context;
    private EditText edtphonenumber,edtfullname;
    private Button btnphonenumber;
    private TextView txtmisspass;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private  static  final  String TAG = VerifyphoneNumberActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyphone_number);
        setStatusBar();
        initui();
        seTitleToobar();

        mAuth = FirebaseAuth.getInstance();
        btnphonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strPhoneNumber = edtphonenumber.getText().toString().trim();

                progressDialog = new ProgressDialog(VerifyphoneNumberActivity.this);
                progressDialog.setMessage("Đang xác minh số...");
                progressDialog.setCancelable(false);



                onClickphonenumber(strPhoneNumber);
            }
        });
    }

    private void onClickmissotp() {
    }

    private  void  seTitleToobar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("verifyphoneNumber");
        }
    }

    private void initui() {
        edtphonenumber = findViewById(R.id.edt_number);
        edtfullname = findViewById(R.id.edt_nameotp);
        btnphonenumber= findViewById(R.id.btn_number);

    }
    private void onClickphonenumber(String strPhoneNumber) {

        if (strPhoneNumber.length() < 10) {
            // Hiển thị thông báo lỗi dưới EditText
            edtphonenumber.setError("Số điện thoại không hợp lệ");
            return;
        } else {
            // Nếu không có lỗi, xóa thông báo lỗi nếu có
            edtphonenumber.setError(null);
        }

        progressDialog.show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(strPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                                progressDialog.dismiss(); // Dismiss the ProgressDialog when verification is completed
                                Toast.makeText(VerifyphoneNumberActivity.this, "Xác minh thành công", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressDialog.dismiss(); // Dismiss the ProgressDialog on verification failure
                                Toast.makeText(VerifyphoneNumberActivity.this, "Xác minh thất bại", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                gotoEnterOtpActivity(strPhoneNumber,s);
                            }
                        })
                        // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            gotToMainActivity(user.getPhoneNumber());
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void gotToMainActivity(String phoneNumber) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("phone_number", phoneNumber);
        intent.putExtra(KEY_NAME_USER_REGISTRY, edtfullname.getText().toString().trim());
        startActivity(intent);
    }

    private void gotoEnterOtpActivity(String strPhoneNumber, String s) {
        Intent intent = new Intent(this, OTPActivity.class);
        intent.putExtra("phone_number", strPhoneNumber);
        intent.putExtra("verification_id", s);
        intent.putExtra(KEY_NAME_USER_REGISTRY, edtfullname.getText().toString().trim());
        startActivity(intent);
    }
    private void setStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }



}