package com.example.tesouro_azul_app.Pages;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.tesouro_azul_app.Class.ProdutoMock;
import com.example.tesouro_azul_app.Class.SuperClassProd;
import com.example.tesouro_azul_app.Adapter.ProdutoAdapter;
import com.example.tesouro_azul_app.Class.SuperClassUser;
import com.example.tesouro_azul_app.R;
import com.example.tesouro_azul_app.Service.RetrofitClient;
import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.Util.AuthUtils;
import com.example.tesouro_azul_app.Util.DatePickerUtil;

import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProdutosActivity extends AppCompatActivity {

    private SuperClassProd.ProdutoDto produtoSelecionado = null;

    private static final int REQUEST_CODE_GALLERY = 1001;
    private static final int PICK_IMAGE = 1;

    private static String fotoProd;

    private RecyclerView recyclerView;
    private ProdutoAdapter adapter;
    private List<SuperClassProd.ProdutoDto> listaProdutos = new ArrayList<>();
    private Handler handler = new Handler();//Para debounce
    private Runnable runnable;
    private static final int DELAY_MILLIS = 500; // Tempo de espera após digitação

    EditText NomeProd;
    EditText ValorProd;
    EditText TipoProd;
    EditText QuantProd;
    EditText ValProd;
    EditText CodProd;
    EditText FornProd;
    EditText SearchProd;
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

        SearchProd = (EditText) findViewById(R.id.txtPesquisaProd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        NomeProd = (EditText) findViewById(R.id.txtNomeProd) ;
        ValorProd = (EditText) findViewById(R.id.txtValorProd);
        TipoProd = (EditText) findViewById(R.id.txtTipo);
        QuantProd = (EditText) findViewById(R.id.txtQuant);
        ValProd = (EditText) findViewById(R.id.txtValidade);
        CodProd = (EditText) findViewById(R.id.txtCodProd);
        FornProd = (EditText) findViewById(R.id.txtFornecedor);

        // Configura Retrofit
        apiService = RetrofitClient.getApiService(getApplicationContext());

        // Configura o LayoutManager (define como os itens são organizados)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adiciona divisor entre itens (opcional)
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //Rever isso aqui
        // Inicializa o Adapter com os dados e um listener de clique
        adapter = new ProdutoAdapter(listaProdutos, this, produto ->
        {
            produtoSelecionado = produto; // Armazena o produto selecionado

            // Preenche os campos com os dados do produto selecionado
            NomeProd.setText(produto.getNomeProduto());
            ValorProd.setText(String.valueOf(produto.getValorProduto()));
            TipoProd.setText(produto.getTipoProduto());

            // Se houver imagem, carrega (opcional)
            if (produto.getImgProduto() != null) {
                Bitmap bitmap = getFoto(produto.getImgProduto());
                prodImage.setImageBitmap(bitmap);
            }
        });

        // Define o adapter no RecyclerView
        recyclerView.setAdapter(adapter);

        configurarBusca();
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
            public void onClick(View view) {/*deletarProduto()*/;}
        });

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {realizarCompra();}
        });

        // Ações para os botões
       btnVenderProd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {realizarVenda();}
       });

        btnAdicionarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {criarProduto();}
        });

        btnAlterarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*alterarProduto();*/}
        });

        ValProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerUtil.showDatePicker(ProdutosActivity.this, ValProd, false);
            }
        });

    }


    //Mock:referece a objetos ou dados simulados
    private void buscarPorNomeMock(String nome) {
        // Simular tempo de carregamento da API
        new Handler().postDelayed(() -> {
            List<SuperClassProd.ProdutoDto> todosProdutos = ProdutoMock.gerarProdutosMockados();
            List<SuperClassProd.ProdutoDto> produtosFiltrados = new ArrayList<>();

            if (nome.isEmpty()) {
                // Se não há termo de busca, mostrar todos
                produtosFiltrados.addAll(todosProdutos);
            } else {
                // Filtrar produtos que contenham o termo de busca no nome
                String termoBusca = nome.toLowerCase();
                for (SuperClassProd.ProdutoDto produto : todosProdutos) {
                    if (produto.getNomeProduto().toLowerCase().contains(termoBusca)) {
                        produtosFiltrados.add(produto);
                    }
                }
            }

            // Atualizar UI na thread principal
            runOnUiThread(() -> {
                if (!produtosFiltrados.isEmpty()) {
                    adapter.atualizarLista(produtosFiltrados);
                } else {
                    Toast.makeText(ProdutosActivity.this,
                            "Nenhum produto encontrado com o nome: " + nome,
                            Toast.LENGTH_SHORT).show();
                    adapter.atualizarLista(new ArrayList<>());
                }
            });
        }, 1000); // Simular 1 segundo de delay como uma chamada real de API
    }

    private void carregarProdutosMock() {
        progressBar.setVisibility(View.VISIBLE);

        // Simular tempo de carregamento da API
        new Handler().postDelayed(() -> {
            List<SuperClassProd.ProdutoDto> produtos = ProdutoMock.gerarProdutosMockados();

            // Atualizar UI na thread principal
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                adapter.atualizarLista(produtos);
            });
        }, 1500); // Simular 1.5 segundos de delay como uma chamada real de API
    }

    private void configurarBusca() {
        SearchProd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancelar busca anterior pendente
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String termoBusca = s.toString().trim();

                // Implementar debounce para evitar buscas a cada tecla pressionada
                runnable = () -> buscarPorNomeApi1(termoBusca);
                handler.postDelayed(runnable, DELAY_MILLIS);
            }
        });
    }

    //Repor e rever
    private void buscarPorNomeApi1(String nome) {

        String token = obterTokenUsuario();

        String authHeader =  token;

        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
        Call<List<SuperClassProd.ProdutoDto>> call = apiService.buscarProdutosPorNomeSimilar(authHeader, nome);

        call.enqueue(new Callback<List<SuperClassProd.ProdutoDto>>() {
            @Override
            public void onResponse(Call<List<SuperClassProd.ProdutoDto>> call,
                                   Response<List<SuperClassProd.ProdutoDto>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<SuperClassProd.ProdutoDto> produtos = response.body();

                    if (!produtos.isEmpty()) {
                        adapter.atualizarLista(produtos);
                    } else {
                        Toast.makeText(ProdutosActivity.this, "Nenhum produto encontrado.", Toast.LENGTH_SHORT).show();
                        adapter.atualizarLista(new ArrayList<>()); // Limpa a lista
                    }

                } else {
                    Toast.makeText(ProdutosActivity.this, "Erro ao buscar: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<SuperClassProd.ProdutoDto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProdutosActivity.this, "Erro na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "Erro: ", t);
            }
        });
    }

    //Repor e rever
    private void buscarPorNomeApi2(String nome) {

        String token = obterTokenUsuario();

        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
        Call<List<SuperClassProd.ProdutoDto>> call = apiService.buscarProdutosPorNomeSimilar(token, nome);

        call.enqueue(new Callback<List<SuperClassProd.ProdutoDto>>() {
            @Override
            public void onResponse(Call<List<SuperClassProd.ProdutoDto>> call,
                                   Response<List<SuperClassProd.ProdutoDto>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    adapter.atualizarLista(response.body());

                    if (response.body().isEmpty()) {
                        Toast.makeText(ProdutosActivity.this,
                                "Nenhum produto encontrado",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProdutosActivity.this,
                            "Erro na busca: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SuperClassProd.ProdutoDto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProdutosActivity.this,
                        "Falha na conexão",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Repor
    private void carregarProdutos() {
        String token = obterTokenUsuario();

        Call<List<SuperClassProd.ProdutoDto>> call = apiService.buscarTodosProdutos(token);

        call.enqueue(new Callback<List<SuperClassProd.ProdutoDto>>() {
            @Override
            public void onResponse(Call<List<SuperClassProd.ProdutoDto>> call,
                                   Response<List<SuperClassProd.ProdutoDto>> response) {

                if (response.isSuccessful() && response.body() != null)
                {
                    List<SuperClassProd.ProdutoDto> produtos = response.body();
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
            public void onFailure(Call<List<SuperClassProd.ProdutoDto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProdutosActivity.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "Erro: ", t);
            }
        });
    }

    private void realizarVenda() {
        // 1. Validação do produto selecionado
        if (produtoSelecionado == null) {
            Toast.makeText(this, "Selecione um produto primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Validação da quantidade
        String quantidadeStr = QuantProd.getText().toString().trim();
        if (quantidadeStr.isEmpty()) {
            Toast.makeText(this, "Informe a quantidade", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
            if (quantidade <= 0) {
                Toast.makeText(this, "Quantidade deve ser maior que zero", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Cálculo do valor total
        double valorTotal = produtoSelecionado.getValorProduto() * quantidade;
        String lote = "LOTE-" + System.currentTimeMillis();

        // 4. Obter token do usuário
        String token = AuthUtils.getToken(this);
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // 5. Criar DTOs para a venda (CORREÇÃO: usando getIdProduto() em vez de getIdUsuarioFk())
        SuperClassProd.ItemVendaDto itemVenda = new SuperClassProd.ItemVendaDto(
                0,
                lote,
                quantidade,
                1, // Número do item
                0.00, // Desconto
                valorTotal
        );

        List<SuperClassProd.ItemVendaDto> itens = new ArrayList<>();
        itens.add(itemVenda);

        SuperClassProd.PedidoDto pedido = new SuperClassProd.PedidoDto(valorTotal);

        SuperClassProd.PedidoVendaCompletoDto pedidoVenda = new SuperClassProd.PedidoVendaCompletoDto(
                pedido,
                itens
        );

        // 6. Chamar API para realizar venda (CORREÇÃO: adicionado "Bearer " antes do token)
        ApiService apiService = RetrofitClient.getApiService(this);
        Call<SuperClassProd.PedidoVendaCompletoDto> call = apiService.criarPedidoVenda("Bearer " + token, pedidoVenda);

        call.enqueue(new Callback<SuperClassProd.PedidoVendaCompletoDto>() {
            @Override
            public void onResponse(Call<SuperClassProd.PedidoVendaCompletoDto> call,
                                   Response<SuperClassProd.PedidoVendaCompletoDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProdutosActivity.this, "Venda realizada com sucesso!", Toast.LENGTH_LONG).show();
                    limparCampos();
                    produtoSelecionado = null;
                    carregarProdutos();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";
                        Toast.makeText(ProdutosActivity.this,
                                "Erro: " + (response.code() == 400 ? "Dados inválidos" : errorBody),
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(ProdutosActivity.this, "Erro ao processar resposta", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuperClassProd.PedidoVendaCompletoDto> call, Throwable t) {
                Toast.makeText(ProdutosActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VENDA_ERROR", "Falha ao realizar venda", t);
            }
        });
    }
    public String formatarData(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Importante para usar UTC
        return sdf.format(data);
    }

    //Ainda sendo feita
    private void realizarCompra() {
        // Validação do produto selecionado
        if (produtoSelecionado == null) {
            Toast.makeText(this, "Selecione um produto primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

        //Validação da quantidade
        String quantidadeStr = QuantProd.getText().toString().trim();

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
            if (quantidade == 0) {
                quantidade = 1;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação da data de validade
        String validadetxt = ValProd.getText().toString();
        String validade = formatarParaISO8601(validadetxt, formatoEntrada);
        if (validade == null) {
            Toast.makeText(this, "Data de validade inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cálculo do valor total
        double valorTotal = produtoSelecionado.getValorProduto() * quantidade;
        String lote = "LOTE-" + System.currentTimeMillis();

        // Criar DTOs para a compra
        SuperClassProd.ItemCompraDto itemCompra = new SuperClassProd.ItemCompraDto(
                0,// ID do produto
                0, // ID do pedido (será gerado pelo servidor)
                validade,
                lote,
                quantidade,
                1, // Número do item
                valorTotal
        );

        List<SuperClassProd.ItemCompraDto> itens = new ArrayList<>();
        itens.add(itemCompra);

        SuperClassProd.PedidoDto pedido = new SuperClassProd.PedidoDto(valorTotal);

        SuperClassProd.PedidoCompraCompletoDto pedidoCompra = new SuperClassProd.PedidoCompraCompletoDto(
                pedido,
                itens
        );

        // Obter token de autenticação
        String token = AuthUtils.getToken(this);
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Token de autenticação inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chamar API para realizar compra
        Call<SuperClassProd.PedidoCompraCompletoDto> call = apiService.criarPedidoCompra("Bearer " + token, pedidoCompra);

        call.enqueue(new Callback<SuperClassProd.PedidoCompraCompletoDto>() {
            @Override
            public void onResponse(Call<SuperClassProd.PedidoCompraCompletoDto> call,
                                   Response<SuperClassProd.PedidoCompraCompletoDto> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProdutosActivity.this,
                            "Compra realizada com sucesso!",
                            Toast.LENGTH_LONG).show();

                    limparCampos();
                    produtoSelecionado = null;
                    carregarProdutos();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";
                        Toast.makeText(ProdutosActivity.this,
                                "Erro: " + (response.code() == 400 ? "Dados inválidos" : errorBody),
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(ProdutosActivity.this, "Erro ao processar resposta", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuperClassProd.PedidoCompraCompletoDto> call, Throwable t) {

                Toast.makeText(ProdutosActivity.this,
                        "Erro na conexão. Tente novamente.", Toast.LENGTH_SHORT).show();
                Log.e("COMPRA_ERROR", "Falha ao realizar compra", t);
            }
        });
    }

    public String formatarParaISO8601(String dataOriginal, String formatoOriginal) {
        try {
            // 1. Converter a String original para Date
            SimpleDateFormat parser = new SimpleDateFormat(formatoOriginal, Locale.getDefault());
            Date date = parser.parse(dataOriginal);

            // 2. Formatar a Date para ISO 8601
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter.format(date);

        } catch (Exception e) {
            e.printStackTrace();
            return null; // ou lançar exceção
        }
    }

    // Exemplo de uso:
    String dataEntrada = "15/06/2023 14:30";
    String formatoEntrada = "dd/MM/yyyy HH:mm";
    String dataISO = formatarParaISO8601(dataEntrada, formatoEntrada);

    //Ainda esta sendo feita
    private void deletarProduto(int produtoId) {
            // Mostrar diálogo de confirmação
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar Exclusão")
                    .setMessage("Tem certeza que deseja excluir este produto?")
                    .setPositiveButton("Excluir", (dialog, which) -> {
                        // Usuário confirmou, prosseguir com a exclusão
                        executarDeletarProduto(produtoId);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }

        private void executarDeletarProduto(int produtoId) {
            String token = obterTokenUsuario();
            if (token == null) {

                Toast.makeText(this, "Token inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
            Call<ResponseBody> call = apiService.deletarProduto(token, produtoId);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(ProdutosActivity.this,
                                "Produto excluído com sucesso!",
                                Toast.LENGTH_LONG).show();

                        // Atualizar lista de produtos
                        carregarProdutos();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            mostrarErroDelete(errorBody);
                        } catch (IOException e) {
                            mostrarErroDelete("Erro ao processar resposta");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ProdutosActivity.this,
                            "Falha na conexão: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("DELETE_PRODUTO", "Erro ao deletar produto", t);
                }
            });
        }

        private void mostrarErroDelete(String mensagem) {
            if (mensagem.contains("associado a pedidos")) {
                new AlertDialog.Builder(this)
                        .setTitle("Não é possível excluir")
                        .setMessage("Este produto está vinculado a pedidos e não pode ser excluído.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                Toast.makeText(this, "Erro: " + mensagem, Toast.LENGTH_LONG).show();
            }
        }


    //Ainda esta sendo feita
    /*
    private void alterarProduto() {
        // 1. Verificar se há um produto selecionado
        if (produtoSelecionado == null) {
            Toast.makeText(this, "Selecione um produto primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Obter o campo a ser alterado e o novo valor
        String campo = ""; // Você precisa definir como obter isso (pode ser um Spinner ou outra UI)
        String novoValor = ""; // Você precisa obter isso de um EditText ou outro componente

        // Exemplo básico - você deve adaptar para sua UI:
        if (NomeProd.isFocused()) {
            campo = "nome_produto";
            novoValor = NomeProd.getText().toString().trim();
        } else if (ValorProd.isFocused()) {
            campo = "valor_produto";
            novoValor = ValorProd.getText().toString().trim();
        } else if (TipoProd.isFocused()) {
            campo = "tipo_produto";
            novoValor = TipoProd.getText().toString().trim();
        } else if (CodProd.isFocused()) {
            campo = "cod_produto";
            novoValor = CodProd.getText().toString().trim();
        }


        if (campo.isEmpty() || novoValor.isEmpty()) {
            Toast.makeText(this, "Selecione um campo e informe o novo valor", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Criar o DTO para alteração
        SuperClassProd.CamposProdutoDto camposProduto = new SuperClassProd.CamposProdutoDto(
                campo,
                novoValor
        );

        // 6. Obter token e verificar autenticação
        String token = obterTokenUsuario();
        if (token == null) {

            return;
        }

        // 7. Chamar a API para alterar o produto
        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
        Call<SuperClassProd.ProdutoDto> call = apiService.alterarProduto(
                token,
                produtoSelecionado.getIdUsuarioFk(), // Supondo que este seja o ID do produto
                camposProduto
        );

        call.enqueue(new Callback<SuperClassProd.ProdutoDto>() {
            @Override
            public void onResponse(Call<SuperClassProd.ProdutoDto> call, Response<SuperClassProd.ProdutoDto> response) {

                if (response.isSuccessful() && response.body() != null) {
                    // Atualizar o produto selecionado com os novos dados
                    produtoSelecionado = response.body();

                    // Atualizar a UI com os novos valores
                    NomeProd.setText(produtoSelecionado.getNomeProduto());
                    ValorProd.setText(String.valueOf(produtoSelecionado.getValorProduto()));
                    // Atualize outros campos conforme necessário

                    Toast.makeText(ProdutosActivity.this,
                            "Produto atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Atualizar a lista de produtos
                    carregarProdutos();
                } else {
                    tratarErroAlteracao(response);
                }
            }

            @Override
            public void onFailure(Call<SuperClassProd.ProdutoDto> call, Throwable t) {
                Toast.makeText(ProdutosActivity.this,
                        "Erro na conexão. Tente novamente.", Toast.LENGTH_SHORT).show();
                Log.e("ALTERACAO_ERROR", "Falha ao alterar produto", t);
            }
        });
    }
*/
    private void tratarErroAlteracao(Response<?> response) {
        try {
            String errorBody = response.errorBody() != null ?
                    response.errorBody().string() : "Erro desconhecido";

            String mensagem;
            switch (response.code()) {
                case 400:
                    mensagem = "Dados inválidos: " + errorBody;
                    break;
                case 404:
                    mensagem = "Produto não encontrado";
                    break;
                case 401:
                    mensagem = "Não autorizado - faça login novamente";
                    break;
                default:
                    mensagem = "Erro ao atualizar produto: " + response.code();
            }

            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
            Log.e("API_ERROR", "Código: " + response.code() + " - " + errorBody);
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao processar resposta", Toast.LENGTH_SHORT).show();
            Log.e("IO_ERROR", "Erro ao ler errorBody", e);
        }
    }

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

        // Conversão do valor para Double com tratamento de erro
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

        int idUsuario = 0;

        // Criar objeto DTO com ou sem imagem
        SuperClassProd.ProdutoDto produtoDto = new SuperClassProd.ProdutoDto(
                codProduto, // codProduto
                nomeProduto, // nomeProduto
                valorProduto, // valorProduto
                tipoProduto, // tipoProduto
                fotoProd // imgProduto
        );


        // Recuperar token do usuário logado
        String token = obterTokenUsuario(); // Essa função precisa retornar o token em formato "Bearer eyJ..."

        Call<Void> call = apiService.cadastrarProduto(token, produtoDto);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProdutosActivity.this,
                            "Produto criado com sucesso!", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    prodImage.setImageResource(R.drawable.placeholder);
                    fotoProd = null;
                } else {
                    tratarErroApi(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ProdutosActivity.this,
                        "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Erro na chamada API", t);
            }
        });
    }

    private String obterTokenUsuario() {
        String token = AuthUtils.getToken(this); // "this" é o Context da Activity
        if (token != null && !token.isEmpty()) {
            return "Bearer " + token;
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return null;
        }
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
