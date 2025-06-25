package com.example.tesouro_azul_app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tesouro_azul_app.R;

public class MainActivity extends AppCompatActivity {

    CardView ProdutoCard, graficosCard, MetasCard, configCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProdutoCard = findViewById(R.id.ProdutoCard);
        graficosCard = findViewById(R.id.graficosCard);
        configCard = findViewById(R.id.configCard);

        ProdutoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProdutosActivity.class);
                startActivity(intent);
            }
        });

        graficosCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GraficosActivity.class);
                startActivity(intent);
            }
        });

        configCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "erro ao ir para config", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

