package com.jefmelo.autenticationforsms.activiy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.jefmelo.autenticationforsms.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textViewNovoCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastrarActivity.class);
            startActivity(intent);
            finishAffinity();
        });
    }
}