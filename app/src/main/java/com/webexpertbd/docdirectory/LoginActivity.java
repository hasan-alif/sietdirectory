package com.webexpertbd.docdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText phone;
    Button sendOtp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(LoginActivity.this);
        ccp = findViewById(R.id.ccpOtp);
        phone = findViewById(R.id.phoneOtp);
        sendOtp = findViewById(R.id.sendOtp);
        mAuth = FirebaseAuth.getInstance();
        ccp.registerCarrierNumberEditText(phone);
        sendOtp.setOnClickListener(v -> {
            Log.d("TAG", "onCreate: "+ccp.getFullNumber());
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+"+ccp.getFullNumber(), 60, TimeUnit.MILLISECONDS, LoginActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                    Toast.makeText(LoginActivity.this, "Mobile Verification Successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    Intent intent = new Intent(LoginActivity.this,OtpActivity.class);
                    intent.putExtra("id",s);
                    startActivity(intent);
                }

                @Override
                public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onStart() {
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
        super.onStart();
    }
}