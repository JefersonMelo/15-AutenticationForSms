package com.jefmelo.autenticationforsms.activiy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jefmelo.autenticationforsms.databinding.ActivityValidarTokenBinding;
import com.jefmelo.autenticationforsms.model.Usuario;

import java.util.concurrent.TimeUnit;

public class ValidarTokenActivity extends AppCompatActivity {

    private static final String TAG = "SMS";
    private ActivityValidarTokenBinding binding;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;
    private String telDigitado;
    private String _otp;
    private String nomeUsuario;
    private String senha;
    private String id;

    private ProgressDialog progressDialog;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValidarTokenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Aguarde...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        binding.editTextSmsValidador.requestFocus();

        //Recebe o telefone digitado pelo usuário no MainActivity
        id = getIntent().getStringExtra("id");
        nomeUsuario = getIntent().getStringExtra("nomeUsuario");
        senha = getIntent().getStringExtra("senha");
        telDigitado = getIntent().getStringExtra("numTelefone");

        //Usuario(String id, String nome, String senha, String telefone)
        user = new Usuario(id, nomeUsuario, senha, telDigitado);

        binding.textViewChecandoNumero.setText("Verificando: " + telDigitado);

        firebaseAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // Este retorno de chamada será invocado em duas situações:
                // 1 - Verificação instantânea. Em alguns casos, o número de telefone pode ser instantâneo
                // verificado sem a necessidade de enviar ou inserir um código de verificação.
                // 2 - Recuperação automática. Em alguns dispositivos, o Google Play Services pode automaticamente
                // detecta o SMS de verificação de entrada e executa a verificação sem
                // ação do usuário.
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Este retorno de chamada é invocado em uma solicitação inválida para verificação feita,
                // por exemplo, se o formato do número de telefone não for válido.
                progressDialog.dismiss();
                Toast.makeText(ValidarTokenActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificarId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificarId, forceResendingToken);
                // O código de verificação de SMS foi enviado para o número de telefone fornecido, nós
                // agora precisa pedir ao usuário para inserir o código e, em seguida, construir uma credencial
                // combinando o código com um ID de verificação.
                Log.d(TAG, "onCodeSent" + verificarId);
                progressDialog.dismiss();
                mVerificationId = verificarId;
                forceResendingToken = token;
                progressDialog.dismiss();
            }
        };

        startPhoneNumberVerification(telDigitado);

        //github
        binding.editTextSmsValidador.setOtpCompletionListener(otp -> {
            _otp = otp;
            verifyNumberPhoneWithCode( mVerificationId, _otp);
        });
        //github

        binding.btnValidador.setOnClickListener(v -> {
            verifyNumberPhoneWithCode( mVerificationId, _otp);
        });

        binding.textViewReenviarCod.setOnClickListener(v -> {
            if (TextUtils.isEmpty(telDigitado)) {
                Intent intent = new Intent(ValidarTokenActivity.this, CadastrarActivity.class);
                startActivity(intent);
                Toast.makeText(ValidarTokenActivity.this, "Número Inválido: " + telDigitado, Toast.LENGTH_LONG).show();
            } else {
                resendPhoneNumberVerification(telDigitado, forceResendingToken);
            }
        });

    }//Final OnCreate

    private void cadastrarUsuario(){
        user.salvarUsuario();
    }

    private void startPhoneNumberVerification(String telDigitado) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(telDigitado)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    //Final startPhoneNumberVerification

    private void resendPhoneNumberVerification(String tel, PhoneAuthProvider.ForceResendingToken token) {
        progressDialog.setMessage("Reenviando...");
        progressDialog.show();

        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(tel)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(token)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }
    //Final resendPhoneNumberVerification

    private void verifyNumberPhoneWithCode(String verificationId, String _otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, _otp);
        
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cadastrarUsuario();
                Intent intent = new Intent(ValidarTokenActivity.this, LogadoActivity.class);
                intent.putExtra("nomeUsuario", nomeUsuario);
                startActivity(intent);
                finishAffinity();
                Toast.makeText(ValidarTokenActivity.this, "Conectado", Toast.LENGTH_LONG).show();
            } else {
                if (TextUtils.isEmpty(binding.editTextSmsValidador.toString())){
                    binding.editTextSmsValidador.setError("Por favor, Digite o Token Recebido");
                    return;
                }
                Toast.makeText(ValidarTokenActivity.this, "Falhou", Toast.LENGTH_LONG).show();
            }
        });

    }
    //Final verifyNumberPhoneWithCode

    private void signInWithPhoneAuthCredential(PhoneAuthCredential authCredential) {
        //progressDialog.setMessage("Conectando...");

        firebaseAuth.signInWithCredential(authCredential)
                .addOnSuccessListener(authResult -> {
                    progressDialog.dismiss();
                    String telefone = firebaseAuth.getCurrentUser().getPhoneNumber();
                    Toast.makeText(ValidarTokenActivity.this, "Conectado com: " + telefone, Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ValidarTokenActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    //Final signInWithPhoneAuthCredential



}//Final Activity

//https://youtu.be/F_UemS493IM?t=2150
//https://youtu.be/F_UemS493IM?t=3030
//https://firebase.google.com/docs/auth/android/phone-auth?hl=pt-br
