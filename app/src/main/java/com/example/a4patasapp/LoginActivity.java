package com.example.a4patasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditEmail;
    private EditText mEditSenha;
    private EditText mEditNome;
    private EditText mTelefone;
    private TextView tvCadastrar;
    private  TextView tvRecuperarSenha;
    private String mUltimoLogin;
    private String emailInformado;
    private String senhaInformada;
    private Button btLogin;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TELA DE LOGIN MANTEM SEU LAYOUT EM MODO RETRATO
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

//        ATUALIZANDO DATA/HORA SISTEMA
        Date currentTime = Calendar.getInstance().getTime();
        mUltimoLogin = "" + currentTime;

//        ALIMENTANDO DEMAIS VARIAVEIS
        mEditEmail = findViewById(R.id.edit_email);
        mEditSenha = findViewById(R.id.edit_senha);
        btLogin = findViewById(R.id.bt_login);
        tvCadastrar = findViewById(R.id.tv_cadastrar);
        tvRecuperarSenha = findViewById(R.id.tv_recuperar_senha);

//        CONFIGURANDO AÇÃO DO BOTÃO LOGIN
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // PEGANDO O QUE FOI INFORMADO NOS CAMPOS E-MAIL, SENHA
                emailInformado = mEditEmail.getText().toString();
                senhaInformada = mEditSenha.getText().toString();

                //GARANTINDO PREENCHIMENTO DOS DADOS OBRIGATÓRIOS E VALIDAÇÕES DE FORMULÁRIO
                if (emailInformado == null || emailInformado.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Campo EMAIL é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (senhaInformada == null || senhaInformada.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Campo SENHA é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (senhaInformada.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Campo SENHA deve ter no mínimo 6 caracteres!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //VALIDANDO SE O E-MAIL INFORMADO É VALIDO
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailInformado).matches() != true) {
                    Toast.makeText(LoginActivity.this, "Formato de e-mail inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //VALIDANDO COM FIREBASE A EXISTÊNCIA DO E-MAIL INFORMADO
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailInformado, senhaInformada)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.i("teste", task.getResult().getUser().getUid());
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("teste", e.getMessage());
                                Toast.makeText(LoginActivity.this, "E-mail ou senha inválido!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

//        AÇÃO BOTÃO CADASTRAR
        tvCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

//        AÇÃO BOTÃO RECUPERAR SENHA

        tvRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RecuperarSenhaActivity.class);
                startActivity(intent);
            }
        });
    }

    //    VERIFICANDO SE O USUARIO ESTÁ ATIVO
    @Override
    public void onStart() {
        super.onStart();
    }
}
