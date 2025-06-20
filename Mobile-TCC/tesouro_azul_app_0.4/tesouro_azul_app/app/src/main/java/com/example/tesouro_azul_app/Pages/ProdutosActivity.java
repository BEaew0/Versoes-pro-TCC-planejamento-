package com.example.tesouro_azul_app.Pages;

import android.Manifest;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.tesouro_azul_app.Class.SuperClassUser;
import com.example.tesouro_azul_app.R;
import com.example.tesouro_azul_app.Service.RetrofitClient;
import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.Util.AuthUtils;
import com.example.tesouro_azul_app.Util.DatePickerUtil;
import com.example.tesouro_azul_app.Util.ImageUtils;
import com.google.android.material.imageview.ShapeableImageView;

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

/**
 * Activity para gerenciamento de produtos - CRUD, vendas e compras
 */
public class ProdutosActivity extends AppCompatActivity {

    // Produto atualmente selecionado na lista
    private SuperClassProd.ProdutoDto produtoSelecionado = null;

    // Código para solicitação de permissão de galeria
    private static final int REQUEST_CODE_GALLERY = 1001;

    // Launcher para seleção de imagens
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    // Variável para armazenar a foto do produto em Base64
    private static String fotoProd;

    // Componentes de UI
    private RecyclerView recyclerView;
    private ProdutoAdapter adapter;
    private List<SuperClassProd.ProdutoDto> listaProdutos = new ArrayList<>();

    // Handler para implementar debounce na busca
    private Handler handler = new Handler();
    private Runnable runnable;
    private static final int DELAY_MILLIS = 500; // Atraso para busca após digitação

    // Campos de entrada
    EditText NomeProd, ValorProd, TipoProd, QuantProd, ValProd, CodProd, FornProd, SearchProd;

    // Botões de ação
    Button btnVenderProd, btnAdicionarProd, btnAlterarProd, btnExluir, btnComprar;

    // Componentes de imagem e loading
    ShapeableImageView prodImage;
    ProgressBar progressBar;

    // Serviço de API
    private ApiService apiService;

    // Dados de autenticação
    String token;
    int userId;

    // Formato de data
    String formatoEntrada = "dd/MM/yyyy HH:mm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtosctivity);

        // Inicialização de componentes de UI
        inicializarViews();

        // Configuração do Retrofit para chamadas à API
        apiService = RetrofitClient.getApiService(getApplicationContext());
        token = obterTokenUsuario();

        // Configuração do RecyclerView (lista de produtos)
        configurarRecyclerView();

        // Obter informações do usuário do token JWT
        obterInfoUsuario();

        // Configurar busca com debounce
        configurarBusca();

        // Configurar seletor de imagens
        configurarImagePicker();

        // Configurar listeners de clique
        configurarListeners();


    }

    /** Inicializa todas as views do layout */
    private void inicializarViews() {
        btnVenderProd = findViewById(R.id.btnVenderProd);
        btnAdicionarProd = findViewById(R.id.btnAdicionarProd);
        btnAlterarProd = findViewById(R.id.btnAlterarProd);
        btnComprar = findViewById(R.id.btnComprarProd);
        btnExluir = findViewById(R.id.btnExcluirProd);

        prodImage = findViewById(R.id.prod_image);
        recyclerView = findViewById(R.id.recyclerViewProdutos);

        SearchProd = findViewById(R.id.txtPesquisaProd);
        progressBar = findViewById(R.id.progressBar);
        NomeProd = findViewById(R.id.txtNomeProd);
        ValorProd = findViewById(R.id.txtValorProd);
        TipoProd = findViewById(R.id.txtTipo);
        QuantProd = findViewById(R.id.txtQuant);
        ValProd = findViewById(R.id.txtValidade);
        CodProd = findViewById(R.id.txtCodProd);
        FornProd = findViewById(R.id.txtFornecedor);
    }

    /** Configura o RecyclerView e seu adapter */
    private void configurarRecyclerView() {
        // Layout linear (lista vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Divisores entre itens
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Adapter com listener de clique nos itens
        adapter = new ProdutoAdapter(listaProdutos, this, produto -> {
            produtoSelecionado = produto; // Armazena o produto selecionado

            // Preenche os campos com os dados do produto
            NomeProd.setText(produto.getNomeProduto());
            ValorProd.setText(String.valueOf(produto.getValorProduto()));
            TipoProd.setText(produto.getTipoProduto());

            // Carrega imagem se existir
            if (produto.getImgProduto() != null && !produto.getImgProduto().isEmpty()) {
                Bitmap bitmap = ImageUtils.base64ToBitmap(produto.getImgProduto());
                prodImage.setImageBitmap(bitmap);
            } else {
                prodImage.setImageResource(R.drawable.placeholder);
            }

        });

        recyclerView.setAdapter(adapter);
    }

    /** Obtém informações do usuário a partir do token JWT */
    private void obterInfoUsuario() {
        SuperClassUser.TokenInfo userInfo = AuthUtils.getUserInfoFromToken(this);
        if (userInfo != null) {
            userId = userInfo.getUserId();
            String email = userInfo.getEmail();
            String role = userInfo.getRole();
        }
    }

    /** Configura a busca com debounce para evitar chamadas excessivas à API */
    private void configurarBusca() {
        SearchProd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancela busca anterior se ainda pendente
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String termoBusca = s.toString().trim();
                // Agenda nova busca após delay (debounce)
                runnable = () -> buscarPorNomeApi1(termoBusca);
                handler.postDelayed(runnable, DELAY_MILLIS);
            }
        });
    }

    /** Busca produtos por nome na API */
    private void buscarPorNomeApi1(String nome) {
        Call<List<SuperClassProd.ProdutoDto>> call = apiService.buscarProdutosPorNomeSimilar(token, nome);

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

    /** Carrega todos os produtos da API */
    private void carregarProdutos() {
        Call<List<SuperClassProd.ProdutoDto>> call = apiService.buscarTodosProdutos(token);

        call.enqueue(new Callback<List<SuperClassProd.ProdutoDto>>() {
            @Override
            public void onResponse(Call<List<SuperClassProd.ProdutoDto>> call,
                                   Response<List<SuperClassProd.ProdutoDto>> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    List<SuperClassProd.ProdutoDto> produtos = response.body();
                    adapter.atualizarLista(produtos);
                } else {
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

    /** Configura o imagePicker para seleção de fotos */
    private void configurarImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result ->
                {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imagemUri = result.getData().getData();
                        try {
                            // Converte a imagem selecionada para Bitmap
                            Bitmap bitmap = BitmapFactory.decodeStream(
                                    getContentResolver().openInputStream(imagemUri));
                            prodImage.setImageBitmap(bitmap);

                            // Converte para Base64 e armazena
                            String bx = ImageUtils.bitmapToBase64(bitmap);
                            fotoProd = bx;

                            // Exibe a imagem novamente (para verificação)
                            Bitmap b = ImageUtils.base64ToBitmap(bx);
                            prodImage.setImageBitmap(b);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(ProdutosActivity.this,
                                    "Erro ao carregar imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Listener para clique na imagem do produto
        prodImage.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(ProdutosActivity.this,
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery(); // Já tem permissão
            } else {
                // Solicita permissão
                ActivityCompat.requestPermissions(
                        ProdutosActivity.this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_CODE_GALLERY
                );
            }
        });
    }

    /** Configura listeners para os botões */
    private void configurarListeners() {
        btnExluir.setOnClickListener(view -> {/*deletarProduto();*/});
        btnComprar.setOnClickListener(view -> realizarCompra());
        btnVenderProd.setOnClickListener(view -> realizarVenda());
        btnAdicionarProd.setOnClickListener(view -> criarProduto());
        btnAlterarProd.setOnClickListener(view -> {/*alterarProduto();*/});

        // Listener para o campo de validade
        ValProd.setOnClickListener(view -> {
            DatePickerUtil.showDatePicker(ProdutosActivity.this, ValProd, false);
        });
    }

    /** Realiza uma venda do produto selecionado */
    private void realizarVenda() {
        // Validações
        if (produtoSelecionado == null) {
            Toast.makeText(this, "Selecione um produto primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

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

        // Cálculos
        double valorTotal = produtoSelecionado.getValorProduto() * quantidade;
        String lote = "LOTE-" + System.currentTimeMillis();

        // Cria DTOs para a venda
        SuperClassProd.ItemVendaDto itemVenda = new SuperClassProd.ItemVendaDto(
                0, lote, quantidade, 1, 0.00, valorTotal);

        List<SuperClassProd.ItemVendaDto> itens = new ArrayList<>();
        itens.add(itemVenda);

        SuperClassProd.PedidoDto pedido = new SuperClassProd.PedidoDto(valorTotal);
        SuperClassProd.PedidoVendaCompletoDto pedidoVenda = new SuperClassProd.PedidoVendaCompletoDto(pedido, itens);

        // Chamada à API
        Call<SuperClassProd.PedidoVendaCompletoDto> call = apiService.criarPedidoVenda(token, pedidoVenda);

        call.enqueue(new Callback<SuperClassProd.PedidoVendaCompletoDto>() {
            @Override
            public void onResponse(Call<SuperClassProd.PedidoVendaCompletoDto> call,
                                   Response<SuperClassProd.PedidoVendaCompletoDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProdutosActivity.this,
                            "Venda realizada com sucesso!",
                            Toast.LENGTH_LONG).show();
                    limparCampos();
                    produtoSelecionado = null;
                    carregarProdutos();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Erro desconhecido";
                        Toast.makeText(ProdutosActivity.this,
                                "Erro: " + (response.code() == 400 ? "Dados inválidos" : errorBody),
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(ProdutosActivity.this,
                                "Erro ao processar resposta",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuperClassProd.PedidoVendaCompletoDto> call, Throwable t) {
                Toast.makeText(ProdutosActivity.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("VENDA_ERROR", "Falha ao realizar venda", t);
            }
        });
    }

    /** Formata data para ISO 8601 */
    public String formatarData(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(data);
    }

    /** Realiza uma compra do produto selecionado */
    private void realizarCompra() {
        // Validações
        if (produtoSelecionado == null) {
            Toast.makeText(this, "Selecione um produto primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

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

        // Validação da data
        String validadetxt = ValProd.getText().toString();
        String validade = formatarParaISO8601(validadetxt, formatoEntrada);
        if (validade == null) {
            Toast.makeText(this, "Data de validade inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cálculos
        double valorTotal = produtoSelecionado.getValorProduto() * quantidade;
        String lote = "LOTE-" + System.currentTimeMillis();

        // Cria DTOs para a compra
        SuperClassProd.ItemCompraDto itemCompra = new SuperClassProd.ItemCompraDto(
                0, 0, validade, lote, quantidade, 1, valorTotal);

        List<SuperClassProd.ItemCompraDto> itens = new ArrayList<>();
        itens.add(itemCompra);

        SuperClassProd.PedidoDto pedido = new SuperClassProd.PedidoDto(valorTotal);
        SuperClassProd.PedidoCompraCompletoDto pedidoCompra = new SuperClassProd.PedidoCompraCompletoDto(pedido, itens);

        // Chamada à API
        Call<SuperClassProd.PedidoCompraCompletoDto> call = apiService.criarPedidoCompra(token, pedidoCompra);

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
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Erro desconhecido";
                        Toast.makeText(ProdutosActivity.this,
                                "Erro: " + (response.code() == 400 ? "Dados inválidos" : errorBody),
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(ProdutosActivity.this,
                                "Erro ao processar resposta",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuperClassProd.PedidoCompraCompletoDto> call, Throwable t) {
                Toast.makeText(ProdutosActivity.this,
                        "Erro na conexão. Tente novamente.",
                        Toast.LENGTH_SHORT).show();
                Log.e("COMPRA_ERROR", "Falha ao realizar compra", t);
            }
        });
    }

    /** Converte data para formato ISO 8601 */
    public String formatarParaISO8601(String dataOriginal, String formatoOriginal) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat(formatoOriginal, Locale.getDefault());
            Date date = parser.parse(dataOriginal);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Cria um novo produto */
    private void criarProduto() {
        // Validações
        String codProduto = CodProd.getText().toString().trim();
        String nomeProduto = NomeProd.getText().toString().trim();
        String valorProdutoStr = ValorProd.getText().toString().trim();
        String tipoProduto = TipoProd.getText().toString().trim();

        if (codProduto.isEmpty() || nomeProduto.isEmpty() ||
                valorProdutoStr.isEmpty() || tipoProduto.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

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

        // Cria DTO do produto
        SuperClassProd.ProdutoDto produtoDto = new SuperClassProd.ProdutoDto(
                codProduto, nomeProduto, valorProduto, tipoProduto, fotoProd);

        // Chamada à API
        Call<Void> call = apiService.cadastrarProduto(token, produtoDto);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProdutosActivity.this,
                            "Produto criado com sucesso!",
                            Toast.LENGTH_SHORT).show();
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
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Erro na chamada API", t);
            }
        });
    }

    /** Obtém o token do usuário com prefixo Bearer */
    private String obterTokenUsuario() {
        String token = AuthUtils.getToken(this);
        if (token != null && !token.isEmpty()) {
            return "Bearer " + token;
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /** Limpa os campos do formulário */
    private void limparCampos() {
        CodProd.setText("");
        NomeProd.setText("");
        ValorProd.setText("");
        TipoProd.setText("");
        CodProd.requestFocus();
    }

    /** Trata erros da API */
    private void tratarErroApi(Response<Void> response) {
        try {
            String errorBody = response.errorBody() != null ?
                    response.errorBody().string() : "Erro desconhecido";

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

    /** Abre a galeria para seleção de imagem */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
}