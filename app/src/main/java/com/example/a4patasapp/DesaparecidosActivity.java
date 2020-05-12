package com.example.a4patasapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.a4patasapp.model.Desaparecido;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class DesaparecidosActivity extends AppCompatActivity {


    private Button bt_foto_desaparecido;
    private Button bt_anunciar;
    private EditText nome;
    private EditText email;
    private EditText telefone;
    private EditText descricao;
    private ImageView img;
    private Uri mSelectUri;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "teste";


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desaparecidos);

        nome = findViewById(R.id.edit_nomeDesaparecido);
        email = findViewById(R.id.edit_email_desaparecido);
        telefone = findViewById(R.id.edit_telefone_desaparecido);
        descricao = findViewById(R.id.edit_descricao_desaparecido);

        img = findViewById(R.id.img_foto_desaparecido);
        bt_foto_desaparecido = findViewById(R.id.bt_foto_desaparecido);
        bt_foto_desaparecido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });

        bt_anunciar = findViewById(R.id.bt_anunciar_desaparecido);
        bt_anunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*CRIANDO ANUNCIO*/
                criarAnuncio();
            }
        });

    }


    //    TRATANDO A CONVERSAO DA IMAGEM PARA SER CARREGADA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            mSelectUri = data.getData();
            //BITMAP VAI RECEBER A IMAGEM
            Bitmap bitmap = null;
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectUri);
                img.setImageDrawable(new BitmapDrawable(bitmap));
                bt_foto_desaparecido.setAlpha(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //    SELETOR DE FOTO
    private void selecionarFoto() {
        //ABRINDO A GALERIA DE FOTOS
        Intent intent = new Intent(Intent.ACTION_PICK);
        //TIPO DE DADOS QUE VOU PERMITIR A INSERÇÃO
        intent.setType("image/*");
        startActivityForResult(intent, 0);
        Log.d(TAG, "foto carregada!");

    }

    private void criarAnuncio() {

        /*VALIDAÇÃO DE CAMPOS*/

        final String nomeAnimal = nome.getText().toString().toUpperCase();
        if (nomeAnimal.equalsIgnoreCase("") || nomeAnimal == null) {
            Toast.makeText(DesaparecidosActivity.this, "Campo NOME é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }


        final String telefoneContato = telefone.getText().toString();
        if (telefoneContato == null || telefoneContato.isEmpty()) {
            Toast.makeText(DesaparecidosActivity.this, "Campo TELEFONE é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (telefoneContato.length() < 10) {
            Toast.makeText(DesaparecidosActivity.this, "Você deve informar o DDD junto ao número do telefone!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (telefoneContato.length() > 12) {
            Toast.makeText(DesaparecidosActivity.this, "Telefone deve ter de 10 a 12 dígitos com o DDD incluso!", Toast.LENGTH_SHORT).show();
            return;
        }

        final String emailContato = email.getText().toString();
        //GARANTINDO PREENCHIMENTO DOS DADOS OBRIGATÓRIOS E VALIDAÇÕES DE FORMULÁRIO
        if (emailContato == null || emailContato.isEmpty()) {
            Toast.makeText(DesaparecidosActivity.this, "Campo EMAIL é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }

        final String descricaoDesaparecido = descricao.getText().toString().toUpperCase();
        if (descricaoDesaparecido == null || descricaoDesaparecido.isEmpty()) {
            Toast.makeText(DesaparecidosActivity.this, "Campo DESCRIÇÃO é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (descricaoDesaparecido.length() > 800) {
            Toast.makeText(DesaparecidosActivity.this, "Campo DESCRIÇÃO deve ter no máximo 300 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

        //CRIANDO A HASH PARA REFERÊNCIA DO NOME DO ARQUIVO
        String fileName = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/fotos/desaparecidos" + fileName);

        //SUBINDO A FOTO PARA A PASTA IMAGES DO FIREBASE
        ref.putFile(mSelectUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //PEGA A REFERÊNCIA DA URL ONDE ESTÁ HOSPEDADO NO FIREBASE A IMAGEM DEPOIS DE ELA TER SUBIDO PARA QUE EU POSSA TRABALHAR COM ELA
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i("URI = ", uri.toString());

                                //ANÚNCIO A SER REGISTRADO
                                // data/hora atual
                                LocalDateTime agora = LocalDateTime.now();
                                DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                                String dataFormatada = formatterData.format(agora).replaceAll("/", "").trim();

                                DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss");
                                String horaFormatada = formatterHora.format(agora).replaceAll(":", "").trim();

                                final String codAnuncio = "D-" + dataFormatada + horaFormatada;
                                String dataCriacao = formatterData.format(agora);

                                final Desaparecido desaparecido = new Desaparecido(codAnuncio, nomeAnimal, emailContato, telefoneContato, uri.toString(),
                                        descricaoDesaparecido, dataCriacao);

                                //CRIANDO A REFERÂNCIA PARA UMA COLEÇÃO DE ANÚNCIOS
                                db.collection("animais-desaparecidos").document(desaparecido.getCodAnuncio())
                                        .set(desaparecido)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Anuncio cadastrado com sucesso!");
                                                Toast.makeText(DesaparecidosActivity.this, "Anúncio " + codAnuncio + " cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(DesaparecidosActivity.this, ListarDesaparecidosActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Erro ao cadastrar anúncio!", e);
                                                Toast.makeText(DesaparecidosActivity.this, "Falha ao cadastrar anúncio! - Tente novamente.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(DesaparecidosActivity.this, DesaparecidosActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("###ERRO###", e.getMessage(), e);
                    }
                });

    }
}
