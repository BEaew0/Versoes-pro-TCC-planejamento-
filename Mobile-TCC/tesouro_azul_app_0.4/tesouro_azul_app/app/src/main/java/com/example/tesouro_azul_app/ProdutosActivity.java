package com.example.tesouro_azul_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProdutosActivity extends AppCompatActivity {

    private String Host="...";
    private String url,ret;

    private static String nomeProd,valorProd,tipoProd,quantProd,valProd,codProd;

    EditText NomeProd,ValorProd,TipoProd,QuantProd,ValProd,CodProd;
    Button btnVenderProd, btnAdicionarProd, btnAlterarProd, btnEstoque;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtosctivity);

        btnVenderProd = findViewById(R.id.btnVenderProd);
        btnAdicionarProd = findViewById(R.id.btnAdicionarProd);
        btnAlterarProd = findViewById(R.id.btnAlterarProd);
        btnEstoque = findViewById(R.id.btnEstoque);

        NomeProd = (EditText) findViewById(R.id.txtNomeProd) ;
        ValorProd = (EditText) findViewById(R.id.txtValorProd);
        TipoProd = (EditText) findViewById(R.id.txtTipo);
        QuantProd = (EditText) findViewById(R.id.txtQuant);
        ValProd = (EditText) findViewById(R.id.txtValidade);
        CodProd = (EditText) findViewById(R.id.txtCodProd);

        // Configura Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://seuservidor.com/")// <- Coloque a URL base da sua API aqui
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Ações para os botões
       btnVenderProd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

        btnAdicionarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para o botão "Adicionar"
                try {
                    adcionarProd();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
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

    private void  adcionarProd() throws ParseException
    {
        String NOME_PRODUTO = NomeProd.getText().toString().trim();
        String TIPO_PRODUTO = ValorProd.getText().toString().trim();
        String DATA_VAL = ValProd.getText().toString().trim();
        String COD_PRODUTO = CodProd.getText().toString().trim();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date DATA_VAL_PROD = null;

        //converte de string para data
        DATA_VAL_PROD = formato.parse(DATA_VAL);

            ProdutoObject produtoObject = new ProdutoObject(COD_PRODUTO,NOME_PRODUTO,TIPO_PRODUTO,DATA_VAL_PROD);

            //Conversão para JSON
            Gson gson = new Gson();
            String json = gson.toJson(produtoObject);

            Toast.makeText(ProdutosActivity.this, json, Toast.LENGTH_SHORT).show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://suaapi.com.br/") // coloque a URL base da sua API
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            // Crie uma instância da interface da API
            ApiService apiService = retrofit.create(ApiService.class);


            Call<Void> call = apiService.criarProduto(produtoObject);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProdutosActivity.this, "Usuário enviado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProdutosActivity.this, "Erro ao enviar: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ProdutosActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
