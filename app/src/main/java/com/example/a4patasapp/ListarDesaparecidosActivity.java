package com.example.a4patasapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4patasapp.model.Anuncio;
import com.example.a4patasapp.model.Desaparecido;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ListarDesaparecidosActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private RecyclerView rv_desaparecidos;
    private final String TAG = "teste";


    private ArrayList<Desaparecido>desaparecidos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_desaparecidos);
        fetchDesaparecidos();

        rv_desaparecidos = findViewById(R.id.rv_desaparecidos);
        adapter = new GroupAdapter();
        rv_desaparecidos.setLayoutManager(new LinearLayoutManager(this));
        rv_desaparecidos.setAdapter(adapter);
    }


    private void fetchDesaparecidos() {


        //CRIA UMA REFERENCIA PARA A COLEÇÃO DE ANUNCIOS
        FirebaseFirestore.getInstance().collection("/animais-desaparecidos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        //VERIFICANDO SE ENCONTROU ALGUMA EXCEÇÃO CAPAZ DE IMPEDIR A EXECUÇÃO, CASO ENCONTRE, PARE A APLICAÇÃO
                        if (e != null) {
                            Log.e(TAG, "Erro: ", e);
                            return;
                        }
                        //REFERÊNCIA PARA TODOS ANÚNCIOS DA BASE
                        List<DocumentSnapshot> documentos = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot doc : documentos) {
//                            SERIALIZANDO O ANUNCIO VINDO DO JSON - FIREBASE
                            Desaparecido desaparecido = doc.toObject(Desaparecido.class);
                            Log.d(TAG,"ADICIONANDO DESAPARECIDO =  "+desaparecido.getNome());
                            desaparecidos.add(desaparecido);
                            Log.d(TAG,desaparecidos.size()+"  tamanho lista de desaparecidos");
//                            CARREGA O ANÚNCIO PARA A RECYCLER VIEW
                            adapter.add(new DesaparecidoItem(desaparecido));
                        }
                    }
                });
    }

    private class DesaparecidoItem extends Item<ViewHolder> {

        private final Desaparecido desaparecido;

        private DesaparecidoItem(Desaparecido desaparecido) {
            this.desaparecido = desaparecido;
        }

        //CONECTANDO AOS OBJETOS PARA PODER EDITAR SEUS VALORES

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

            // MANIPULA OBJETO TEXT VIEW DA LISTA
            ImageView foto = viewHolder.itemView.findViewById(R.id.im_foto_desaparecido);
            TextView nome = viewHolder.itemView.findViewById(R.id.tv_nome_desaparecido_item);
            TextView descricao = viewHolder.itemView.findViewById(R.id.tv_descricao_desaparecido);

            /*ENVIANDO WHATS*/
            Button bt_whats_desaparecido = viewHolder.itemView.findViewById(R.id.bt_whats_desaparecido);
            bt_whats_desaparecido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://api.whatsapp.com/send?1=pt_BR&phone=55"+desaparecido.getTelefone()));
                    startActivity(intent2);
                }
            });


            /*FAZENDO LIGAÇÃO DE TELEFONE*/
            Button bt_ligacao_desaparecido = viewHolder.itemView.findViewById(R.id.bt_telefonar_desaparecido);
            bt_ligacao_desaparecido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("tel: " +desaparecido.getTelefone());
                    Intent telefonar = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(telefonar);
                }
            });

            /*ENVIANDO EMAIL*/
            Button bt_email_desaparecido = viewHolder.itemView.findViewById(R.id.bt_email_desaparecido);
            bt_email_desaparecido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] remetente = {desaparecido.getEmail()};
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL,remetente);
                    email.setType("message/rfc822");
                    startActivity(email);
                }
            });

            Button bt_home_desaparecido = viewHolder.itemView.findViewById(R.id.bt_home_deaparecido);
            bt_home_desaparecido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListarDesaparecidosActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });


//            CARREGANDO A IMAGEM DO ANUNCIO JÁ COM BORDAS ARREDONDADAS
            Picasso.get()
                    .load(desaparecido.getFoto())
                    .resize(900, 540)
                    .transform(new RoundedCornersTransformation(22, 22))
                    .into(foto);

//            CARREGANDO DEMAIS ATRIBUTOS AO LAYOUT
            nome.setText(desaparecido.getNome());
            descricao.setText(desaparecido.getDescricao());
        }

        @Override
        public int getLayout() {
            return R.layout.item_desaparecido;
        }
    }
}
