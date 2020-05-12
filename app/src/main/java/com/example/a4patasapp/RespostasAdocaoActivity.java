package com.example.a4patasapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.a4patasapp.model.Anuncio;
import com.example.a4patasapp.model.Duvida;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RespostasAdocaoActivity extends AppCompatActivity {

    private TextView resposta;
    private Duvida duvidaRecebida;
    private final String TAG = "teste";
    private ArrayList<Duvida> duvidas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respostas_adocao);

        resposta = findViewById(R.id.resp_resposta_duvida);
        duvidaRecebida =  getIntent().getExtras().getParcelable("duvida");
        resposta.setText(duvidaRecebida.getResposta());
    }
}
