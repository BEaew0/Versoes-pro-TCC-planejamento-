package com.example.tesouro_azul_app.Pages;


import android.app.Activity;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.Toast;

import com.example.tesouro_azul_app.EntradaActivity;
import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.MainActivity;
import com.example.tesouro_azul_app.R;
import com.example.tesouro_azul_app.Service.RetrofitClient;
import com.example.tesouro_azul_app.Util.AuthUtils;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class ConfigActivity extends AppCompatActivity {

    public static String fotox;

    private SharedPreferences sharedPreferences;

    private Bitmap bitmap;

    private String tokenUser = obterTokenUsuario();
    ImageView Xleave,themeIcon;
    RelativeLayout trocarSenha,SairConta,ExcluirConta;

    // Nome do arquivo de preferências e chave booleana usada para salvar o modo escuro
    private static final String PREF_NAME = "ThemePrefs";
    private static final String NIGHT_MODE_KEY = "night_mode";

    //Declara uma constante para identificar o código da requisição da galeria,
    // o ícone do usuário e o launcher para abrir a galeria e receber o resultado.
    private static final int REQUEST_CODE_GALLERY = 1001;
    private static final int PICK_IMAGE = 1;

    private int PICK_IMAGE_REQUEST = 2;
    private ShapeableImageView userIcon;
    private Uri filePath;
    private ApiService apiService;

    String bx;
    Uri imagemUri;
    Bitmap b;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    // Antes de carregar o layout, verificamos o tema salvo
    sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

    // Recupera se o modo escuro estava ativado na última vez
    boolean isNightMode = sharedPreferences.getBoolean(NIGHT_MODE_KEY, false);

    // Define o tema do app conforme a preferência salva
    if (isNightMode) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_config);

    userIcon.setImageBitmap(b);
    SwitchMaterial swicthTheme = findViewById(R.id.switchTheme);
    userIcon = findViewById(R.id.User_icon);
    themeIcon = findViewById(R.id.ThemeMode);
    Xleave = (ImageView) findViewById(R.id.Xleave);
    trocarSenha = (RelativeLayout) findViewById(R.id.trocarSenha);
    SairConta = (RelativeLayout) findViewById(R.id.SairConta);
    ExcluirConta = (RelativeLayout) findViewById(R.id.ExcluirConta);

        // Configura Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tesouroazul1.hospedagemdesites.ws/api/")// <- Coloque a URL base da sua API aqui
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Define o estado inicial do Switch de acordo com a preferência
    swicthTheme.setChecked(isNightMode);
    updateThemeIcon(isNightMode);

        //Prepara o launcher para abrir a galeria e, se o usuário selecionar uma imagem, define essa imagem como o novo ícone do usuário.
        try {
            galleryLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result ->
                    {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
                        {
                            Uri imageUri = result.getData().getData();
                            userIcon.setImageURI(imageUri);
                        }
                    });

        }catch (Exception e) {
            Toast.makeText(this, "Erro ao resgatar imagem", Toast.LENGTH_SHORT).show();
        }

        buscarImagemUsuario();

        Xleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //Ao clicar no ícone do usuário, verifica se tem permissão para acessar a galeria.
        // Se não tiver, solicita. Se tiver, abre a galeria para o usuário escolher uma imagem.
        userIcon.setOnClickListener(view ->
        {
            if (ContextCompat.checkSelfPermission(ConfigActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
            {
                openGallery(); // Já tem permissão → Abre direto
            }
            else
            {
                // Solicita permissão
                ActivityCompat.requestPermissions(
                        ConfigActivity.this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_CODE_GALLERY
                );
            }
        });


        // Listener para detectar mudanças no Switch
        swicthTheme.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                // Se o usuário ativar, muda para o modo escuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveThemePreference(true);
                updateThemeIcon(true);// Salva essa escolha


            } else
            {
                // Se o usuário desativar, volta para o modo claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveThemePreference(false);
                updateThemeIcon(false);// Salva essa escolha
            }

            // Atualiza a tela com o novo tema
            recreate();
        });

        trocarSenha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {}
        });

        SairConta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {deslogarUsuario();}
        });

        ExcluirConta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            desativarUsuario();
            }
        });

    }

    // Função auxiliar para salvar a preferência no SharedPreferences
    private void saveThemePreference(boolean isNight)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NIGHT_MODE_KEY, isNight); // Salva o valor true ou false
        editor.apply(); // Aplica as mudanças
    }

    private void updateThemeIcon(boolean isNightMode)
    {
        if(isNightMode){
            themeIcon.setImageResource(R.drawable.nigth_mode_icon);
            themeIcon.setBackgroundResource(R.drawable.round_back_night);
        }
        else {
            themeIcon.setImageResource(R.drawable.ligth_mode_icon);
            themeIcon.setBackgroundResource(R.drawable.round_back_white);
        }

    }

    //Quando a activity for destruida limpa ActivityResultLauncher evitando vazamento de memoria
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (galleryLauncher != null)
        {
            galleryLauncher.unregister(); // Libera o registro do launcher
        }
    }

    private void openGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_GALLERY) // Verifica se é a permissão da galeria
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Permissão concedida → Abre a galeria
                openGallery();
            }
            else
            {
                // Permissão negada → Mostra mensagem ou desabilita funcionalidade
                Toast.makeText(this, "Permissão negada. Não é possível alterar a imagem.", Toast.LENGTH_SHORT).show();

                // Verifica se o usuário *não* marcou "Nunca perguntar de novo"
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES))
                {
                    new AlertDialog.Builder(this)
                            .setTitle("Permissão necessária")
                            .setMessage("Você precisa permitir o acesso à galeria para alterar a imagem.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                // Solicita novamente a permissão
                                ActivityCompat.requestPermissions(
                                        this,
                                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                                        REQUEST_CODE_GALLERY
                                );
                            })
                            .setNegativeButton("Cancelar", null) // Não faz nada se cancelar
                            .show();
                }
            }
        }
    }

    //converte a imagem para POST
    public String imagem_string(Bitmap fotox)
    {
        ByteArrayOutputStream data = new ByteArrayOutputStream();

        // Comprime o bitmap em formato JPEG com 100% de qualidade
        fotox.compress(Bitmap.CompressFormat.JPEG, 100, data);

        // Converte o bitmap em um array de bytes
        byte[] b1 = data.toByteArray();

        // Codifica os bytes em uma string Base64
        return Base64.encodeToString(b1, Base64.DEFAULT);
    }

    //Converte a imagem a view
    public Bitmap getFoto(String s)
    {
        // Decodifica a string Base64 de volta para um array de bytes
        byte[] decodes = Base64.decode(s, Base64.DEFAULT);

        // Converte o array de bytes para um objeto Bitmap
        return BitmapFactory.decodeByteArray(decodes, 0, decodes.length);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifica se a imagem foi selecionada com sucesso
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imagemUri = data.getData(); // Obtém o URI da imagem

            try {
                // Carrega a imagem a partir do URI (Uniform Resource Identifier,
                // ou Identificador Uniforme de Recursos) é uma string (sequência de caracteres) que se refere a um recurso

                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imagemUri));

                userIcon.setImageBitmap(bitmap); // Define a imagem no ImageView

                // Converte o bitmap para string Base64
                bx = imagem_string(bitmap);

                // Armazena a string Base64 em uma variável
                fotox = bx;

                // Reconverte a Base64 para Bitmap (talvez para validar ou reutilizar)
                Bitmap b = getFoto(bx);

                // Atualiza novamente o ImageView com o bitmap reconvertido
                userIcon.setImageBitmap(b);

            } catch (FileNotFoundException e) {
                e.printStackTrace(); // Exibe erro caso o arquivo não seja encontrado
            }
        }
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

    private void deslogarUsuario() {
        new AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Deseja realmente sair da sua conta?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // 2. Executar logout quando usuário confirmar
                    executarLogout();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

     // Executa o processo de logout de forma segura
    private void executarLogout() {
        // Mostrar progresso
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saindo...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // 1. Limpar token e dados de autenticação
        AuthUtils.logout(this);

        // 2. Limpar qualquer dado de sessão adicional (se necessário)
        limparDadosSessao();

        // 3. Fechar qualquer conexão ativa
        RetrofitClient.resetClient();

        // 4. Redirecionar para tela de login após um pequeno delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            progressDialog.dismiss();
            redirecionarParaLogin();
        }, 1000); // Delay de 1 segundo para melhor UX
    }

    private void buscarImagemUsuario() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando imagem...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String token = AuthUtils.getToken(this);
        if (token == null) {
            progressDialog.dismiss();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<ResponseBody> call = apiService.buscarUsuarioFoto("Bearer " + token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    try {
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        if (jsonObject.has("imagemBase64")) {
                            String imagemBase64 = jsonObject.getString("imagemBase64");
                            bitmap = getFoto(imagemBase64);

                            userIcon.setImageBitmap(bitmap);
                        } else
                        {
                            Toast.makeText(ConfigActivity.this,
                                    "Usuário não possui imagem", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(ConfigActivity.this,
                                "Erro ao processar imagem", Toast.LENGTH_SHORT).show();
                        Log.e("IMAGEM_ERROR", "Erro: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ConfigActivity.this,
                        "Falha na conexão", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Erro: " + t.getMessage());
            }
        });
    }

    //Limpa dados adicionais da sessão do usuário
    private void limparDadosSessao() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        prefs.edit()
                .remove("ultimo_usuario")
                .remove("configuracoes_usuario")
                .apply();
    }

    private void redirecionarParaLogin() {
        Intent intent = new Intent(this, EntradaActivity.class);

        // Configura flags para limpar a pilha de atividades
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish(); // Finaliza a atividade atual
    }

    private void desativarUsuario(){
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Deseja realmente sair da sua conta?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // 2. Executar logout quando usuário confirmar
                    Desativar();
                })
                .setNegativeButton("Não", null)
                .show();

    }

    private void Desativar() {
        // Mostrar diálogo de progresso
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Desativando conta...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<ResponseBody> call = apiService.desativarUsuario(tokenUser);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    try {
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Verifica se a resposta contém a mensagem de sucesso
                        if (jsonObject.has("mensagem") && jsonObject.getString("mensagem").contains("sucesso")) {

                            // Desativação bem-sucedida - fazer logout e redirecionar
                            Toast.makeText(ConfigActivity.this,
                                    "Conta desativada com sucesso", Toast.LENGTH_LONG).show();

                            // Realizar logout
                            deslogarUsuario();

                            // Redirecionar para tela de login
                           redirecionarParaLogin();

                        } else {
                            // Resposta inesperada da API
                            Toast.makeText(ConfigActivity.this,
                                    "Resposta inesperada do servidor", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(ConfigActivity.this,
                                "Erro ao processar resposta", Toast.LENGTH_SHORT).show();
                        Log.e("DESATIVAR_ERROR", "Erro: " + e.getMessage());
                    }
                } else {
                    // Tratar erros específicos da API
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorBody);

                        if (response.code() == 400 && errorBody.contains("já está desativado")) {
                            Toast.makeText(ConfigActivity.this,
                                    "Sua conta já está desativada", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 404) {
                            Toast.makeText(ConfigActivity.this,
                                    "Usuário não encontrado", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ConfigActivity.this,
                                    "Erro ao desativar conta: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(ConfigActivity.this,
                                "Erro ao desativar conta", Toast.LENGTH_SHORT).show();
                        Log.e("DESATIVAR_ERROR", "Erro: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ConfigActivity.this,
                        "Falha na conexão. Tente novamente.", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Erro: " + t.getMessage());
            }
        });
    }
}
