package com.example.a4patasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4patasapp.model.Anuncio;
import com.example.a4patasapp.model.Mensagem;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class AnunciosProximosActivity extends AppCompatActivity {

    private final String TAG = "teste";
    private ArrayList<Anuncio> anuncios;
    private RecyclerView rv_anunciosProximos;
    private GroupAdapter adapter;
    private Button bt_refazer_pesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios_proximos);


//        RECEBENDO ANUNCIOS FILTRADOS COMO SENDO PRÓXIMOS AO USUÁRIO
        anuncios = getIntent().getParcelableArrayListExtra("anunciosProximos");

        if(anuncios.size() == 0){
            Intent intent = new Intent(AnunciosProximosActivity.this,MensagemActivity.class);
            startActivity(intent);
        }

        Log.d(TAG, "TOTAL DE ANUNCIOS RECEBIDOS=  " + anuncios.size());
        rv_anunciosProximos = findViewById(R.id.rv_anuncios_proximos);
        adapter = new GroupAdapter();
        popularRecycler(anuncios);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                //                ENCAMINHANDO ANUNCIO PARA A ACTIVITY DetalharAnuncioActivity
                Intent intent = new Intent(AnunciosProximosActivity.this, DetalharAnuncioActivity.class);
                AnunciosProximosActivity.AnuncioItem anuncioItem = (AnunciosProximosActivity.AnuncioItem) item;
                intent.putExtra("anuncio", anuncioItem.anuncio);
                startActivity(intent);
                Log.d(TAG, anuncioItem.anuncio.getCodAnuncio());
            }
        });
    }

    private class AnuncioItem extends Item<ViewHolder> {

        private final Anuncio anuncio;

        private AnuncioItem(Anuncio anuncio) {
            this.anuncio = anuncio;
        }

        //CONECTANDO AOS OBJETOS PARA PODER EDITAR SEUS VALORES

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

            // MANIPULA OBJETO TEXT VIEW DA LISTA
            TextView titulo = viewHolder.itemView.findViewById(R.id.lb_titulo_item);
            TextView codigo = viewHolder.itemView.findViewById(R.id.resp_cod_item);
            TextView tipoAnimal = viewHolder.itemView.findViewById(R.id.resp_tipo_item);
            TextView sexo = viewHolder.itemView.findViewById(R.id.resp_sexo_item);
            TextView porte = viewHolder.itemView.findViewById(R.id.resp_porte_item);
            TextView idade = viewHolder.itemView.findViewById(R.id.resp_idade_item);
            TextView cidade = viewHolder.itemView.findViewById(R.id.resp_cidade_item);
            TextView estado = viewHolder.itemView.findViewById(R.id.resp_estado_item);
            ImageView img = viewHolder.itemView.findViewById(R.id.image_item);


//            CARREGANDO A IMAGEM DO ANUNCIO JÁ COM BORDAS ARREDONDADAS
            Picasso.get()
                    .load(anuncio.getImagem())
                    .resize(900, 540)
                    .transform(new RoundedCornersTransformation(22, 22))
                    .into(img);

//            CARREGANDO DEMAIS ATRIBUTOS AO LAYOUT
            titulo.setText(anuncio.getTitulo());
            codigo.setText(anuncio.getCodAnuncio());
            tipoAnimal.setText(anuncio.getTipoAnimal());
            sexo.setText(anuncio.getSexo());
            porte.setText(anuncio.getPorte());
            idade.setText(anuncio.getIdade());
            cidade.setText(anuncio.getCidade());
            estado.setText(anuncio.getEstado());
        }

        @Override
        public int getLayout() {
            return R.layout.item_anuncio;
        }
    }


    /*MÉTODO RESPONSÁVEL POR POPULAR A RECYCLER VIEW*/
    private void popularRecycler(ArrayList<Anuncio> anuncios) {
        Anuncio a = null;
        for (int i = 0; i < anuncios.size(); i++) {
            a = anuncios.get(i);
            Log.d(TAG, "ANUNCIO " + i + a.getCodAnuncio());
            adapter.add(new AnuncioItem(a));
        }
        rv_anunciosProximos.setLayoutManager(new LinearLayoutManager(this));
        rv_anunciosProximos.setAdapter(adapter);
    }
}
