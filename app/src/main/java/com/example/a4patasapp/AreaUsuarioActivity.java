package com.example.a4patasapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4patasapp.model.Anuncio;
import com.example.a4patasapp.model.Duvida;
import com.example.a4patasapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class AreaUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText telefone;
    private final String TAG = "teste";
    private RecyclerView rv_anuncios_usuario;
    private Button bt_salvar_alteracoes;
    private GroupAdapter adapter;
    private ArrayList<Anuncio> anunciosRecebidos = new ArrayList<>();
    private String emailLogado;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_usuario);

        nome = findViewById(R.id.edit_nomeAreaCliente);
        email = findViewById(R.id.edit_emailAreaCliente);
        senha = findViewById(R.id.edit_senhaAreaCliente);
        telefone = findViewById(R.id.edit_telefoneAreaCliente);

        bt_salvar_alteracoes = findViewById(R.id.bt_salvar_dados_usuario);

        /*RECEBER DADOS DO USUARIO E ANUNCIOS*/

        Bundle extras = getIntent().getExtras();
        emailLogado = extras.getString("emailLogado");
        Log.d(TAG, "Area Usuario - Logado = " + emailLogado);
        Intent intent = getIntent();
        anunciosRecebidos = intent.getParcelableArrayListExtra("anuncios");
        Log.d(TAG, "Area Usuario - total anuncios recebidos = " + anunciosRecebidos.size());

        /*#################################*/

        /*PREENCHER EDIT TEXT COM DADOS DO USUARIO*/
         getUser(emailLogado);




        /*RETORNAR ANUNCIOS DO USUARIO NA RECYCLERVIEW*/

        rv_anuncios_usuario = findViewById(R.id.rv_anuncios_usuario);
        adapter = new GroupAdapter();
        carregarAnuncios(anunciosRecebidos, emailLogado);
        rv_anuncios_usuario.setLayoutManager(new LinearLayoutManager(this));
        rv_anuncios_usuario.setAdapter(adapter);


        bt_salvar_alteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ProgressBar
                pBar = new ProgressDialog(AreaUsuarioActivity.this);
                pBar.setCancelable(true);
                pBar.setMessage("Carregando");
                pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pBar.show();

            }
        });
    }

    private class AnuncioItemUsuario extends Item<ViewHolder> {

        private final Anuncio anuncio;

        private AnuncioItemUsuario(Anuncio anuncio) {
            this.anuncio = anuncio;
        }

        //CONECTANDO AOS OBJETOS PARA PODER EDITAR SEUS VALORES

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

            // MANIPULA OBJETO TEXT VIEW DA LISTA

            TextView titulo = viewHolder.itemView.findViewById(R.id.tv_titulo_anuncio_usuario);
            Button delete = viewHolder.itemView.findViewById(R.id.bt_delete_usuario);
            ImageView foto = viewHolder.itemView.findViewById(R.id.item_foto_usuario);

            Picasso.get()
                    .load(anuncio.getImagem())
                    .resize(900, 540)
                    .transform(new RoundedCornersTransformation(22, 22))
                    .into(foto);
            titulo.setText(anuncio.getTitulo());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletarAnuncioUsuario(anuncio.getCodAnuncio());
                    removerAnuncio(anuncio);
                }
            });
        }

        @Override
        public int getLayout() {
            return R.layout.item_rv_anuncio_user;
        }
    }


    /*IMPORTA ANUNCIOS DO USUÁRIO*/
    private void carregarAnuncios(ArrayList<Anuncio> anunciosRecebidos, final String emailLogado) {
        for (int i = 0; i < anunciosRecebidos.size(); i++) {
            if (anunciosRecebidos.get(i).getEmail().equalsIgnoreCase(emailLogado)) {
                adapter.add(new AnuncioItemUsuario(anunciosRecebidos.get(i)));
            }
        }
    }


    public void getUser(final String emailUsuario) {

        // [START get_multiple_all]
        db.collection("/usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //                            SERIALIZANDO O ANUNCIO VINDO DO JSON - FIREBASE
                                Usuario usuario = document.toObject(Usuario.class);
                                if (usuario.getUserID().equalsIgnoreCase(emailUsuario)) {
                                    nome.setText(usuario.getNome());
                                    email.setText(usuario.getEmail());
                                    senha.setText(usuario.getSenha());
                                    telefone.setText(usuario.getTelefone());
                                    Log.d(TAG, "Usuario encontrado OK= " + usuario.getNome());
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        // [END get_multiple_all]

    }

    /*EXCLUIR OBJETO DA LISTA*/

    public void deletarAnuncioUsuario(final String codigoDocumento) {
        // [START delete_document]
       db.collection("/doacoes").document(codigoDocumento)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Anúncio  " +""+codigoDocumento +" excluído!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Erro ao tentar excluir anúncio  "+""+codigoDocumento, e);

                    }
                });
        // [END delete_document]
    }

    public void removerAnuncio(Anuncio anuncio) {
        int position = anunciosRecebidos.indexOf(anuncio);

        //ProgressBar
        pBar = new ProgressDialog(AreaUsuarioActivity.this);
        pBar.setCancelable(true);
        pBar.setMessage("Excluindo anúncio:  " +anuncio.getCodAnuncio());
        pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pBar.show();
        anunciosRecebidos.remove(position);
        adapter.notifyItemRemoved(position);
    }

}