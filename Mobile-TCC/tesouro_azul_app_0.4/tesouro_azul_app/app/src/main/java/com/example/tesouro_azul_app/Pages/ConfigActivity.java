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

import android.widget.TextView;
import android.widget.Toast;

import com.example.tesouro_azul_app.Class.SuperClassUser;
import com.example.tesouro_azul_app.EntradaActivity;
import com.example.tesouro_azul_app.LoginActivity;
import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.R;
import com.example.tesouro_azul_app.Service.RetrofitClient;
import com.example.tesouro_azul_app.Util.AuthUtils;
import com.example.tesouro_azul_app.Util.ImageUtils;
import com.example.tesouro_azul_app.Util.PermissionUtils;
import com.example.tesouro_azul_app.Util.ThemeManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigActivity extends AppCompatActivity {

    private String fotox;

    private SharedPreferences sharedPreferences;

    private Bitmap bitmap;

    private String token;
    private ImageView Xleave,themeIcon;
    private RelativeLayout trocarSenha,SairConta,ExcluirConta;

    // Nome do arquivo de preferências e chave booleana usada para salvar o modo escuro
    private static final String PREF_NAME = "ThemePrefs";
    private static final String NIGHT_MODE_KEY = "night_mode";

    //Declara uma constante para identificar o código da requisição da galeria,
    // o ícone do usuário e o launcher para abrir a galeria e receber o resultado.
    private static final int REQUEST_CODE_GALLERY = 1001;

    private ShapeableImageView userIcon;
    private ApiService apiService;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    TextView UserName,UserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applySavedTheme(this);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_config);

        userIcon = findViewById(R.id.User_icon);
        UserEmail = findViewById(R.id.UserEmail);
        UserName = findViewById(R.id.UserName);
        SwitchMaterial swicthTheme = findViewById(R.id.switchTheme);

        themeIcon = findViewById(R.id.ThemeMode);
        Xleave = (ImageView) findViewById(R.id.Xleave);
        trocarSenha = (RelativeLayout) findViewById(R.id.trocarSenha);
        SairConta = (RelativeLayout) findViewById(R.id.SairConta);
        ExcluirConta = (RelativeLayout) findViewById(R.id.ExcluirConta);

        // Configura Retrofit
        apiService = RetrofitClient.getApiService(getApplicationContext());

        // Define o estado inicial do Switch de acordo com a preferência
        swicthTheme.setChecked(ThemeManager.getNightModePreference(this));

        token = obterTokenUsuario();

        // Carregar nome e email
        buscarNomeEmailUsuario();

        // Carregar foto de perfil
        buscarImagemUsuario();

        //Prepara o launcher para abrir a galeria e, se o usuário selecionar uma imagem, define essa imagem como o novo ícone do usuário.
        try {
            galleryLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            handleSelectedImage(imageUri);
                        }
                    });

        }catch (Exception e) {
            Toast.makeText(this, "Erro ao resgatar imagem", Toast.LENGTH_SHORT).show();
        }

        Xleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Ao clicar no ícone do usuário, verifica se tem permissão para acessar a galeria.
        // Se não tiver, solicita. Se tiver, abre a galeria para o usuário escolher uma imagem.
        userIcon.setOnClickListener(view -> {escolherImagem();});

        // Listener para detectar mudanças no Switch
        swicthTheme.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            ThemeManager.saveNightModePreference(this, isChecked);
            ThemeManager.applySavedTheme(this);
            updateThemeIcon(isChecked);
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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent); // Usa o launcher em vez de startActivityForResult
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
                if (PermissionUtils.shouldShowRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Permissão necessária")
                            .setMessage("Você precisa permitir o acesso à galeria para alterar a imagem.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                PermissionUtils.requestPermission(this, Manifest.permission.READ_MEDIA_IMAGES, REQUEST_CODE_GALLERY);
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                }

            }
        }
    }

    private void escolherImagem()
    {
        // Já tem permissão → Abre direto
        if (PermissionUtils.isPermissionGranted(this, Manifest.permission.READ_MEDIA_IMAGES)) {
            openGallery();
        } else {
            PermissionUtils.requestPermission(this, Manifest.permission.READ_MEDIA_IMAGES, REQUEST_CODE_GALLERY);
        }
    }

    private void handleSelectedImage(Uri imageUri) {
        try {
            Bitmap originalBitmap = BitmapFactory.decodeStream(
                    getContentResolver().openInputStream(imageUri));

            // Reduz o tamanho da imagem antes de enviar
            Bitmap resizedBitmap = ImageUtils.resizeBitmap(originalBitmap, 800);

            // Exibir a imagem na ImageView
            userIcon.setImageBitmap(resizedBitmap);

            // Converter para Base64
            fotox = ImageUtils.bitmapToBase64(resizedBitmap);

            // Enviar para API
            enviarImagem(fotox);

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
            Log.e("IMAGE_ERROR", "Erro ao abrir imagem", e);
        }
    }

    private void enviarImagem(String imagemBase64) {
        ApiService apiService = RetrofitClient.getApiService(this);

        // Cria o DTO
        SuperClassUser.ImagemDto imagemDto = new SuperClassUser.ImagemDto(imagemBase64);

        Call<ResponseBody> call = apiService.atualizarImagem(token, imagemDto);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ConfigActivity.this, "Imagem enviada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConfigActivity.this, "Erro ao enviar imagem: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ConfigActivity.this, "Falha na rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String obterTokenUsuario() {
        token = AuthUtils.getToken(ConfigActivity.this); //
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

        //Recria a conexao
        apiService = RetrofitClient.getApiService(getApplicationContext());

        // 4. Redirecionar para tela de login após um pequeno delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            progressDialog.dismiss();
            redirecionarParaLogin();
        }, 1000); // Delay de 1 segundo para melhor UX
    }

    //Busca o nome e o email
    private void buscarNomeEmailUsuario() {
        Call<SuperClassUser.Usuario> call = apiService.buscarUsuario(token);

        call.enqueue(new Callback<SuperClassUser.Usuario>() {
            @Override
            public void onResponse(Call<SuperClassUser.Usuario> call, Response<SuperClassUser.Usuario> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    String nome = response.body().getNomeUsuario();
                    String email = response.body().getEmailUsuario();

                    UserEmail.setText(email);
                    UserName.setText(nome);

                } else {
                    Toast.makeText(ConfigActivity.this, "Erro ao carregar perfil: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuperClassUser.Usuario> call, Throwable t) {
                Toast.makeText(ConfigActivity.this, "Erro ao buscar nome: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarImagemUsuario() {
        // Verifica se o token está disponível
        if (token == null || token.isEmpty()) {
            showToast("Token de autenticação inválido");
            Log.e("IMAGEM_ERROR", "Token não disponível");
            return;
        }

        Call<ResponseBody> call = apiService.buscarUsuarioFoto(token);
        if (call == null) {
            showToast("Erro na solicitação da imagem");
            Log.e("IMAGEM_ERROR", "Call object é nulo");
            return;
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        handleUnsuccessfulResponse(response);
                        return;
                    }

                    if (response.body() == null) {
                        showToast("Resposta da API vazia");
                        Log.e("IMAGEM_ERROR", "Response body é nulo");
                        return;
                    }

                    processImageResponse(response.body().string());

                } catch (IOException e) {
                    showToast("Erro ao ler resposta da API");
                    Log.e("IMAGEM_ERROR", "IO Error: " + e.getMessage(), e);
                } catch (Exception e) {
                    showToast("Erro inesperado");
                    Log.e("IMAGEM_ERROR", "Unexpected error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.w("IMAGEM_ERROR", "Chamada cancelada");
                    return;
                }

                showToast("Falha na conexão. Tente novamente.");
                Log.e("IMAGEM_ERROR", "Falha na requisição: " + t.getMessage(), t);
            }
        });
    }

    private void processImageResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            if (!jsonObject.has("imagemBase64")) {
                showToast("Usuário não possui imagem");
                return;
            }

            String imagemBase64 = jsonObject.getString("imagemBase64");
            if (imagemBase64 == null || imagemBase64.isEmpty()) {
                showToast("Imagem inválida ou vazia");
                return;
            }

            bitmap = ImageUtils.base64ToBitmap(imagemBase64);
            if (bitmap == null) {
                showToast("Erro ao decodificar imagem");
                return;
            }

            userIcon.setImageBitmap(bitmap);

        } catch (JSONException e) {
            showToast("Erro no formato da resposta");
            Log.e("IMAGEM_ERROR", "JSON Error: " + e.getMessage(), e);
        } catch (Exception e) {
            showToast("Erro ao processar imagem");
            Log.e("IMAGEM_ERROR", "Image processing error: " + e.getMessage(), e);
        }
    }

    private void handleUnsuccessfulResponse(Response<ResponseBody> response) {
        String errorMessage = "Erro na requisição";
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                errorMessage += ": " + errorBody;
                Log.e("IMAGEM_ERROR", "Erro na resposta: " + response.code() + " - " + errorBody);
            } else {
                errorMessage += " (Código: " + response.code() + ")";
                Log.e("IMAGEM_ERROR", "Erro na resposta: " + response.code());
            }
        } catch (IOException e) {
            Log.e("IMAGEM_ERROR", "Erro ao ler errorBody", e);
        }
        showToast(errorMessage);
    }

    private void showToast(String message) {
        Toast.makeText(ConfigActivity.this, message, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, LoginActivity.class);

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
        Call<ResponseBody> call = apiService.desativarUsuario(token);

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
