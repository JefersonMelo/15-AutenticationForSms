package com.jefmelo.autenticationforsms.activiy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.jefmelo.autenticationforsms.config.ConfigFirebase;
import com.jefmelo.autenticationforsms.databinding.ActivityCadastrarBinding;
import com.jefmelo.autenticationforsms.helper.Permissoes;
import com.jefmelo.autenticationforsms.util.MaskFormatUtil;

public class CadastrarActivity extends AppCompatActivity {

    private final String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

    private FirebaseAuth auth;

    private ActivityCadastrarBinding binding;

    //Progressive Dialog
    private ProgressDialog progressDialog;

    private String telFormatado;
    private String nomeUsuario;
    private String senha;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastrarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editTextNome.requestFocus();

        Permissoes.validarPermissoes(1, this, permissoesNecessarias);

        //Definição das Máscaras
        binding.editTexTelefone.addTextChangedListener(MaskFormatUtil.mask(binding.editTexTelefone, MaskFormatUtil.FORMAT_FONE));
        binding.editTextCodPais.addTextChangedListener(MaskFormatUtil.mask(binding.editTextCodPais, MaskFormatUtil.FORMAT_COD_PAIS));
        binding.editTextCodArea.addTextChangedListener(MaskFormatUtil.mask(binding.editTextCodArea, MaskFormatUtil.FORMAT_COD_AREA));

        //Bloqueio de touch durante progresso
        progressDialog = new ProgressDialog(this);
        auth = ConfigFirebase.getFirebaseAuth();

        binding.btnCadastrar.setOnClickListener(v -> {
            nomeUsuario = binding.editTextNome.getText().toString();
            senha = binding.editTextSenhaCadastro.getText().toString();
            String telSemFormatar = binding.editTextCodPais.getText().toString()
                    + binding.editTextCodArea.getText().toString()
                    + binding.editTexTelefone.getText().toString();

            if (TextUtils.isEmpty(binding.editTextCodPais.getText().toString())) {
                binding.editTextCodPais.setError("Por favor, Digite o Código do País");
                return;
            }
            if (TextUtils.isEmpty(binding.editTextCodArea.getText().toString())) {
                binding.editTextCodArea.setError("Por favor, Digite o Código do Área");
                return;
            }
            if (TextUtils.isEmpty(binding.editTexTelefone.getText().toString())) {
                binding.editTexTelefone.setError("Por favor, Digite um Telefone Válido");
                return;
            }
            if (TextUtils.isEmpty(binding.editTextSenhaCadastro.getText().toString())) {
                binding.editTextSenhaCadastro.setError("Por favor, Digite uma Senha");
                return;
            }

            telFormatado = "+" + MaskFormatUtil.unmask(telSemFormatar);
            id = auth.getUid();

            if (TextUtils.isEmpty(nomeUsuario)) {
                binding.editTextNome.setError("Por favor, Digite um Nome");
                return;
            } else {
                progressDialog.setTitle("Aguarde...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Verificando Número do Telefone...");
                progressDialog.show();

                Intent intent = new Intent(CadastrarActivity.this, ValidarTokenActivity.class);
                intent.putExtra("numTelefone", telFormatado);
                intent.putExtra("nomeUsuario", nomeUsuario);
                intent.putExtra("senha", senha);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    //Inicio: Requerimento de Permissões
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int resultado : grantResults) {
            if (resultado == PackageManager.PERMISSION_DENIED) {
                alertaPermissao();
            }
        }
    }

    private void alertaPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Permissões Negadas");
        builder.setMessage("Para Prosseguir a Permissão Deve ser Aceita");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }//Final: Requerimento de Permissões

}//Final da Classe