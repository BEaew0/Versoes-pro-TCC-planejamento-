package com.example.tesouro_azul_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ProdutosActivity extends AppCompatActivity {

    ImageView Leave;
    Button btnVenderProd, btnAdicionarProd, btnAlterarProd, btnEstoque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtosctivity);

        Leave = (ImageView) findViewById(R.id.Leave);

        btnVenderProd = findViewById(R.id.btnVenderProd);
        btnAdicionarProd = findViewById(R.id.btnAdicionarProd);
        btnAlterarProd = findViewById(R.id.btnAlterarProd);
        btnEstoque = findViewById(R.id.btnEstoque);

        // Ações para os botões
       btnVenderProd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

       Leave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ProdutosActivity.this, MainActivity.class);
               startActivity(intent);
           }
       });

        btnAdicionarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para o botão "Adicionar"

            }
        });

        btnAlterarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para o botão "Alterar"
            }
        });

        btnEstoque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a tela de estoque

            }
        });
    }
}