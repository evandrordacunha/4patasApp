package com.example.a4patasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    private ProgressDialog pBar;


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
                //ProgressBar
                pBar = new ProgressDialog(FiltrarBuscaActivity.this);
                pBar.setCancelable(true);
                pBar.setMessage("Carregando... ");
                pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pBar.show();

                raio = findViewById(R.id.resp_distancia_filtro);

                if(raio.getText().toString().length()==0 || raio.getText().toString().equalsIgnoreCase("")){
                    raioRecebido = 0.1;
                }else {
                    raioRecebido = Double.parseDouble(raio.getText().toString());
                }
                Log.d(TAG, "RAIO INFORMADO =  " + raioRecebido);
                Log.d(TAG, "Raio Recebido =  " + raioRecebido);

                /* MACHO - FILHOTE - PEQUENO - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* MACHO - FILHOTE - PEQUENO - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* MACHO - FILHOTE - PEQUENO - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* MACHO - FILHOTE - MEDIO - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* MACHO - FILHOTE - MEDIO - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* MACHO - FILHOTE - MEDIO - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* MACHO - FILHOTE - GRANDE - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* MACHO - FILHOTE - GRANDE - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* MACHO - FILHOTE - GRANDE - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }
                /*####################   ADULTOS E MACHOS   #####################################*/
                /* MACHO - ADULTO - PEQUENO - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* MACHO - ADULTO - PEQUENO - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* MACHO - ADULTO - PEQUENO - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* MACHO - ADULTO - MEDIO - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* MACHO - ADULTO - MEDIO - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* MACHO - ADULTO - MEDIO - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* MACHO - ADULTO - GRANDE - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* MACHO - ADULTO - GRANDE - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* MACHO - ADULTO - GRANDE - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_macho_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("MACHO") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* ######################### COMEÇA AS FEMEAS ###############################################*/

                /* FEMEA - FILHOTE - PEQUENO - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* FEMEA - FILHOTE - PEQUENO - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* FEMEA - FILHOTE - PEQUENO - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* FEMEA - FILHOTE - MEDIO - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* FEMEA - FILHOTE - MEDIO - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* FEMEA - FILHOTE - MEDIO - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* FEMEA - FILHOTE - GRANDE - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* FEMEA - FILHOTE - GRANDE - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* FEMEA - FILHOTE - GRANDE - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_filhote_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("FILHOTE")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }
                /*####################   ADULTOS E FEMEAS   #####################################*/
                /* FEMEA - ADULTO - PEQUENO - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* FEMEA - ADULTO - PEQUENO - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* FEMEA - ADULTO - PEQUENO - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_pequeno_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("PEQUENO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* FEMEA - ADULTO - MEDIO - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* FEMEA - ADULTO - MEDIO - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* FEMEA - ADULTO - MEDIO - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_medio_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("MEDIO") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }


                /* FEMEA - ADULTO - GRANDE - CACHORRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_cachorro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("CACHORRO(A)")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* FEMEA - ADULTO - GRANDE - GATO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_gato_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("GATO(A)")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }

                /* FEMEA - ADULTO - GRANDE - OUTRO*/
                if (rd_group_sexo_filtro.getCheckedRadioButtonId() == R.id.rb_femea_filtro &&
                        rd_group_tipo_filtro.getCheckedRadioButtonId() == R.id.rb_outro_filtro &&
                        rd_group_porte_filtro.getCheckedRadioButtonId() == R.id.rb_porte_grande_filtro &&
                        rd_group_idade_filtro.getCheckedRadioButtonId() == R.id.rb_adulto_filtro) {
                    /*VARRER ANUNCIOS RECEBIDOS*/
                    Anuncio a = null;
                    Log.d(TAG, "ENTRANDO NO LAÇO COM ANUNCIOS =" + anunciosRecebidos.size());
                    for (int i = 0; i < anunciosRecebidos.size(); i++) {
                        a = anunciosRecebidos.get(i);
                        double lat = Double.parseDouble(a.getLatitude());
                        double longt = Double.parseDouble(a.getLongitude());
                        double latUser = Double.parseDouble(latitudeUser);
                        double longtUser = Double.parseDouble(longitudeUser);

                        if (a.getSexo().equalsIgnoreCase("FEMEA") && a.getTipoAnimal().equalsIgnoreCase("OUTRO")
                                && a.getPorte().equalsIgnoreCase("GRANDE") && a.getIdade().equalsIgnoreCase("ADULTO")) {
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
                    Log.d(TAG, "ANUNCOS ENVIADOS=  " + anunciosProximos.size());
                    startActivity(intent);
                }
                /*#### FIM FILTROS  ####*/
            }
        });

    }
}