package com.jefmelo.autenticationforsms.activiy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jefmelo.autenticationforsms.databinding.ActivityConectarBinding;

public class ConectarActivity extends AppCompatActivity {

    private ActivityConectarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConectarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSair.setOnClickListener(v -> {
            finishAffinity();
        });
    }
}