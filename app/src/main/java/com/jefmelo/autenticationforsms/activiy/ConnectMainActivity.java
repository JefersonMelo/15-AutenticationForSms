package com.jefmelo.autenticationforsms.activiy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jefmelo.autenticationforsms.databinding.ActivityConnectMainBinding;
import com.jefmelo.autenticationforsms.databinding.ActivityMainBinding;

public class ConnectMainActivity extends AppCompatActivity {

    private ActivityConnectMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSair.setOnClickListener(v->{
            finish();
        });
    }
}