package com.example.mydreammusicfinal.Activity;

import static com.example.mydreammusicfinal.Constance.Constance.KEY_NAME_USER_REGISTRY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    public static  final String TAG = OTPActivity.class.getName();

    private EditText edtphoneotp;
    private Button btnphonenumotp;
    private TextView txtmisspass;

    private FirebaseAuth mAuth;
    private String phonenumber, mVerificatioId;
    private  PhoneAuthProvider.ForceResendingToken mForceResendingToken;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        getDataIntent();
        initui();
        seTitleToobar();
        mAuth = FirebaseAuth.getInstance();
        btnphonenumotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strOtp = edtphoneotp.getText().toString().trim();
                onClicksenOTP(strOtp);
            }
        });
        txtmisspass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickmissotp();
            }
        });



    }

    private void onClickmissotp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phonenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setForceResendingToken(mForceResendingToken)// (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OTPActivity.this,"Faile",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                mVerificatioId = s;
                                mForceResendingToken = forceResendingToken;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private  void  getDataIntent(){
        phonenumber = getIntent().getStringExtra("phone_number");
        mVerificatioId = getIntent().getStringExtra("verification_id");
    }

    private  void  seTitleToobar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("OTPActivity");
        }
    }
    private void onClicksenOTP(String strOtp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificatioId, strOtp);
        signInWithPhoneAuthCredential(credential);
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
        Intent intent = new Intent(this, UpdateInformation_User.class);
        intent.putExtra("phone_number", phoneNumber);
        startActivity(intent);
    }



    private void initui() {
        edtphoneotp = findViewById(R.id.edt_otp);
        btnphonenumotp = findViewById(R.id.btn_otp);
        txtmisspass = findViewById(R.id.txtotp);
    }

}