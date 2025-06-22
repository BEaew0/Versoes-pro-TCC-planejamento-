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
    // Tag para logs
    private static final String TAG = "ProdutosActivity";

    // Produto atualmente selecionado na lista
    private SuperClassProd.ProdutoDtoArray produtoSelecionado = null;

    // Código para solicitação de permissão de galeria
    private static final int REQUEST_CODE_GALLERY = 1001;

    // Launcher para seleção de imagens
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    // Variável para armazenar a foto do produto em Base64
    private static String fotoProd;

    // Componentes de UI
    private RecyclerView recyclerView;
    private ProdutoAdapter adapter;
    private List<SuperClassProd.ProdutoDtoArray> listaProdutos = new ArrayList<>();

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
    int idSelecionado ;
    int userId;

    // Formato de data
    String formatoEntrada = "dd/MM/yyyy HH:mm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtosctivity);
        Log.d(TAG, "onCreate: Activity sendo criada");

        try {
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

            carregarProdutos();
        } catch (Exception e) {
            Log.e(TAG, "Erro durante a inicialização da Activity", e);
            Toast.makeText(this, "Erro ao inicializar a tela de produtos", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /** Inicializa todas as views do layout */
    private void inicializarViews() {
        try {
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

            Log.d(TAG, "Views inicializadas com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inicializar views", e);
            throw new RuntimeException("Falha ao inicializar componentes da interface", e);
        }
    }

    /** Configura o RecyclerView e seu adapter */
    private void configurarRecyclerView() {
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            adapter = new ProdutoAdapter(listaProdutos, this, produto -> {
                try {
                    produtoSelecionado = produto; // Agora é do tipo ProdutoDtoArray
                    Log.d(TAG, "Produto selecionado: " + produto.getNomeProduto());

                    idSelecionado = produto.getIdProduto();
                    Log.d(TAG, "ID do Produto selecionado: " + idSelecionado);

                    NomeProd.setText(produto.getNomeProduto());
                    ValorProd.setText(String.valueOf(produto.getValorProduto()));
                    TipoProd.setText(produto.getTipoProduto());

                    if (produto.getImgProduto() != null && !produto.getImgProduto().isEmpty()) {
                        Bitmap bitmap = ImageUtils.base64ToBitmap(produto.getImgProduto());
                        prodImage.setImageBitmap(bitmap);
                        Log.d(TAG, "Imagem do produto carregada");
                    } else {
                        prodImage.setImageResource(R.drawable.placeholder);
                        Log.d(TAG, "Imagem padrão carregada (sem imagem do produto)");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao selecionar produto", e);
                    Toast.makeText(ProdutosActivity.this, "Erro ao carregar produto", Toast.LENGTH_SHORT).show();
                }
            });

            recyclerView.setAdapter(adapter);
            Log.d(TAG, "RecyclerView configurado com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao configurar RecyclerView", e);
            Toast.makeText(this, "Erro ao configurar a lista de produtos", Toast.LENGTH_SHORT).show();
        }
    }


    /** Carrega todos os produtos da API */
    private void carregarProdutos() {
        Log.d(TAG, "Carregando lista de produtos");

        if (token == null) {
            Log.e(TAG, "Token não disponível para carregar produtos");
            Toast.makeText(this, "Erro de autenticação", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        Call<List<SuperClassProd.ProdutoDtoArray>> call = apiService.buscarTodosProdutos(token);

        call.enqueue(new Callback<List<SuperClassProd.ProdutoDtoArray>>() {
            @Override
            public void onResponse(Call<List<SuperClassProd.ProdutoDtoArray>> call,
                                   Response<List<SuperClassProd.ProdutoDtoArray>> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        List<SuperClassProd.ProdutoDtoArray> produtos = response.body();
                        Log.d(TAG, "Produtos carregados: " + produtos.size());
                        adapter.atualizarLista(produtos);
                    } else {
                        String errorMsg = "Erro ao carregar produtos: " + response.code();
                        if (response.errorBody() != null) {
                            errorMsg += " - " + response.errorBody().string();
                        }
                        Log.e(TAG, errorMsg);
                        Toast.makeText(ProdutosActivity.this,
                                "Erro ao carregar produtos",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao processar lista de produtos", e);
                    Toast.makeText(ProdutosActivity.this, "Erro ao processar produtos", Toast.LENGTH_SHORT).show();
                } finally {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<SuperClassProd.ProdutoDtoArray>> call, Throwable t) {
                Log.e(TAG, "Falha ao carregar produtos", t);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProdutosActivity.this,
                        "Falha na conexão. Verifique sua internet.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /** Obtém informações do usuário a partir do token JWT */
    private void obterInfoUsuario() {
        try {
            SuperClassUser.TokenInfo userInfo = AuthUtils.getUserInfoFromToken(this);
            if (userInfo != null) {
                userId = userInfo.getUserId();
                String email = userInfo.getEmail();
                String role = userInfo.getRole();
                Log.d(TAG, "Usuário autenticado - ID: " + userId + ", Email: " + email + ", Role: " + role);
            } else {
                Log.w(TAG, "Token de usuário não contém informações válidas");
                Toast.makeText(this, "Sessão inválida. Faça login novamente.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao obter informações do usuário", e);
            Toast.makeText(this, "Erro ao verificar autenticação", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /** Configura a busca com debounce para evitar chamadas excessivas à API */
    private void configurarBusca() {
        try {
            SearchProd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Não precisa fazer nada aqui
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Cancela qualquer busca anterior agendada (debounce)
                    if (runnable != null) {
                        handler.removeCallbacks(runnable);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        String termoBusca = s.toString().trim();
                        Log.d(TAG, "Texto alterado na busca: " + termoBusca);

                        if (termoBusca.isEmpty()) {
                            // Se o campo estiver vazio, carrega todos os produtos
                            carregarProdutos();
                        } else {
                            // Agenda a nova busca com um pequeno delay (debounce)
                            runnable = () -> buscarPorNomeApi1(termoBusca);
                            handler.postDelayed(runnable, DELAY_MILLIS);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "Erro durante afterTextChanged", e);
                    }
                }
            });

            Log.d(TAG, "Busca configurada com sucesso");

        } catch (Exception e) {
            Log.e(TAG, "Erro ao configurar busca", e);
            Toast.makeText(this, "Erro ao configurar busca", Toast.LENGTH_SHORT).show();
        }
    }


    /** Busca produtos por nome na API */
    private void buscarPorNomeApi1(String nome) {
        Log.d(TAG, "Iniciando busca por nome: " + nome);

        if (token == null) {
            Log.e(TAG, "Token não disponível para busca");
            Toast.makeText(this, "Erro de autenticação", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<List<SuperClassProd.ProdutoDtoArray>> call = apiService.buscarProdutosPorNomeSimilar(token, nome);

        call.enqueue(new Callback<List<SuperClassProd.ProdutoDtoArray>>() {
            @Override
            public void onResponse(Call<List<SuperClassProd.ProdutoDtoArray>> call,
                                   Response<List<SuperClassProd.ProdutoDtoArray>> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        List<SuperClassProd.ProdutoDtoArray> produtos = response.body();
                        Log.d(TAG, "Busca retornou " + produtos.size() + " produtos");

                        if (!produtos.isEmpty()) {
                            adapter.atualizarLista(produtos);
                        } else {
                            Log.d(TAG, "Nenhum produto encontrado para: " + nome);
                            Toast.makeText(ProdutosActivity.this, "Nenhum produto encontrado.", Toast.LENGTH_SHORT).show();
                            adapter.atualizarLista(new ArrayList<>()); // Limpa a lista
                        }
                    } else {
                        String errorMsg = "Erro ao buscar: " + response.code();
                        if (response.errorBody() != null) {
                            errorMsg += " - " + response.errorBody().string();
                        }
                        Log.e(TAG, errorMsg);
                        Toast.makeText(ProdutosActivity.this, "Erro ao buscar produtos", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao processar resposta da busca", e);
                    Toast.makeText(ProdutosActivity.this, "Erro ao processar resultados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SuperClassProd.ProdutoDtoArray>> call, Throwable t) {
                Log.e(TAG, "Falha na busca por nome: " + nome, t);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProdutosActivity.this, "Erro na conexão. Verifique sua internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    /** Configura o imagePicker para seleção de fotos */
    private void configurarImagePicker() {
        try {
            imagePickerLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), result -> {
                        try {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                Uri imagemUri = result.getData().getData();
                                Log.d(TAG, "Imagem selecionada: " + imagemUri.toString());

                                try {
                                    // Converte a imagem selecionada para Bitmap
                                    Bitmap bitmap = BitmapFactory.decodeStream(
                                            getContentResolver().openInputStream(imagemUri));
                                    prodImage.setImageBitmap(bitmap);

                                    // Converte para Base64 e armazena
                                    String bx = ImageUtils.bitmapToBase64(bitmap);
                                    fotoProd = bx;
                                    Log.d(TAG, "Imagem convertida para Base64");

                                    // Exibe a imagem novamente (para verificação)
                                    Bitmap b = ImageUtils.base64ToBitmap(bx);
                                    prodImage.setImageBitmap(b);
                                } catch (FileNotFoundException e) {
                                    Log.e(TAG, "Arquivo de imagem não encontrado", e);
                                    Toast.makeText(ProdutosActivity.this,
                                            "Erro ao carregar imagem",
                                            Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e(TAG, "Erro ao processar imagem", e);
                                    Toast.makeText(ProdutosActivity.this,
                                            "Erro ao processar imagem selecionada",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d(TAG, "Seleção de imagem cancelada ou falhou");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Erro no resultado do imagePicker", e);
                        }
                    }
            );

            // Listener para clique na imagem do produto
            prodImage.setOnClickListener(view -> {
                try {
                    if (ContextCompat.checkSelfPermission(ProdutosActivity.this,
                            Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Permissão já concedida, abrindo galeria");
                        openGallery();
                    } else {
                        Log.d(TAG, "Solicitando permissão para acessar galeria");
                        ActivityCompat.requestPermissions(
                                ProdutosActivity.this,
                                new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                                REQUEST_CODE_GALLERY
                        );
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao solicitar permissão de galeria", e);
                    Toast.makeText(ProdutosActivity.this, "Erro ao acessar galeria", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d(TAG, "ImagePicker configurado com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao configurar ImagePicker", e);
            Toast.makeText(this, "Erro ao configurar seleção de imagens", Toast.LENGTH_SHORT).show();
        }
    }

    /** Configura listeners para os botões */
    private void configurarListeners() {
        try {
            btnExluir.setOnClickListener(view -> {deletarProduto(idSelecionado);});
            btnComprar.setOnClickListener(view -> realizarCompra());
            btnVenderProd.setOnClickListener(view -> realizarVenda());
            btnAdicionarProd.setOnClickListener(view -> criarProduto());
            btnAlterarProd.setOnClickListener(view -> {/*alterarProduto();*/});

            // Listener para o campo de validade
            ValProd.setOnClickListener(view -> {
                try {
                    DatePickerUtil.showDatePicker(ProdutosActivity.this, ValProd, false);
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao mostrar DatePicker", e);
                    Toast.makeText(ProdutosActivity.this, "Erro ao selecionar data", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d(TAG, "Listeners configurados com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao configurar listeners", e);
            Toast.makeText(this, "Erro ao configurar botões", Toast.LENGTH_SHORT).show();
        }
    }

    /** Realiza uma venda do produto selecionado */
    private void realizarVenda() {
        Log.d(TAG, "Iniciando processo de venda");

        try {
            // Validações
            if (produtoSelecionado == null) {
                Log.w(TAG, "Tentativa de venda sem produto selecionado");
                Toast.makeText(this, "Selecione um produto primeiro", Toast.LENGTH_SHORT).show();
                return;
            }

            String quantidadeStr = QuantProd.getText().toString().trim();

            double quantidade;
            try {
                quantidade = Integer.parseInt(quantidadeStr);
                if (quantidade <= 0) {
                    Log.w(TAG, "Tentativa de venda com quantidade inválida: " + quantidade);
                    Toast.makeText(this, "Quantidade deve ser maior que zero", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Quantidade inválida para venda: " + quantidadeStr, e);
                Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cálculos
            double valorTotal = produtoSelecionado.getValorProduto() * quantidade;
            String lote = "LOTE-" + System.currentTimeMillis();
            Log.d(TAG, "Preparando venda - Produto: " + produtoSelecionado.getNomeProduto() +
                    ", Quantidade: " + quantidade + ", Valor Total: " + valorTotal);

            // Cria DTOs para a venda
            SuperClassProd.ItemVendaDto itemVenda = new SuperClassProd.ItemVendaDto(
                    idSelecionado, lote, quantidade, 0, 0.00, valorTotal);

            List<SuperClassProd.ItemVendaDto> itens = new ArrayList<>();
            itens.add(itemVenda);

            SuperClassProd.VendaDto pedido = new SuperClassProd.VendaDto(valorTotal);
            SuperClassProd.PedidoVendaCompletoDto pedidoVenda = new SuperClassProd.PedidoVendaCompletoDto(pedido, itens);

            // Chamada à API
            Call<SuperClassProd.PedidoVendaCompletoDto> call = apiService.criarPedidoVenda(token, pedidoVenda);
            progressBar.setVisibility(View.VISIBLE);

            call.enqueue(new Callback<SuperClassProd.PedidoVendaCompletoDto>() {
                @Override
                public void onResponse(Call<SuperClassProd.PedidoVendaCompletoDto> call,
                                       Response<SuperClassProd.PedidoVendaCompletoDto> response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(ProdutosActivity.this,
                                    "Venda realizada com sucesso!",
                                    Toast.LENGTH_LONG).show();
                            limparCampos();
                            produtoSelecionado = null;
                            carregarProdutos();
                        } else {
                            String errorMsg = "Erro na venda: " + response.code();
                            if (response.errorBody() != null) {
                                errorMsg += " - " + response.errorBody().string();
                            }
                            Log.e(TAG, errorMsg);
                            Toast.makeText(ProdutosActivity.this,
                                    "Erro ao realizar venda: " + (response.code() == 400 ? "Dados inválidos" : "Erro no servidor"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao processar resposta da venda", e);
                        Toast.makeText(ProdutosActivity.this,
                                "Erro ao processar venda",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuperClassProd.PedidoVendaCompletoDto> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Falha ao realizar venda", t);
                    Toast.makeText(ProdutosActivity.this,
                            "Falha na conexão. Verifique sua internet.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Log.e(TAG, "Erro durante o processo de venda", e);
            Toast.makeText(this, "Erro ao processar venda", Toast.LENGTH_SHORT).show();
        }
    }

    /** Formata data para ISO 8601 */
    public String formatarData(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.format(data);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao formatar data", e);
            return null;
        }
    }

    /** Realiza uma compra do produto selecionado */
    private void realizarCompra() {
        Log.d(TAG, "Iniciando processo de compra");

        try {
            // Validações
            if (produtoSelecionado == null) {
                Log.w(TAG, "Tentativa de compra sem produto selecionado");
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
                Log.d(TAG, "Quantidade para compra: " + quantidade);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Quantidade inválida para compra: " + quantidadeStr, e);
                Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validação da data
            String validadetxt = ValProd.getText().toString();
            String validade = formatarParaISO8601(validadetxt, formatoEntrada);

            // Cálculos
            double valorTotal = produtoSelecionado.getValorProduto() * quantidade;
            String lote = "LOTE-" + System.currentTimeMillis();
            Log.d(TAG, "Preparando compra - Produto: " + produtoSelecionado.getNomeProduto() +
                    ", Quantidade: " + quantidade + ", Valor Total: " + valorTotal +
                    ", Validade: " + validade);

            // Cria DTOs para a compra
            SuperClassProd.ItemCompraDto itemCompra = new SuperClassProd.ItemCompraDto(
                    idSelecionado, 0, validade, lote, quantidade, 1, valorTotal);

            List<SuperClassProd.ItemCompraDto> itens = new ArrayList<>();
            itens.add(itemCompra);

            SuperClassProd.PedidoDto pedido = new SuperClassProd.PedidoDto(null,valorTotal);
            SuperClassProd.PedidoCompraCompletoDto pedidoCompra = new SuperClassProd.PedidoCompraCompletoDto(pedido, itens);

            // Chamada à API
            Call<SuperClassProd.PedidoCompraCompletoDto> call = apiService.criarPedidoCompra(token, pedidoCompra);
            progressBar.setVisibility(View.VISIBLE);

            call.enqueue(new Callback<SuperClassProd.PedidoCompraCompletoDto>() {
                @Override
                public void onResponse(Call<SuperClassProd.PedidoCompraCompletoDto> call,
                                       Response<SuperClassProd.PedidoCompraCompletoDto> response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(ProdutosActivity.this,
                                    "Compra realizada com sucesso!",
                                    Toast.LENGTH_LONG).show();
                            limparCampos();
                            produtoSelecionado = null;
                            carregarProdutos();
                        } else {
                            String errorMsg = "Erro na compra: " + response.code();
                            if (response.errorBody() != null) {
                                errorMsg += " - " + response.errorBody().string();
                            }
                            Log.e(TAG, errorMsg);
                            Toast.makeText(ProdutosActivity.this,
                                    "Erro ao realizar compra: " + (response.code() == 400 ? "Dados inválidos" : "Erro no servidor"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao processar resposta da compra", e);
                        Toast.makeText(ProdutosActivity.this,
                                "Erro ao processar compra",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuperClassProd.PedidoCompraCompletoDto> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Falha ao realizar compra", t);
                    Toast.makeText(ProdutosActivity.this,
                            "Erro na conexão. Tente novamente.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Log.e(TAG, "Erro durante o processo de compra", e);
            Toast.makeText(this, "Erro ao processar compra", Toast.LENGTH_SHORT).show();
        }
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
            Log.e(TAG, "Erro ao formatar data: " + dataOriginal, e);
            return null;
        }
    }

    private void deletarProduto(int produtoId) {
        Log.d(TAG, "Iniciando exclusão do produto ID: " + produtoId);

        // Chamada para o endpoint de exclusão
        Call<Void> call = apiService.deletarProduto(token, produtoId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Produto excluído com sucesso - ID: " + produtoId);
                        Toast.makeText(ProdutosActivity.this,
                                "Produto excluído com sucesso!",
                                Toast.LENGTH_SHORT).show();
                        carregarProdutos();  // Recarrega a lista de produtos após exclusão
                    } else {
                        Log.e(TAG, "Erro ao excluir produto: Código " + response.code());
                        Toast.makeText(ProdutosActivity.this,
                                "Erro ao excluir produto: " + response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao processar exclusão de produto", e);
                    Toast.makeText(ProdutosActivity.this,
                            "Erro ao processar exclusão",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e(TAG, "Falha na requisição de exclusão", t);
                Toast.makeText(ProdutosActivity.this,
                        "Falha na conexão. Verifique sua internet.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /** Cria um novo produto */
    private void criarProduto() {
        Log.d(TAG, "Iniciando criação de produto");

        try {
            // Validações
            String codProduto = CodProd.getText().toString().trim();
            String nomeProduto = NomeProd.getText().toString().trim();
            String valorProdutoStr = ValorProd.getText().toString().trim();
            String tipoProduto = TipoProd.getText().toString().trim();

            if (codProduto.isEmpty() || nomeProduto.isEmpty() ||
                    valorProdutoStr.isEmpty() || tipoProduto.isEmpty()) {
                Log.w(TAG, "Tentativa de criar produto com campos obrigatórios vazios");
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            double valorProduto;
            try {
                valorProduto = Double.parseDouble(valorProdutoStr);
                if (valorProduto <= 0) {
                    Log.w(TAG, "Tentativa de criar produto com valor inválido: " + valorProdutoStr);
                    Toast.makeText(this, "O valor do produto deve ser positivo", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Valor do produto inválido: " + valorProdutoStr, e);
                Toast.makeText(this, "Valor do produto inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Criando produto - Código: " + codProduto +
                    ", Nome: " + nomeProduto + ", Valor: " + valorProduto +
                    ", Tipo: " + tipoProduto + ", Imagem: " + (fotoProd != null ? "sim" : "não"));

            // Cria DTO do produto
            SuperClassProd.ProdutoDto produtoDto = new SuperClassProd.ProdutoDto(
                    codProduto, nomeProduto, valorProduto, tipoProduto, fotoProd);

            // Chamada à API
            Call<Void> call = apiService.cadastrarProduto(token, produtoDto);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "Produto criado com sucesso: " + nomeProduto);
                            Toast.makeText(ProdutosActivity.this,
                                    "Produto criado com sucesso!",
                                    Toast.LENGTH_SHORT).show();
                            limparCampos();
                            prodImage.setImageResource(R.drawable.placeholder);
                            fotoProd = null;
                            carregarProdutos();
                        } else {
                            tratarErroApi(response);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao processar criação de produto", e);
                        Toast.makeText(ProdutosActivity.this,
                                "Erro ao processar criação",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Log.e(TAG, "Falha ao criar produto", t);
                    Toast.makeText(ProdutosActivity.this,
                            "Falha na conexão. Verifique sua internet.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Erro durante criação de produto", e);
            Toast.makeText(this, "Erro ao criar produto", Toast.LENGTH_SHORT).show();
        }
    }

    /** Obtém o token do usuário com prefixo Bearer */
    private String obterTokenUsuario() {
        try {
            String token = AuthUtils.getToken(this);
            if (token != null && !token.isEmpty()) {
                Log.d(TAG, "Token do usuário obtido com sucesso");
                return "Bearer " + token;
            } else {
                Log.w(TAG, "Token do usuário não disponível");
                Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
                finish();
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao obter token do usuário", e);
            Toast.makeText(this, "Erro de autenticação", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }
    }

    /** Limpa os campos do formulário */
    private void limparCampos() {
        try {
            CodProd.setText("");
            NomeProd.setText("");
            ValorProd.setText("");
            TipoProd.setText("");
            QuantProd.setText("");
            ValProd.setText("");
            CodProd.requestFocus();
            Log.d(TAG, "Campos limpos com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao limpar campos", e);
        }
    }

    /** Trata erros da API */
    private void tratarErroApi(Response<Void> response) {
        try {
            String errorBody = response.errorBody() != null ?
                    response.errorBody().string() : "Erro desconhecido";

            String mensagemErro = "Erro ao criar produto: " + response.code();
            Log.e(TAG, mensagemErro + " - " + errorBody);

            if (response.code() == 400) {
                mensagemErro = "Dados inválidos: " + errorBody;
            } else if (response.code() == 401) {
                mensagemErro = "Acesso não autorizado";
            } else if (response.code() == 500) {
                mensagemErro = "Erro no servidor";
            }

            Toast.makeText(this, mensagemErro, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Erro ao ler errorBody", e);
            Toast.makeText(this, "Erro ao processar resposta do servidor", Toast.LENGTH_SHORT).show();
        }
    }

    /** Abre a galeria para seleção de imagem */
    private void openGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
            Log.d(TAG, "Intent para galeria lançada");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao abrir galeria", e);
            Toast.makeText(this, "Erro ao acessar galeria", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Activity sendo destruída");
        // Limpa qualquer callback pendente do handler
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}