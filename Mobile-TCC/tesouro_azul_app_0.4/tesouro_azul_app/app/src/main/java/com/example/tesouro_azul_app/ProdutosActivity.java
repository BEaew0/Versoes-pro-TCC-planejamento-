package com.example.tesouro_azul_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProdutosActivity extends AppCompatActivity {

    private String Host="...";
    private String url,ret;
    private static final int REQUEST_CODE_GALLERY = 1001;
    private static final int PICK_IMAGE = 1;

    private static String fotoProd;

    private RecyclerView recyclerView;
    private ProdutoAdapter adapter;
    private List<SuperClassProd.ProdutoGet> listaProdutos = new ArrayList<>();

    EditText NomeProd,ValorProd,TipoProd,QuantProd,ValProd,CodProd;
    Button btnVenderProd, btnAdicionarProd, btnAlterarProd, btnEstoque;
    ShapeableImageView prodImage;
    ProgressBar progressBar;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtosctivity);

        btnVenderProd = findViewById(R.id.btnVenderProd);
        btnAdicionarProd = findViewById(R.id.btnAdicionarProd);
        btnAlterarProd = findViewById(R.id.btnAlterarProd);
        prodImage = findViewById(R.id.prod_image);
        recyclerView = findViewById(R.id.recyclerViewProdutos);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        NomeProd = (EditText) findViewById(R.id.txtNomeProd) ;
        ValorProd = (EditText) findViewById(R.id.txtValorProd);
        TipoProd = (EditText) findViewById(R.id.txtTipo);
        QuantProd = (EditText) findViewById(R.id.txtQuant);
        ValProd = (EditText) findViewById(R.id.txtValidade);
        CodProd = (EditText) findViewById(R.id.txtCodProd);


        // Configura Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tesouroazul1.hospedagemdesites.ws/api/")// <- Coloque a URL base da sua API aqui
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Configura o LayoutManager (define como os itens são organizados)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adiciona divisor entre itens (opcional)
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Inicializa o Adapter com os dados e um listener de clique
        adapter = new ProdutoAdapter(listaProdutos, this, produto ->
        {
            // Ação ao clicar em um item (ex: abrir detalhes)
            Intent intent = new Intent(this, DetalhesProdutoActivity.class);
            intent.putExtra("PRODUTO_ID", produto.getiD_PRODUTO());
            startActivity(intent);
        });

        // Define o adapter no RecyclerView
        recyclerView.setAdapter(adapter);

        carregarProdutos();

        prodImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (ContextCompat.checkSelfPermission(ProdutosActivity.this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
              {
                  openGallery(); // Já tem permissão → Abre direto
              }
              else
              {
                  // Solicita permissão
                  ActivityCompat.requestPermissions(
                          ProdutosActivity.this,
                          new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                          REQUEST_CODE_GALLERY
                  );
              }
          }
      });

        // Ações para os botões
       btnVenderProd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {}
       });

        btnAdicionarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para o botão "Adicionar"
                CriarProd();
            }
        });

        btnAlterarProd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // Lógica para o botão "Alterar"
            }
        });

        btnEstoque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // Navegar para a tela de estoque

            }
        });

        ValProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerUtil.showDatePicker(ProdutosActivity.this, ValProd, false);
            }
        });
    }

    private void carregarProdutos() {
        // Mostrar progress bar (opcional)
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getApiService(ProdutosActivity.this);
        Call<List<SuperClassProd.ProdutoGet>> call = ApiService.getProdutos();

        call.enqueue(new Callback<List<SuperClassProd.ProdutoGet>>() {
            @Override
            public void onResponse(Call<List<SuperClassProd.ProdutoGet>> call,
                                   Response<List<SuperClassProd.ProdutoGet>> response) {

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null)
                {
                    List<SuperClassProd.ProdutoGet> produtos = response.body();
                    adapter.atualizarLista(produtos);
                }
                else {
                    Toast.makeText(ProdutosActivity.this,
                            "Erro ao cargar produtos: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<SuperClassProd.ProdutoGet>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProdutosActivity.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "Erro: ", t);
            }
        });
    }
    private void CriarProd() {
        // Obter valores dos campos de texto (assumindo que são EditText)
        String coD_PRODUTO = CodProd.getText().toString().trim();
        String nomE_PRODUTO = NomeProd.getText().toString().trim();
        String valoR_PRODUTO_STR = ValorProd.getText().toString().trim();
        String tipO_PRODUTO = TipoProd.getText().toString().trim();
        String imG_PRODUTO = "imagem_padrao.jpg"; // Valor padrão ou obtenha de um campo

        // Converter valor para Double
        Double valoR_PRODUTO = null;
        try {
            valoR_PRODUTO = Double.parseDouble(valoR_PRODUTO_STR);
        } catch (NumberFormatException e) {
            Toast.makeText(ProdutosActivity.this, "Valor do produto inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // ID do usuário (poderia ser obtido de SharedPreferences ou outra fonte)
        int iD_USUARIO = 1; // Substitua pelo ID real do usuário logado

        // Criar objeto ProdutoPost
        SuperClassProd.ProdutoPost produtoPost = new SuperClassProd.ProdutoPost(
                iD_USUARIO,
                coD_PRODUTO,
                nomE_PRODUTO,
                valoR_PRODUTO,
                tipO_PRODUTO,
                imG_PRODUTO
        );

        // Conversão para JSON (opcional, para debug)
        Gson gson = new Gson();
        String json = gson.toJson(produtoPost);
        Toast.makeText(ProdutosActivity.this, json, Toast.LENGTH_SHORT).show();

        // Configura Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tesouroazul1.hospedagemdesites.ws/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crie uma instância da interface da API
        ApiService apiService = retrofit.create(ApiService.class);

        // Fazer a chamada à API
        Call<Void> call = apiService.criarProduto(produtoPost);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProdutosActivity.this, "Produto criado com sucesso!", Toast.LENGTH_SHORT).show();
                } else
                {
                    try
                    {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";
                        Toast.makeText(ProdutosActivity.this, "Erro ao criar produto: " + response.code() + " - " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProdutosActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Erro na chamada API", t);
            }
        });
    }

    public String imagem_string(Bitmap prodFoto)
    {
        ByteArrayOutputStream data = new ByteArrayOutputStream();

        // Comprime o bitmap em formato JPEG com 100% de qualidade
        prodFoto.compress(Bitmap.CompressFormat.JPEG, 100, data);

        // Converte o bitmap em um array de bytes
        byte[] b1 = data.toByteArray();

        // Codifica os bytes em uma string Base64
        return Base64.encodeToString(b1, Base64.DEFAULT);
    }

    public Bitmap getFoto(String s)
    {
        // Decodifica a string Base64 de volta para um array de bytes
        byte[] decodes = Base64.decode(s, Base64.DEFAULT);

        // Converte o array de bytes para um objeto Bitmap
        return BitmapFactory.decodeByteArray(decodes, 0, decodes.length);
    }

    private void openGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifica se a imagem foi selecionada com sucesso
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imagemUri = data.getData(); // Obtém o URI da imagem

            try {
                // Carrega a imagem a partir do URI (Uniform Resource Identifier,
                // ou Identificador Uniforme de Recursos) é uma string (sequência de caracteres) que se refere a um recurso

                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imagemUri));

                prodImage.setImageBitmap(bitmap); // Define a imagem no ImageView

                // Converte o bitmap para string Base64
                String bx = imagem_string(bitmap);

                // Armazena a string Base64 em uma variável
                 fotoProd = bx;

                // Reconverte a Base64 para Bitmap (talvez para validar ou reutilizar)
                Bitmap b = getFoto(bx);

                // Atualiza novamente o ImageView com o bitmap reconvertido
                prodImage.setImageBitmap(b);

            } catch (FileNotFoundException e) {
                e.printStackTrace(); // Exibe erro caso o arquivo não seja encontrado
            }
        }
    }

}
