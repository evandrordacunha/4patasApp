package com.example.a4patasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.a4patasapp.model.Anuncio;
import com.example.a4patasapp.model.Haversine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FiltrarBuscaActivity extends AppCompatActivity {

    private EditText raio;
    private RadioGroup rd_group_sexo_filtro;
    private RadioGroup rd_group_tipo_filtro;
    private RadioGroup rd_group_idade_filtro;
    private RadioGroup rd_group_porte_filtro;
    private Button bt_filtrar;
    private String latitudeUser;
    private String longitudeUser;
    private double raioRecebido;
    private double raioConvertido;
    private double distanciaHaversine;
    double latUser;
    double longUser;
    double latAnun;
    double longAnun;
    private String latitudeanuncio;
    private String longitudeAnuncio;
    private ArrayList<Anuncio> anunciosRecebidos;
    private ArrayList<Anuncio> anuncios = new ArrayList<>();
    private final String TAG = "teste";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_busca);

//        RECEBENDO DADOS DA MAIN ACTIVITY

        Intent intent = getIntent();
        latitudeUser = intent.getStringExtra("latitude");
        longitudeUser = intent.getStringExtra("longitude");
        anunciosRecebidos = getIntent().getParcelableArrayListExtra("anuncios");
        Log.d(TAG, "Latitude recebida" + latitudeUser);
        Log.d(TAG, "Longitude recebida" + longitudeUser);
        Log.d(TAG, "Lista recebida com total de elementos =" + anunciosRecebidos.size());


//        PEGANDO COMPONENTES DO LAYOUT DA ACTIVITY

        rd_group_sexo_filtro = findViewById(R.id.rg_group_sexo_filtro);
        rd_group_tipo_filtro = findViewById(R.id.rg_group_tipo_animal_filtro);
        rd_group_idade_filtro = findViewById(R.id.rg_group_idade_filtro);
        rd_group_porte_filtro = findViewById(R.id.rg_group_porte_filtro);
        bt_filtrar = findViewById(R.id.bt_filtrar);

        bt_filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Anuncio> anunciosProximos = new ArrayList<>();

                raio = findViewById(R.id.resp_distancia_filtro);
                raioRecebido = Double.parseDouble(raio.getText().toString());
                Log.d(TAG, "RAIO INFORMADO =  " + raioRecebido);
                Log.d(TAG, "Raio Recebido =  " + raioRecebido);

                /* MACHO - FILHOTE - PEQUENO - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG,"ENTRANDO NO LAÇO COM ANUNCIOS =" +anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if(a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                        && a.getPorte().equalsIgnoreCase("PEQUENO")&& a.getIdade().equalsIgnoreCase("FILHOTE")){
                        //if (a.getSexo() == "MACHO" && a.getTipoAnimal() == "CACHORRO(A)" && a.getPorte() == "MEDIO" && a.getIdade() == "FILHOTE") {
                            //                        DISTANCIA ENTRE USUARIO E ANUNCIO
                            double dist = Haversine.distance(latUser, longtUser, lat, longt);
                            Log.d(TAG, "Distância entre 2 pontos =  " + dist);
                            if (dist <= raioRecebido) {
                                anunciosProximos.add(a);
                            }

                        }

                    }

                    //ENCAMINHA LISTA COM ANUNCIOS PRÓXIMOS A MAIN ACTIVITY
                    Intent intent = new Intent(getApplicationContext(), AnunciosProximosActivity.class);
                    intent.putParcelableArrayListExtra("anunciosProximos", anunciosProximos);
                    Log.d(TAG,"ANUNCOS ENVIADOS=  "+anunciosProximos.size());
                    startActivity(intent);
                }
            }

        });

    }
}


