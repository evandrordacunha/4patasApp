package com.example.a4patasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MensagemActivity extends AppCompatActivity {

    private Button bt_refazer_pesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        bt_refazer_pesquisa = findViewById(R.id.bt_refazer_pesquisa);
        bt_refazer_pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MensagemActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });

    }
}
