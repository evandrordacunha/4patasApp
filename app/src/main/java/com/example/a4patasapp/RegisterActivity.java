package com.example.a4patasapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a4patasapp.model.Usuario;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private EditText mNome;
    private EditText mEmail;
    private EditText mSenha;
    private EditText mTelefone;
    private Button btCadastrar;
    private String telefone;
    private String dataCriacao;
    private String nome;
    private String email;
    private String senha;
    private String latitude;
    private String longitude;

    //CLIENTE PARA MANIPULACAO DE LOCALIZAÇÃO DO USUARIO
    FusedLocationProviderClient client;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        INSTANCIANDO CLIENT
        client = LocationServices.getFusedLocationProviderClient(this);




//        INICIALIZANDO AS VARIAVEIS
        mNome = findViewById(R.id.edit_nomeCad);
        mEmail = findViewById(R.id.edit_emailCad);
        mSenha = findViewById(R.id.edit_senhaCad);
        mTelefone = findViewById(R.id.edit_telefoneCad);
        btCadastrar = findViewById(R.id.bt_realizarCadastro);

        telefone = mTelefone.getText().toString();
        LocalDateTime dataHora = LocalDateTime.now();
        dataCriacao = dataHora.toString();

        nome = mNome.getText().toString();
        senha = mSenha.getText().toString();
        email = mEmail.getText().toString();

//        CONFIGURANDO AÇÃO PARA BOTÃO CADASTRAR

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    /**
     * AUTENTICANDO USUARIO COM O FIREBASE AUTHENTICATION
     */
    private void createUser() {

        String nomeInformado = mNome.getText().toString();
        String emailInformado = mEmail.getText().toString();
        String senhaInformada = mSenha.getText().toString();
        //GARANTINDO PREENCHIMENTO DOS DADOS OBRIGATÓRIOS E VALIDAÇÕES DE FORMULÁRIO
        if (emailInformado == null || emailInformado.isEmpty()) {
            Toast.makeText(this, "Campo EMAIL é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nomeInformado == null || nomeInformado.isEmpty()) {
            Toast.makeText(this, "Campo NOME é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senhaInformada == null || senhaInformada.isEmpty()) {
            Toast.makeText(this, "Campo SENHA é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senhaInformada.length() < 6) {
            Toast.makeText(this, "Campo SENHA deve ter no mínimo 6 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

        //VALIDANDO SE O E-MAIL INFORMADO É VALIDO
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailInformado).matches() != true) {
            Toast.makeText(this, "Formato de e-mail inválido!", Toast.LENGTH_SHORT).show();
            return;
        }



        //GRAVANDO DADOS DE USUÁRIO NO FIREBASE AUTHENTICATION
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailInformado, senhaInformada)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("DADOS INSERIDOS!", task.getResult().getUser().getUid());
                            Toast.makeText(RegisterActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            //SE O CADASTRO FOR REALIZADO COM SUCESSO, VOLTA PARA A TELA DE LOGIN APÓS INSERÇÃO DE DADOS
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            saveUserInFirebase();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("ERRO ENCONTRADO!", e.getMessage());
                    }
                });
    }

    //    GRAVANDO INFORMAÇÕES DE USUÁRIOS DA TABELA DO FIREBASE
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveUserInFirebase() {
        //CRIANDO A HASH PARA REFERÊNCIA DO NOME DO ARQUIVO
        String fileName = UUID.randomUUID().toString();
        final String userID = mEmail.getText().toString();
        String nome = mNome.getText().toString();
        String email = mEmail.getText().toString();
        String senha = mSenha.getText().toString();
        telefone = mTelefone.getText().toString();
        LocalDateTime dataHora = LocalDateTime.now();
        dataCriacao = dataHora.toString();
//        USUÁRIO NOVO CADASTRADO
        Usuario usuario = new Usuario(userID, nome, email, senha, dataCriacao, telefone,latitude,longitude);

//       CRIANDO A REFERÂNCIA PARA UMA COLEÇÃO DE USUÁRIOS
        FirebaseFirestore.getInstance().collection("usuarios").document(userID)
                .set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Usuario inserido ", userID);
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Erro ao inserir ", userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("###ERRO###", e.getMessage(), e);
            }
        });
    }
    //    CHECAR SE O PLAY SERVICES ESTÁ INSTALADO NO DISPOSITIVO DO USUARIO
    @Override
    protected void onResume() {
        super.onResume();

        int statusCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

//        TRATAR DA ATIVAÇÃO CORRETA DO PLAY SERVICES
        switch (statusCode) {
//               SE NÃO TEM O PLAY SERVICES INSTALADO
            case ConnectionResult.SERVICE_MISSING:
//                SE O PLAY SERVICES PRECISA SER ATUALIZADO
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
//                SE O PLAY SERVICES ESTÁ DESATIVADO
            case ConnectionResult.SERVICE_DISABLED:
                Log.d("teste","showDialog");
//                EXIBINDO MENSAGEM PARA O USUARIO PEDINDO A ATIVAÇÃO OU ATUALIZACAO DO PLAY SERVICES
                GoogleApiAvailability.getInstance().getErrorDialog(this, statusCode,
                        0, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }
                        }).show();
                break;

            case ConnectionResult.SUCCESS:
                Log.d("teste","Google play services tá ok");
                break;
        }

//        PEDE AUTORIZAÇÃO PARA O USUÁRIO PARA ACESSAR A LOCALIZAÇÃO

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            // permission has been granted, continue as usual
            Task<Location> locationResult = LocationServices
                    .getFusedLocationProviderClient(this /** Context */)
                    .getLastLocation();
        }
//        PEGANDO A ÚLTIMA LOCALIZAÇÃO DO USUÁRIO
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    Log.i("teste",location.getLatitude() +" " +location.getLongitude());
                    latitude ="" +location.getLatitude();
                    longitude = ""+location.getLongitude();
                }else{
                    Log.e("teste","Localização está nula!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });
    }
}

