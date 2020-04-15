package com.example.a4patasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4patasapp.model.Anuncio;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.CropSquareTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DetalharAnuncioActivity extends AppCompatActivity {
    private GroupAdapter adapter;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private String TAG = "teste";
    private String codigoRecebido;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhar_anuncio);


        // MANIPULA OBJETO TEXT VIEW
        TextView titulo = findViewById(R.id.lb_titulo_detalhe);
        TextView codigo = findViewById(R.id.resp_cod_detalhe);
        TextView tipoAnimal = findViewById(R.id.resp_tipo_detalhe);
        TextView sexo = findViewById(R.id.resp_sexo_detalhe);
        TextView porte = findViewById(R.id.resp_porte_detalhe);
        TextView idade = findViewById(R.id.resp_idade_detalhe);
        TextView cidade = findViewById(R.id.resp_cidade_detalhe);
        TextView estado = findViewById(R.id.resp_estado_detalhe);
        TextView descricao = findViewById(R.id.resp_descricao);
        TextView email = findViewById(R.id.resp_email_detalhe);
        TextView fone = findViewById(R.id.resp_telefone_detalhe);
        TextView data = findViewById(R.id.resp_data_detalhe);
        ImageView img = findViewById(R.id.image_detalhe);

        //            ANUNCIO RECEBIDO DA CLASSE MAIN
        Anuncio anuncioRecebido = getIntent().getExtras().getParcelable("anuncio");

        //            CARREGANDO A IMAGEM DO ANUNCIO
       // Picasso.get().load(anuncioRecebido.getImagem()).into(img);

        Picasso.get()
                .load(anuncioRecebido.getImagem())
                .resize(450, 340)
                .transform(new RoundedCornersTransformation(22,22))
                .into(img);


//            CARREGANDO DEMAIS ATRIBUTOS AO LAYOUT
        titulo.setText(anuncioRecebido.getTitulo());
        codigo.setText(anuncioRecebido.getCodAnuncio());
        tipoAnimal.setText(anuncioRecebido.getTipoAnimal());
        sexo.setText(anuncioRecebido.getSexo());
        porte.setText(anuncioRecebido.getPorte());
        idade.setText(anuncioRecebido.getIdade());
        cidade.setText(anuncioRecebido.getCidade());
        estado.setText(anuncioRecebido.getEstado());
        descricao.setText(anuncioRecebido.getDescricao());
        email.setText(anuncioRecebido.getEmail().toLowerCase());
        fone.setText(anuncioRecebido.getTelefone());
        data.setText(anuncioRecebido.getDataCriacao());
    }
}

