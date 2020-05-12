package com.example.a4patasapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.a4patasapp.model.Anuncio;
import com.example.a4patasapp.model.Duvida;
import com.example.a4patasapp.model.Usuario;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DuvidasAdocaoActivity extends AppCompatActivity {

    private RecyclerView rv_duvidas;
    private TextView tv_pergunta;
    private final String TAG = "teste";
    private GroupAdapter adapter;
    private ArrayList<Duvida> duvidas = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duvidas_adocao);

        tv_pergunta = findViewById(R.id.tv_pergunta);

        /*PREENCHENDO A RECYCLER VIEW COM DADOS DE DÚVIDAS*/
        rv_duvidas = findViewById(R.id.rv_duvidas_adocao);
        adapter = new GroupAdapter();
        rv_duvidas.setLayoutManager(new LinearLayoutManager(this));
        rv_duvidas.setAdapter(adapter);
        fetchDuvidas();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {

//                ENCAMINHANDO DUVIDA
                        Intent intent = new Intent(DuvidasAdocaoActivity.this, RespostasAdocaoActivity.class);
                        DuvidasAdocaoActivity.DuvidaItem duvidaItem = (DuvidasAdocaoActivity.DuvidaItem) item;
                       intent.putExtra("duvida", duvidaItem.duvida);
                        startActivity(intent);
                       Log.d(TAG, duvidaItem.duvida.getPergunta());
                    }
                });
    }

    private class DuvidaItem extends Item<ViewHolder> {

        private final Duvida duvida;

        private DuvidaItem(Duvida duvida) {
            this.duvida = duvida;
        }

        //CONECTANDO AOS OBJETOS PARA PODER EDITAR SEUS VALORES

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

            // MANIPULA OBJETO TEXT VIEW DA LISTA
            TextView pergunta = viewHolder.itemView.findViewById(R.id.tv_pergunta);


//            CARREGANDO DEMAIS ATRIBUTOS AO LAYOUT
            pergunta.setText(duvida.getPergunta());
        }

        @Override
        public int getLayout() {
            return R.layout.item_duvida;
        }
    }

    private void fetchDuvidas() {

        //CRIA UMA REFERENCIA PARA A COLEÇÃO DE ANUNCIOS
        FirebaseFirestore.getInstance().collection("/duvidas")
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
                            Duvida duvida = doc.toObject(Duvida.class);
                            duvidas.add(duvida);
                            Log.d(TAG, duvidas.size() + "  tamanho lista");
//                            CARREGA O ANÚNCIO PARA A RECYCLER VIEW
                            adapter.add(new DuvidaItem(duvida));
                        }
                    }
                });
    }
}
