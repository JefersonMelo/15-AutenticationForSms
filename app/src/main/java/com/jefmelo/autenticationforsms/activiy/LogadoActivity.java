package com.jefmelo.autenticationforsms.activiy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jefmelo.autenticationforsms.config.ConfigFirebase;
import com.jefmelo.autenticationforsms.databinding.ActivityLogadoBinding;

public class LogadoActivity extends AppCompatActivity {

    private ActivityLogadoBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String nUsuario = getIntent().getStringExtra("nomeUsuario");
        binding.textViewUsuario.setText("Nome: " + nUsuario);

        auth = ConfigFirebase.getFirebaseAuth();

        checarStatusUsuario();

        binding.buttonSair.setOnClickListener(v -> {
            auth.signOut();
            checarStatusUsuario();
        });
    }

    private void checarStatusUsuario() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String tel = user.getPhoneNumber();
            binding.textViewNumTelefoneLogado.setText("Tel: " + tel);
        } else {
            finishAffinity();
        }
    }
}