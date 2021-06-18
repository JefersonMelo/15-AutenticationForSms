package com.jefmelo.autenticationforsms.activiy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jefmelo.autenticationforsms.databinding.ActivityMainBinding;
import com.jefmelo.autenticationforsms.databinding.ActivityValidarTokenMainBinding;

public class ValidarTokenMainActivity extends AppCompatActivity {

    protected ActivityValidarTokenMainBinding binding;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;
    private String tel;
    private static final String TAG = "MAIN_TAG";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValidarTokenMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnValidador.setOnClickListener(v->{
            Intent intent = new Intent(ValidarTokenMainActivity.this, ConnectMainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}