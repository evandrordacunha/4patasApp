package com.example.a4patasapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4patasapp.model.Anuncio;
import com.example.a4patasapp.model.Usuario;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private static final int REQUEST_LOCATION = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView rv;
    private FusedLocationProviderClient client;
    private String latitude;
    private String longitude;
    private String emailLogado;
    private ArrayList<Anuncio> anuncios = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private final String TAG = "teste";
    private double latitudeUser;
    private double longitudeUser;
    private GroupAdapter adapter;
    private FirebaseAuth mAuth;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        PEGANDO USUÁRIO CONECTADO
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        emailLogado  = currentUser.getEmail();
        fetchAnuncios();
        fetchUsuarios();

        //        INSTANCIANDO CLIENT
        client = LocationServices.getFusedLocationProviderClient(this);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

//        ###################################################
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        RECYCLER VIEW QUE RECEBE ANUNCIOS

        rv = findViewById(R.id.rv_anuncios);
        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
//                ENCAMINHANDO ANUNCIO PARA A ACTIVITY DetalharAnuncioActivity
                Intent intent = new Intent(MainActivity.this, DetalharAnuncioActivity.class);
                AnuncioItem anuncioItem = (AnuncioItem) item;
                intent.putExtra("anuncio", anuncioItem.anuncio);
                startActivity(intent);
                Log.d(TAG, anuncioItem.anuncio.getCodAnuncio());
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AnunciarActivity.class);
                startActivity(intent);
            }
        });

    }

    /*
        CRIANDO MENU
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
        GERENCIANDO BOTÃO DE SETTINGS
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //    CONFIGURANDO LINKS DO MENU
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //ITEM 1 DO MENU É DIRECIONADO PARA A ACTIVITY INSTITUCIONAL
        if(id==R.id.nav_perfil){
            Intent intent = new Intent(MainActivity.this,AreaUsuarioActivity.class);
            intent.putExtra("emailLogado",emailLogado );
            intent.putParcelableArrayListExtra("anuncios",anuncios);
            startActivity(intent);
        }else if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            //ENCAMINHANDO ANUNCIO PARA A ACTIVITY DetalharAnuncioActivity
            Intent intent = new Intent(getApplicationContext(), FiltrarBuscaActivity.class);
            String latUser = ""+latitudeUser;
            String longUser = ""+longitudeUser;

            intent.putExtra("latitude", latUser);
            Log.d(TAG,"ENVIANDO LATITUDE: "+latUser);
            intent.putExtra("longitude", longUser);
            Log.d(TAG,"ENVIANDO LONGITUDE: "+longUser);
            intent.putParcelableArrayListExtra("anuncios",anuncios);
            Log.d(TAG,"Lista envia com tamanho " +anuncios.size());
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(getApplicationContext(), AnunciarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ongs) {

        } else if (id == R.id.nav_comunicar_desaparecimento) {
            Intent intent = new Intent(MainActivity.this,DesaparecidosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mural_desaparecidos) {
            Intent intent = new Intent(MainActivity.this,ListarDesaparecidosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_duvidas) {
            Intent intent = new Intent(MainActivity.this,DuvidasAdocaoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_parceiros) {

        } else if (id == R.id.nav_doacao_app) {
            Intent intent = new Intent(MainActivity.this, DoacaoAppActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
        GERENCIANDO BOTÃO VOLTAR
     */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                Log.d(TAG, "showDialog");
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
                Log.d(TAG, "Google play services tá ok");
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
                if (location != null) {
                    Log.i(TAG, location.getLatitude() + " " + location.getLongitude());
                    latitudeUser = location.getLatitude();
                    longitudeUser = location.getLongitude();
                    latitude = "" + location.getLatitude();
                    longitude = "" + location.getLongitude();
                } else {
                    Log.e(TAG, "Localização está nula!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });
//        ATUALIZANDO LOCALIZAÇÕES EM TEMPO REAL A CADA 3 MIN
        LocationRequest locationRequest = LocationRequest.create();
//        DE QUANTOS EM QUANTOS SEGUNDOS ATUALIZA A POSIÇÃO?
        locationRequest.setInterval(180 * 1000);
//        SE OUTRO APLICATIVO TB ESTIVER USANDO LOCATION ENTÃO COMPARTILHA LOCALIZAÇÃO COMIGO A CADA 60 SEG
        locationRequest.setFastestInterval(60 * 1000);
//         BALANCEAR TANTO A PRECISÃO QUANTO O USO DA BATERIA
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

//    VERIFICAR SE AS LOCALIZAÇÕES ESTÃO CORRETAS
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, locationSettingsResponse
                                .getLocationSettingsStates().isNetworkLocationPresent() + "");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(MainActivity.this, 10);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


//    LISTENER FICA ESCUTANDO AS POSIÇÕES NOVAS VINDAS DO PROVIDER PARA ATUALIZAÇÃO DE LOCALIZAÇÃO
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i(TAG, "Localização nula");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.i(TAG, location.getLatitude() + "");
                    latitudeUser = location.getLatitude();
                    longitudeUser = location.getLongitude();
                    String latitudeAtualizada = location.getLatitude() + "";
                    String longitudeAtualizada = location.getLongitude() + "";

                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                //AVISA SE A LOCALIZAÇÃO ESTÁ OU NÃO DISPONIVEL
                Log.i(TAG, locationAvailability.isLocationAvailable() + "");
            }
        };
        client.requestLocationUpdates(locationRequest, locationCallback, null);

    }

    private void fetchAnuncios() {

        //CRIA UMA REFERENCIA PARA A COLEÇÃO DE ANUNCIOS
        FirebaseFirestore.getInstance().collection("/doacoes")
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
                            Anuncio anuncio = doc.toObject(Anuncio.class);
                            anuncios.add(anuncio);
                            Log.d(TAG,anuncios.size()+"  tamanho lista");
//                            CARREGA O ANÚNCIO PARA A RECYCLER VIEW
                            adapter.add(new AnuncioItem(anuncio));
                        }
                    }
                });
    }

    private void fetchUsuarios() {
        //CRIA UMA REFERENCIA PARA A COLEÇÃO DE USUARIOS
        FirebaseFirestore.getInstance().collection("/usuarios")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        //VERIFICANDO SE ENCONTROU ALGUMA EXCEÇÃO CAPAZ DE IMPEDIR A EXECUÇÃO, CASO ENCONTRE, PARE A APLICAÇÃO
                        if (e != null) {
                            Log.e(TAG, "Erro: ", e);
                            return;
                        }
                        //REFERÊNCIA PARA TODOS USUARIOS DA BASE
                        List<DocumentSnapshot> documentos = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot doc : documentos) {
//                            SERIALIZANDO O ANUNCIO VINDO DO JSON - FIREBASE
                            Usuario usuario = doc.toObject(Usuario.class);
                            usuarios.add(usuario);
                        }
                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {
        latitudeUser =location.getLatitude();
        longitudeUser = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
            TextView data = viewHolder.itemView.findViewById(R.id.resp_data_item);
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
            data.setText(anuncio.getDataCriacao());
        }

        @Override
        public int getLayout() {
            return R.layout.item_anuncio;
        }
    }


    /*CLASE USADA PARA AUXILIAR O ENCAMPSULAMENTO DO OBJETO USER A SER ENVIADO A CLASSE MAIN PARA USO DAS COORDENADAS*/


}