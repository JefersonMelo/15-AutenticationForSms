package com.jefmelo.autenticationforsms.activiy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.jefmelo.autenticationforsms.databinding.ActivityCadastrarBinding;
import com.jefmelo.autenticationforsms.databinding.ActivityMainBinding;
import com.jefmelo.autenticationforsms.helper.Permissoes;
import com.jefmelo.autenticationforsms.util.MaskFormatUtil;

public class CadastrarActivity extends AppCompatActivity {

    private final String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

    private ActivityCadastrarBinding binding;

    private String telFormatado;

    //Progressive Dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastrarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getSupportActionBar().hide();
        binding.editTextNome.requestFocus();

        Permissoes.validarPermissoes(1, this, permissoesNecessarias);

        //Definição das Máscaras
        binding.editTexTelefone.addTextChangedListener(MaskFormatUtil.mask(binding.editTexTelefone, MaskFormatUtil.FORMAT_FONE));
        binding.editTextCodPais.addTextChangedListener(MaskFormatUtil.mask(binding.editTextCodPais, MaskFormatUtil.FORMAT_COD_PAIS));
        binding.editTextCodArea.addTextChangedListener(MaskFormatUtil.mask(binding.editTextCodArea, MaskFormatUtil.FORMAT_COD_AREA));

        //Bloqueio de touch durante progresso
        progressDialog = new ProgressDialog(this);

        binding.btnCadastrar.setOnClickListener(v -> {
            String nomeUsuario = binding.editTextNome.getText().toString();
            String telSemFormatar = binding.editTextCodPais.getText().toString()
                    + binding.editTextCodArea.getText().toString()
                    + binding.editTexTelefone.getText().toString();

            telFormatado = "+" + MaskFormatUtil.unmask(telSemFormatar);

            if (TextUtils.isEmpty(telSemFormatar)) {
                progressDialog.setTitle(nomeUsuario);
                progressDialog.setMessage("Digite um Telefone Válido.");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(true);
            } else {
                progressDialog.setTitle("Aguarde...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Verificando Número do Telefone...");
                progressDialog.show();
                Intent intent = new Intent(CadastrarActivity.this, ValidarTokenActivity.class);
                intent.putExtra("numTelefone", telFormatado);
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
}