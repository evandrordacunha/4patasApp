package com.example.a4patasapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;

import com.example.a4patasapp.model.Anuncio;
import com.example.a4patasapp.model.Category;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_LOCATION = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView rv;
    private FusedLocationProviderClient client;
    private String latitude;
    private String longitude;
    private List<Anuncio> anuncios = new ArrayList<>();
    private List<Category> categorias;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //        INSTANCIANDO CLIENT
        client = LocationServices.getFusedLocationProviderClient(this);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

//        ###################################################
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        RECYCLER VIEW QUE RECEBE ANUNCIOS



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
        if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {



        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(getApplicationContext(), AnunciarActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_alerta) {


        } else if (id == R.id.nav_ongs) {


        } else if (id == R.id.nav_desaparecidos) {



        } else if (id == R.id.nav_parceiros) {



        } else if (id == R.id.nav_doacao_app) {


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                Log.d("teste", "showDialog");
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
                Log.d("teste", "Google play services tá ok");
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
                    Log.i("teste", location.getLatitude() + " " + location.getLongitude());
                    latitude = "" + location.getLatitude();
                    longitude = "" + location.getLongitude();
                } else {
                    Log.e("teste", "Localização está nula!");
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
                        Log.i("teste", locationSettingsResponse
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
                if(locationResult ==null){
                    Log.i("teste","Localização nula");
                    return;
                }
                for(Location location :locationResult.getLocations()){
                    Log.i("teste",location.getLatitude() +"");
                        String latitudeAtualizada = location.getLatitude()+"";
                        String longitudeAtualziada = location.getLongitude()+"";
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                //AVISA SE A LOCALIZAÇÃO ESTÁ OU NÃO DISPONIVEL
                Log.i("teste", locationAvailability.isLocationAvailable() + "");
            }
        };
        client.requestLocationUpdates(locationRequest, locationCallback, null);

    }

    private void fetchAnuncios() {

        //CRIA UMA REFERENCIA PARA A COLEÇÃO DE ANUNCIOS
        FirebaseFirestore.getInstance().collection("/anuncios")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        //VERIFICANDO SE ENCONTROU ALGUMA EXCEÇÃO CAPAZ DE IMPEDIR A EXECUÇÃO, CASO ENCONTRE, PARE A APLICAÇÃO
                        if (e != null) {
                            Log.e("TESTE", "Erro: ", e);
                            return;
                        }

                        //REFERÊNCIA PARA TODOS POSTOS DA BASE
                        List<DocumentSnapshot> documentos = queryDocumentSnapshots.getDocuments();
                        int totalAnuncios = queryDocumentSnapshots.getDocuments().size();


                        for (DocumentSnapshot doc : documentos) {
                            Anuncio anuncio = doc.toObject(Anuncio.class);


                            Log.d("TESTE", anuncio.getTitulo() + "encontrado!");
                            anuncios.add(anuncio);
                        }
                    }
                });
    }


}
