package com.jefmelo.autenticationforsms.activiy;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;

import com.jefmelo.autenticationforsms.R;
import com.jefmelo.autenticationforsms.databinding.ActivityMainBinding;
import com.jefmelo.autenticationforsms.helper.Permissoes;

public class MainActivity extends AppCompatActivity {

    private final String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

    private ActivityMainBinding binding;

    protected String telFormatado;

    //Progressive Dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Permissoes.validarPermissoes(1, this, permissoesNecessarias);

        binding.btnCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ValidarTokenMainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}