package com.jefmelo.autenticationforsms.activiy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jefmelo.autenticationforsms.databinding.ActivityConectarBinding;

public class LogadoActivity extends AppCompatActivity {

    private ActivityConectarBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConectarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String nomeUsuario = getIntent().getStringExtra("nomeUsuario");
        binding.textViewUsuario.setText("Nome: " + nomeUsuario);

        auth = FirebaseAuth.getInstance();
        checarStatusUsuario();

        binding.buttonSair.setOnClickListener(v -> {
            auth.signOut();
            checarStatusUsuario();
        });
    }

    private void checarStatusUsuario(){
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            String tel = user.getPhoneNumber();
            binding.textViewNumTelefoneLogado.setText("Tel: " + tel);
        }else{
            finishAffinity();
        }
    }
}