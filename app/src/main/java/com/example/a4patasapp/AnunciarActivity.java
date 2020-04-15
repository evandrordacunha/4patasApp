package com.example.a4patasapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a4patasapp.model.Anuncio;
import com.example.a4patasapp.model.Usuario;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnunciarActivity extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_LOCATION = 1;
    private Button bt_foto;
    private Button bt_anunciar;
    private EditText edit_Titulo;
    private EditText edit_Descricao;
    private EditText edit_email;
    private EditText edit_telefone;
    private String sexo;
    private String telefone;
    private String idade;
    private String titulo;
    private String descricao;
    private String email;
    private String tipo_animal;
    private String porte;
    private String dataCriacao;
    private RadioGroup rd_group_sexo;
    private RadioGroup rd_group_tipo;
    private RadioGroup rd_group_porte;
    private RadioGroup rd_group_idade;
    private Uri mSelectUri;
    private ImageView im_foto;
    private String latitude;
    private String longitude;
    private String cidade;
    private String estado;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int totalAnunciosFirebase;
    private final String TAG = "teste";
    private Address address;
    private double dLatitude, dLongitude;
    private String endAprox;
    private LocationManager locationManager;


    //CLIENTE PARA MANIPULACAO DE LOCALIZAÇÃO DO USUARIO
    FusedLocationProviderClient client;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anunciar);


        //        INSTANCIANDO CLIENT
        client = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
        buscarEndereco(location);

//        INICIALIZANDO VARIAVEIS

        im_foto = findViewById(R.id.img_foto_anuncio);
        bt_foto = findViewById(R.id.bt_foto_anuncio);

//        SELECIONANDO A FOTO DO ANUNCIO
        bt_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });
        bt_anunciar = findViewById(R.id.bt_anunciar);
        edit_Descricao = findViewById(R.id.edit_descricao_anuncio);
        edit_email = findViewById(R.id.edit_email_anuncio);
        edit_telefone = findViewById(R.id.edit_telefone_anuncio);
        edit_Titulo = findViewById(R.id.edit_titulo_anuncio);

        bt_anunciar = findViewById(R.id.bt_anunciar);
        bt_foto = findViewById(R.id.bt_foto_anuncio);
        rd_group_sexo = findViewById(R.id.rg_group_sexo);
        rd_group_tipo = findViewById(R.id.rg_tipo_animal);
        rd_group_idade = findViewById(R.id.rg_group_idade);
        rd_group_porte = findViewById(R.id.rg_porte);
        bt_anunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAnuncio();
            }
        });

    }

    //    TRATANDO A CONVERSAO DA IMAGEM PARA SER CARREGADA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            mSelectUri = data.getData();
            //BITMAP VAI RECEBER A IMAGEM
            Bitmap bitmap = null;
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectUri);
                im_foto.setImageDrawable(new BitmapDrawable(bitmap));
                bt_foto.setAlpha(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //    SELETOR DE FOTO
    private void selecionarFoto() {
        //ABRINDO A GALERIA DE FOTOS
        Intent intent = new Intent(Intent.ACTION_PICK);
        //TIPO DE DADOS QUE VOU PERMITIR A INSERÇÃO
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    //    MÉTODO RESPONSÁVEL POR CADASTRAR ANÚNCIO NO FIREBASE
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void criarAnuncio() {

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
                latitude = "" + location.getLatitude();
                longitude = "" + location.getLongitude();

                if (location != null) {
                    Log.i("teste", "Latitude: " + latitude + " " + "Longitude: " + longitude);
                } else {
                    Log.e("teste", "Localização está nula!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });


//                PEGANDO DADOS DE TÍTULO DO ANUNCIO
        titulo = edit_Titulo.getText().toString().toUpperCase();
        if (titulo == null || titulo.isEmpty()) {
            Toast.makeText(AnunciarActivity.this, "Campo TÍTULO é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (titulo.length() > 30) {
            Toast.makeText(AnunciarActivity.this, "Campo TÍTULO deve ter no máximo 30 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

//                PEGANDO DADOS DE DESCRICAO DO ANUNCIO
        descricao = edit_Descricao.getText().toString();
        if (descricao == null || descricao.isEmpty()) {
            Toast.makeText(AnunciarActivity.this, "Campo DESCRIÇÃO é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (descricao .length()>300) {
            Toast.makeText(AnunciarActivity.this, "Campo DESCRIÇÃO deve ter no máximo 300 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

//                PEGANDO DADOS DE TELEFONE DO ANUNCIO
        telefone = edit_telefone.getText().toString().toUpperCase().trim();
        if (telefone == null || telefone.isEmpty()) {
            Toast.makeText(AnunciarActivity.this, "Campo TELEFONE é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (telefone.length() <10) {
            Toast.makeText(AnunciarActivity.this, "Você deve informar o DDD junto ao número do telefone!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (telefone.length() >12) {
            Toast.makeText(AnunciarActivity.this, "Telefone deve ter de 10 a 12 dígitos com o DDD incluso!", Toast.LENGTH_SHORT).show();
            return;
        }

//                PEGANDO DADOS DE E-MAIL DE CONTATO
        email = edit_email.getText().toString().toUpperCase();
        //GARANTINDO PREENCHIMENTO DOS DADOS OBRIGATÓRIOS E VALIDAÇÕES DE FORMULÁRIO
        if (email == null || email.isEmpty()) {
            Toast.makeText(AnunciarActivity.this, "Campo EMAIL é obrigatório e deve ser informado!", Toast.LENGTH_SHORT).show();
            return;
        }

//                MANIPULANDO RADIO BUTTON
        switch (rd_group_sexo.getCheckedRadioButtonId()) {
            case R.id.rb_macho:
                sexo = "MACHO";
                break;
            case R.id.rb_femea:
                sexo = "FEMEA";
                break;
        }
        switch (rd_group_idade.getCheckedRadioButtonId()) {
            case R.id.rb_adulto:
                idade = "ADULTO";
                break;
            case R.id.rb_filhote:
                idade = "FILHOTE";
                break;
        }
        switch (rd_group_tipo.getCheckedRadioButtonId()) {
            case R.id.rb_cachorro:
                tipo_animal = "CACHORRO(A)";
                break;
            case R.id.rb_gato:
                tipo_animal = "GATO(A)";
                break;
        }
        switch (rd_group_porte.getCheckedRadioButtonId()) {
            case R.id.rb_porte_pequeno:
                porte = "PEQUENO";
                break;
            case R.id.rb_porte_medio:
                porte = "MEDIO";
                break;
            case R.id.rb_porte_grande:
                porte = "GRANDE";
                break;
        }


        //CRIANDO A HASH PARA REFERÊNCIA DO NOME DO ARQUIVO
        String fileName = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/fotos/" + fileName);

        //SUBINDO A FOTO PARA A PASTA IMAGES DO FIREBASE
        ref.putFile(mSelectUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //PEGA A REFERÊNCIA DA URL ONDE ESTÁ HOSPEDADO NO FIREBASE A IMAGEM DEPOIS DE ELA TER SUBIDO PARA QUE EU POSSA TRABALHAR COM ELA
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i("URI = ", uri.toString());

                                String profileUrl = uri.toString();
                                //ANÚNCIO A SER REGISTRADO
                                // data/hora atual
                                LocalDateTime agora = LocalDateTime.now();
                                DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                                String dataFormatada = formatterData.format(agora).replaceAll("/", "").trim();

                                DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss");
                                String horaFormatada = formatterHora.format(agora).replaceAll(":", "").trim();

                                final String codAnuncio = dataFormatada + horaFormatada;
                                dataCriacao = formatterData.format(agora);

                                final Anuncio anuncio = new Anuncio(codAnuncio,dataCriacao,titulo,descricao,telefone,email,
                                        sexo,tipo_animal,porte,idade,latitude,longitude,uri.toString(),endAprox,cidade,estado);
                                //CRIANDO A REFERÂNCIA PARA UMA COLEÇÃO DE ANÚNCIOS
                                db.collection("doacoes").document(anuncio.getCodAnuncio())
                                        .set(anuncio)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Anuncio cadastrado com sucesso!");
                                                Toast.makeText(AnunciarActivity.this, "Anúncio " + codAnuncio + " cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(AnunciarActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Erro ao cadastrar anúncio!", e);
                                                Toast.makeText(AnunciarActivity.this, "Falha ao cadastrar anúncio! - Tente novamente.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AnunciarActivity.this, AnunciarActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("###ERRO###", e.getMessage(), e);
                    }
                });
    }

//    DELETAR ANUNCIO


    /*TOTAL DE ANUNCIOS CADASTRADOS*/

    public int getAnuncios() {

        // [START get_multiple_all]
        final ArrayList<String> listaAnuncios = new ArrayList<>();
        db.collection("anuncios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                listaAnuncios.add(document.getId());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });
        // [END get_multiple_all]
        Log.d(TAG, "totalAnuncios" + listaAnuncios.size());
        return listaAnuncios.size();
    }

    @Override
    public void onLocationChanged(Location location) {
        dLatitude = location.getLatitude();
        dLongitude = location.getLongitude();

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

    /*TRADUTO DE LOCALIDADE - CONVERTE LATITUDE E LONGITUDE EM DADOS DE ENDEREÇO */
    public void buscarEndereco(Location location) {
        String endereco = "";
        try {


            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(dLatitude, dLongitude, 1);
            endereco = addresses.get(0).getAddressLine(0) + "/ "
                    + addresses.get(0).getAdminArea() + " - " + addresses.get(0).getCountryCode().toUpperCase();
            cidade = addresses.get(0).getSubAdminArea().toUpperCase();
            estado = addresses.get(0).getAdminArea().toUpperCase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        endAprox = endereco;
    }
}




