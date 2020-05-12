package com.example.a4patasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
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
    private Button bt_whats;
    private Button bt_email;
    private Button bt_telefonar;
    private Button bt_home_detalhar;
    private  Anuncio anuncioRecebido;


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
        bt_whats = findViewById(R.id.bt_whats);
        bt_email = findViewById(R.id.bt_email_detalhar);
        bt_telefonar = findViewById(R.id.bt_telefonar_detalhar);
        bt_home_detalhar = findViewById(R.id.bt_home_detalhar);

        bt_home_detalhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalharAnuncioActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //            ANUNCIO RECEBIDO DA CLASSE MAIN
        anuncioRecebido = getIntent().getExtras().getParcelable("anuncio");

        //ACTION BOTÃO WHATSAPP ENVIANDO MENSAGEM DIRETA PARA O WHATS CADASTRADO
        bt_whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://api.whatsapp.com/send?1=pt_BR&phone=55"+anuncioRecebido.getTelefone()));
                startActivity(intent2);
            }
        });

//        ACTION BOTÃO E-MAIL ENVIANDO EMAIL PARA O CONTATO

        bt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] remetente = {anuncioRecebido.getEmail()};
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL,remetente);
                email.setType("message/rfc822");
                startActivity(email);
            }
        });

//        ACTION BOTÃO TELEFONE LIGANDO PARA O CONTATO

        bt_telefonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel: " +anuncioRecebido.getTelefone());
                Intent telefonar = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(telefonar);

            }
        });

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

