package com.jefmelo.autenticationforsms.activiy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jefmelo.autenticationforsms.databinding.ActivityMainBinding;
import com.jefmelo.autenticationforsms.databinding.ActivityValidarTokenMainBinding;
import com.mukesh.OnOtpCompletionListener;

import java.util.concurrent.TimeUnit;

public class ValidarTokenMainActivity extends AppCompatActivity {

    protected ActivityValidarTokenMainBinding binding;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;
    private String telDigitado;
    private static final String TAG = "MAIN_TAG";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValidarTokenMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Aguarde...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //getSupportActionBar().hide();
        binding.editTextSmsValidador.requestFocus();

        //Recebe o telefone digitado pelo usuário no MainActivity
        telDigitado = getIntent().getStringExtra("numTelefone");

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

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Este retorno de chamada é invocado em uma solicitação inválida para verificação feita,
                // por exemplo, se o formato do número de telefone não for válido.

            }

            @Override
            public void onCodeSent(@NonNull String verificarId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificarId, forceResendingToken);
                // O código de verificação de SMS foi enviado para o número de telefone fornecido, nós
                // agora precisa pedir ao usuário para inserir o código e, em seguida, construir uma credencial
                // combinando o código com um ID de verificação.

                progressDialog.dismiss();
                mVerificationId = verificarId;
            }
        };

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(telDigitado)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

        binding.editTextSmsValidador.setOtpCompletionListener(otp -> {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(ValidarTokenMainActivity.this, "Conectado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ValidarTokenMainActivity.this, "Falhou", Toast.LENGTH_LONG).show();
                }
            });
        });

        binding.btnValidador.setOnClickListener(v -> {
            Intent intent = new Intent(ValidarTokenMainActivity.this, ConnectMainActivity.class);
            startActivity(intent);
            finish();
        });

    }//Final OnCreate

    private void startPhoneNumberVerification(String telDigitado) {
        //progressDialog.setMessage("Checando...");
        //progressDialog.show();

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
        //progressDialog.setMessage("Reenviando...");
        //progressDialog.show();

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

    private void verifyNumberPhoneWithCode(String verificationId, String codigo) {
        //progressDialog.setMessage("Verificando...");
        //progressDialog.show();
        PhoneAuthCredential authCredential = PhoneAuthProvider.getCredential(verificationId, codigo);
        signInWithPhoneAuthCredential(authCredential);

    }
    //Final verifyNumberPhoneWithCode

    protected void signInWithPhoneAuthCredential(PhoneAuthCredential authCredential) {
        //progressDialog.setMessage("Conectando...");

        firebaseAuth.signInWithCredential(authCredential)
                .addOnSuccessListener(authResult -> {
                    progressDialog.dismiss();
                    String telefone = firebaseAuth.getCurrentUser().getPhoneNumber();
                    //Toast.makeText(ValidarCodigoMainActivity.this, "Conectado com: " + telefone, Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        //Toast.makeText(ValidarCodigoMainActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }
    //Final signInWithPhoneAuthCredential

}//Final Activity

//https://youtu.be/F_UemS493IM?t=2157
//https://firebase.google.com/docs/auth/android/phone-auth?hl=pt-br
