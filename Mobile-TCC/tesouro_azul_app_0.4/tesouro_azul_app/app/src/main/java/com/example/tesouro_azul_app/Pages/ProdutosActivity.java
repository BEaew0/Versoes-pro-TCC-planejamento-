package com.example.tesouro_azul_app.Pages;

import android.Manifest;
import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesouro_azul_app.Class.SuperClassProd;
import com.example.tesouro_azul_app.Adapter.ProdutoAdapter;
import com.example.tesouro_azul_app.R;
import com.example.tesouro_azul_app.Service.RetrofitClient;
import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.Util.DatePickerUtil;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProdutosActivity extends AppCompatActivity {

    private SuperClassProd.Produto produtoSelecionado = null;

    private static final int REQUEST_CODE_GALLERY = 1001;
    private static final int PICK_IMAGE = 1;

    private static String fotoProd;

    private RecyclerView recyclerView;
    private ProdutoAdapter adapter;
    private List<SuperClassProd.Produto> listaProdutos = new ArrayList<>();

    EditText NomeProd,ValorProd,TipoProd,QuantProd,ValProd,CodProd,FornProd;
    Button btnVenderProd, btnAdicionarProd, btnAlterarProd, btnExluir,btnComprar;
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
        btnComprar = findViewById(R.id.btnComprarProd);
        btnExluir = findViewById(R.id.btnExcluirProd);

        prodImage = findViewById(R.id.prod_image);
        recyclerView = findViewById(R.id.recyclerViewProdutos);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        NomeProd = (EditText) findViewById(R.id.txtNomeProd) ;
        ValorProd = (EditText) findViewById(R.id.txtValorProd);
        TipoProd = (EditText) findViewById(R.id.txtTipo);
        QuantProd = (EditText) findViewById(R.id.txtQuant);
        ValProd = (EditText) findViewById(R.id.txtValidade);
        CodProd = (EditText) findViewById(R.id.txtCodProd);
        FornProd = (EditText) findViewById(R.id.txtFornecedor);

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

        //Rever isso aqui
        // Inicializa o Adapter com os dados e um listener de clique
        /*adapter = new ProdutoAdapter(listaProdutos, this, produto ->
        {
            produtoSelecionado = produto; // Armazena o produto selecionado

            // Preenche os campos com os dados do produto selecionado
            NomeProd.setText(produto.getNOME_PRODUTO());
            ValorProd.setText(String.valueOf(produto.getVALOR_PRODUTO()));
            TipoProd.setText(produto.getTIPO_PRODUTO());

            // Se houver imagem, carrega (opcional)
            if (produto.getIMG_PRODUTO() != null) {
                Bitmap bitmap = getFoto(produto.getIMG_PRODUTO());
                prodImage.setImageBitmap(bitmap);
            }
        });*/

        // Define o adapter no RecyclerView
        recyclerView.setAdapter(adapter);

        carregarProdutos();

        prodImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (ContextCompat.checkSelfPermission(ProdutosActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
              {
                  openGallery(); // Já tem permissão → Abre direto
              }
              else
              {// Solicita permissão
                  ActivityCompat.requestPermissions(
                          ProdutosActivity.this,
                          new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                          REQUEST_CODE_GALLERY
                  );
              }
          }
      });

        btnExluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                criarProduto();
            }
        });

        btnAlterarProd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // Lógica para o botão "Alterar"
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

        // Na sua Activity ou Fragment
        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
        Call<List<SuperClassProd.Produto>> call = apiService.buscarTodosProdutos();

        call.enqueue(new Callback<List<SuperClassProd.Produto>>() {
            @Override
            public void onResponse(Call<List<SuperClassProd.Produto>> call,
                                   Response<List<SuperClassProd.Produto>> response) {

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null)
                {
                    List<SuperClassProd.Produto> produtos = response.body();
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
            public void onFailure(Call<List<SuperClassProd.Produto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProdutosActivity.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "Erro: ", t);
            }
        });
    }

    //Ainda esta sendo feita

    /*private void criarPedidoVenda() {
        // 1. Verifica se há um produto selecionado
        if (produtoSelecionado == null) {
            Toast.makeText(this, "Selecione um produto primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Valida a quantidade
        String quantidadeStr = QuantProd.getText().toString().trim();
        if (quantidadeStr.isEmpty()) {
            Toast.makeText(this, "Informe a quantidade", Toast.LENGTH_SHORT).show();
            return;
        }

        double quantidade;
        try {
            quantidade = Double.parseDouble(quantidadeStr);
            if (quantidade <= 0) {
                Toast.makeText(this, "Quantidade deve ser maior que zero", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Valida a data de validade (opcional para venda)
        String dataValidade = ValProd.getText().toString().trim();
        String dataFormatada = null;

        if (!dataValidade.isEmpty()) {
            SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date date = sdfInput.parse(dataValidade);
                dataFormatada = sdfOutput.format(date);
            } catch (ParseException e) {
                Toast.makeText(this, "Formato de data inválido (use DD/MM/AAAA)", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 4. Cálculos e preparação dos dados
        double valorTotal = produtoSelecionado.getVALOR_PRODUTO() * quantidade;
        String lote = "LOTE-" + System.currentTimeMillis();

        // 5. Criação dos DTOs para venda
        List<SuperClassProd.ItemVendaDto> itens = new ArrayList<>();
        itens.add(new SuperClassProd.ItemVendaDto(
                produtoSelecionado.getID_PRODUTO(),
                lote,
                quantidade,
                1, // N_ITEM_VENDA
                0.00, // DESCONTO (pode ser ajustado)
                valorTotal
        ));

        int idUsuario = obterIdUsuarioLogado();
        if (idUsuario == -1) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // DTO principal para venda
        SuperClassProd.PedidoVendaDto pedidoVenda = new SuperClassProd.PedidoVendaDto(
                idUsuario,
                valorTotal
        );

        // DTO completo para a API
        SuperClassProd.PedidoVendaCompletoDto pedidoVendaDto =
                new SuperClassProd.PedidoVendaCompletoDto(pedidoVenda, itens);

        // 6. Mostra progresso (substitua por ProgressBar se possível)
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processando venda...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // 7. Chama a API de venda
        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
        Call<SuperClassProd.PedidoVendaResponse> call = apiService.criarPedidoVenda(pedidoVendaDto);

        call.enqueue(new Callback<SuperClassProd.PedidoVendaResponse>() {
            @Override
            public void onResponse(Call<SuperClassProd.PedidoVendaResponse> call,
                                   Response<SuperClassProd.PedidoVendaResponse> response) {

                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    // Sucesso
                    Toast.makeText(ProdutosActivity.this,
                            "Venda realizada com sucesso! ID: " +
                                    response.body().PedidoVendaID, Toast.LENGTH_LONG).show();

                    limparCampos();
                    produtoSelecionado = null;

                    // Atualiza a lista de produtos
                    carregarProdutos();
                } else {
                    tratarErroVenda(response);
                }
            }

            @Override
            public void onFailure(Call<SuperClassProd.PedidoVendaResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProdutosActivity.this,
                        "Erro na conexão. Tente novamente.", Toast.LENGTH_SHORT).show();
                Log.e("VENDA_ERROR", "Falha ao realizar venda", t);
            }
        });
    }*/

    // Método para tratar erros específicos de venda
    private void tratarErroVenda(Response<?> response) {
        try {
            String errorBody = response.errorBody() != null ?
                    response.errorBody().string() : "Erro desconhecido";

            if (response.code() == 400) {
                JSONObject errorJson = new JSONObject(errorBody);
                if (errorJson.has("estoque")) {
                    Toast.makeText(this, "Estoque insuficiente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Dados inválidos: " + errorBody, Toast.LENGTH_LONG).show();
                }
            } else if (response.code() == 401) {
                Toast.makeText(this, "Sessão expirada", Toast.LENGTH_LONG).show();
                // Redirecionar para login
            } else {
                Toast.makeText(this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao processar resposta", Toast.LENGTH_SHORT).show();
        }
    }

    //Ainda esta sendo feita
    private void removerProduto() {
        //  Obter o ID do produto selecionado (você precisará implementar essa lógica)
        // Por exemplo, se você tiver um produto selecionado no adapter:
        int produtoId = obterProdutoSelecionado();

        if (produtoId == -1) {
            Toast.makeText(this, "Selecione um produto para remover", Toast.LENGTH_SHORT).show();
            return;
        }

        //  Mostrar confirmação antes de remover
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja remover este produto?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // 3. Chamada à API para remover
                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Removendo produto...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // Na sua Activity ou Fragment
                    ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
                    Call<Void> call = apiService.deletarProduto(produtoId);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful()) {
                                Toast.makeText(ProdutosActivity.this,
                                        "Produto removido com sucesso!", Toast.LENGTH_SHORT).show();
                                carregarProdutos(); // Recarrega a lista
                            } else {
                                tratarErroApi(response);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(ProdutosActivity.this,
                                    "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Não", null)
                .show();
    }

    //Ainda esta sendo feita
    private void comprarProduto() {
        // 1. Verifica se há um produto selecionado
        if (produtoSelecionado == null) {
            Toast.makeText(this, "Selecione um produto primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Valida a quantidade
        String quantidadeStr = QuantProd.getText().toString().trim();
        if (quantidadeStr.isEmpty()) {
            Toast.makeText(this, "Informe a quantidade", Toast.LENGTH_SHORT).show();
            return;
        }

        double quantidade;
        try {
            quantidade = Double.parseDouble(quantidadeStr);
            if (quantidade <= 0) {
                Toast.makeText(this, "Quantidade deve ser maior que zero", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Valida a data de validade
        String dataValidade = ValProd.getText().toString().trim();
        String dataFormatada = null;

        if (!dataValidade.isEmpty()) {
            SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date date = sdfInput.parse(dataValidade);
                // Verifica se a data é futura
                if (date.before(new Date())) {
                    Toast.makeText(this, "Data de validade deve ser futura", Toast.LENGTH_SHORT).show();
                    return;
                }
                dataFormatada = sdfOutput.format(date);
            } catch (ParseException e) {
                Toast.makeText(this, "Formato de data inválido (use DD/MM/AAAA)", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 4. Cálculos e preparação dos dados
        double valorTotal = produtoSelecionado.getVALOR_PRODUTO() * quantidade;
        String lote = "LOTE-" + System.currentTimeMillis();

        // 5. Criação dos DTOs
        List<SuperClassProd.ItemCompraDto> itens = new ArrayList<>();
        itens.add(new SuperClassProd.ItemCompraDto(
                produtoSelecionado.getID_PRODUTO(),
                lote,
                quantidade,
                1, // N_ITEM_COMPRA (poderia ser incremental)
                valorTotal,
                dataFormatada
        ));

        int idUsuario = obterIdUsuarioLogado();
        if (idUsuario == -1) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        SuperClassProd.PedidoDto pedido = new SuperClassProd.PedidoDto(
                idUsuario,
                1, // ID_FORNECEDOR (vao se foderem back end )
                valorTotal
        );
        //Vai se foder back end
        //________________$$$$
        //______________$$____$$
        //______________$$____$$
        //______________$$____$$
        //______________$$____$$
        //______________$$____$$
        //__________$$$$$$____$$$$$$
        //________$$____$$____$$____$$$$
        //________$$____$$____$$____$$__$$
        //$$$$$$__$$____$$____$$____$$____$$
        //$$____$$$$________________$$____$$
        //$$______$$______________________$$
        //__$$____$$______________________$$
        //___$$$__$$______________________$$
        //____$$__________________________$$
        //_____$$$________________________$$
        //______$$______________________$$$
        //_______$$$____________________$$
        //________$$____________________$$
        //_________$$$________________$$$
        //__________$$________________$$
        //__________$$$$$$$$$$$$$$$$$$$$

        SuperClassProd.PedidoCompraCompletoDto pedidoDto =
                new SuperClassProd.PedidoCompraCompletoDto(pedido, itens);

        // 6. Mostra progresso
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processando compra...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // 7. Chama a API
        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
        Call<SuperClassProd.PedidoCompraResponse> call = apiService.criarPedidoCompra(pedidoDto);

        call.enqueue(new Callback<SuperClassProd.PedidoCompraResponse>() {
            @Override
            public void onResponse(Call<SuperClassProd.PedidoCompraResponse> call,
                                   Response<SuperClassProd.PedidoCompraResponse> response) {

                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    // Sucesso
                    Toast.makeText(ProdutosActivity.this,
                            "Compra realizada com sucesso! ID: " +
                                    response.body().PedidoID, Toast.LENGTH_LONG).show();

                    limparCampos();
                    produtoSelecionado = null;

                    // Atualiza a lista de produtos se necessário
                    carregarProdutos();
                } else {
                    // Trata erros da API
                    Toast.makeText(ProdutosActivity.this,"Deu erro na API",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuperClassProd.PedidoCompraResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProdutosActivity.this,
                        "Erro na conexão. Tente novamente.", Toast.LENGTH_SHORT).show();
                Log.e("COMPRA_ERROR", "Falha ao realizar compra", t);
            }
        });
    }

    //Rever
    private int obterProdutoSelecionado() {
        if (produtoSelecionado == null) {
            Toast.makeText(this, "Nenhum produto selecionado", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return produtoSelecionado.getID_PRODUTO(); // Supondo que Produto tenha um método getID_PRODUTO()
    }

    //Ainda esta sendo feita
    private void alterarProduto(){}

    private void criarProduto() {
        // Validação dos campos de entrada
        String codProduto = CodProd.getText().toString().trim();
        String nomeProduto = NomeProd.getText().toString().trim();
        String valorProdutoStr = ValorProd.getText().toString().trim();
        String tipoProduto = TipoProd.getText().toString().trim();

        // Validação de campos obrigatórios
        if (codProduto.isEmpty() || nomeProduto.isEmpty() || valorProdutoStr.isEmpty() || tipoProduto.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        //Conversão do valor para Double com tratamento de erro
        double valorProduto;
        try {
            valorProduto = Double.parseDouble(valorProdutoStr);
            if (valorProduto <= 0) {
                Toast.makeText(this, "O valor do produto deve ser positivo", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor do produto inválido", Toast.LENGTH_SHORT).show();
            return;
        }
        int idUsuario = obterIdUsuarioLogado();

        // Verificar se uma imagem foi selecionada
        if (fotoProd == null || fotoProd.isEmpty())
        {
            // Criar objeto DTO sem imagem
            SuperClassProd.CadastrarProdutoDto produtoDto = new SuperClassProd.CadastrarProdutoDto(
                    idUsuario,       // ID_USUARIO
                    codProduto,      // COD_PRODUTO
                    nomeProduto,     // NOME_PRODUTO
                    valorProduto,    // VALOR_PRODUTO
                    tipoProduto,// TIPO_PRODUTO
                    fotoProd = null
            );
        }

        // Criar objeto DTO com a imagem em Base64
        SuperClassProd.CadastrarProdutoDto produtoDto = new SuperClassProd.CadastrarProdutoDto(
                idUsuario,       // ID_USUARIO
                codProduto,      // COD_PRODUTO
                nomeProduto,     // NOME_PRODUTO
                valorProduto,    // VALOR_PRODUTO
                tipoProduto,    // TIPO_PRODUTO
                fotoProd        // IMG_PRODUTO (string Base64)
        );


        // Mostrar progresso durante o upload
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando produto...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Usar o RetrofitClient singleton
        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
        Call<Void> call = apiService.cadastrarProduto(produtoDto);

        // 9. Chamada à API com tratamento completo de erros
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    Toast.makeText(ProdutosActivity.this,
                            "Produto criado com sucesso!", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    // Resetar a imagem após envio bem-sucedido
                    prodImage.setImageResource(R.drawable.placeholder);
                    fotoProd = null;
                } else
                {
                    tratarErroApi(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProdutosActivity.this,
                        "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Erro na chamada API", t);
            }
        });
    }

    //Rever
    private int obterIdUsuarioLogado() {
        // Implemente a lógica para obter o ID do usuário logado
        // Exemplo: SharedPreferences, sessão, etc.
        return 1; // Apenas exemplo - substitua pela implementação real
    }

    private void limparCampos() {
        CodProd.setText("");
        NomeProd.setText("");
        ValorProd.setText("");
        TipoProd.setText("");
        CodProd.requestFocus();
    }

    private void tratarErroApi(Response<Void> response) {
        try {
            String errorBody = response.errorBody() != null ?
                    response.errorBody().string() : "Erro desconhecido";

            // Você pode parsear o erroBody se a API retornar um JSON estruturado
            String mensagemErro = "Erro ao criar produto: " + response.code();

            if (response.code() == 400) {
                mensagemErro = "Dados inválidos: " + errorBody;
            } else if (response.code() == 401) {
                mensagemErro = "Acesso não autorizado";
            } else if (response.code() == 500) {
                mensagemErro = "Erro no servidor";
            }

            Toast.makeText(this, mensagemErro, Toast.LENGTH_LONG).show();
            Log.e("API_RESPONSE", "Código: " + response.code() + " - " + errorBody);
        } catch (IOException e) {
            Log.e("API_ERROR", "Erro ao ler errorBody", e);
        }
    }

    public String imagem_string(Bitmap prodFoto) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();

        // Comprime o bitmap em formato JPEG com 100% de qualidade
        prodFoto.compress(Bitmap.CompressFormat.JPEG, 100, data);

        // Converte o bitmap em um array de bytes
        byte[] b1 = data.toByteArray();

        // Codifica os bytes em uma string Base64
        return Base64.encodeToString(b1, Base64.DEFAULT);
    }

    public Bitmap getFoto(String s) {
        // Decodifica a string Base64 de volta para um array de bytes
        byte[] decodes = Base64.decode(s, Base64.DEFAULT);

        // Converte o array de bytes para um objeto Bitmap
        return BitmapFactory.decodeByteArray(decodes, 0, decodes.length);
    }

    private void openGallery() {
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
