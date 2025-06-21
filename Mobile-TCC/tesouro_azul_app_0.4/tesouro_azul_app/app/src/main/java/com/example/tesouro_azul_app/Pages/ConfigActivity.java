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

import com.example.tesouro_azul_app.Class.ImagemDto;
import com.example.tesouro_azul_app.Class.SuperClassProd;
import com.example.tesouro_azul_app.Class.SuperClassUser;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigActivity extends AppCompatActivity {

    // Constantes
    private static final String TAG = "ConfigActivity";
    private static final int REQUEST_CODE_GALLERY = 1001;
    private static final String PREF_NAME = "ThemePrefs";
    private static final String NIGHT_MODE_KEY = "night_mode";
    private static final long PROGRESS_DIALOG_DELAY_MS = 1000L;

    // Views
    private ImageView Xleave, themeIcon;
    private RelativeLayout trocarSenha, SairConta, ExcluirConta;
    private ShapeableImageView userIcon;
    private TextView UserName, UserEmail;

    private Uri imagemSelecionadaUri;

    // Dados
    private String fotox;
    private Bitmap bitmap;
    private String token;
    private int userId;
    private String email;
    private String role;

    // Serviços e Utilitários
    private ApiService apiService;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        initViews();
        setupApiService();
        setupUserInfo();
        setupThemeSwitch();
        setupGalleryLauncher();
        setupClickListeners();
        buscarImagemUsuario();
    }

    private void initViews() {
        userIcon = findViewById(R.id.User_icon);
        UserEmail = findViewById(R.id.UserEmail);
        UserName = findViewById(R.id.UserName);
        SwitchMaterial swicthTheme = findViewById(R.id.switchTheme);
        themeIcon = findViewById(R.id.ThemeMode);
        Xleave = findViewById(R.id.Xleave);
        trocarSenha = findViewById(R.id.trocarSenha);
        SairConta = findViewById(R.id.SairConta);
        ExcluirConta = findViewById(R.id.ExcluirConta);
    }

    private void setupApiService() {
        apiService = RetrofitClient.getApiService(getApplicationContext());
    }

    private void setupUserInfo() {
        token = obterTokenUsuario();
        SuperClassUser.TokenInfo userInfo = AuthUtils.getUserInfoFromToken(this);

        if (userInfo != null) {
            userId = userInfo.getUserId();
            email = userInfo.getEmail();
            role = userInfo.getRole();
            updateUserInfoViews();
        }
    }

    private void updateUserInfoViews() {
        UserEmail.setText(email);
        UserName.setText(email);
    }

    private void setupThemeSwitch() {
        SwitchMaterial swicthTheme = findViewById(R.id.switchTheme);
        swicthTheme.setChecked(ThemeManager.getNightModePreference(this));

        swicthTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeManager.saveNightModePreference(this, isChecked);
            ThemeManager.applySavedTheme(this);
            updateThemeIcon(isChecked);
            recreate();
        });

        updateThemeIcon(swicthTheme.isChecked());
    }

    //Configura o envio do Icon
    private void setupGalleryLauncher() {
        try {
            galleryLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            imagemSelecionadaUri = result.getData().getData();
                            if (imagemSelecionadaUri!= null) {
                                handleSelectedImage(imagemSelecionadaUri);  // Aqui você processa a imagem
                            }
                        }
                    });
        } catch (Exception e) {
            showToast("Erro ao configurar galeria");
            Log.e(TAG, "Erro ao configurar galleryLauncher", e);
        }
    }


    private void setupClickListeners() {
        Xleave.setOnClickListener(view -> navigateToMainActivity());
        userIcon.setOnClickListener(view -> escolherImagem());
        trocarSenha.setOnClickListener(view -> { /* Implementar quando necessário */ });
        SairConta.setOnClickListener(view -> deslogarUsuario());
        ExcluirConta.setOnClickListener(view -> desativarUsuario());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void updateThemeIcon(boolean isNightMode) {
        if (isNightMode) {
            themeIcon.setImageResource(R.drawable.nigth_mode_icon);
            themeIcon.setBackgroundResource(R.drawable.round_back_night);
        } else {
            themeIcon.setImageResource(R.drawable.ligth_mode_icon);
            themeIcon.setBackgroundResource(R.drawable.round_back_white);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (galleryLauncher != null) {
            galleryLauncher.unregister();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                showToast("Permissão negada. Não é possível alterar a imagem.");
                if (PermissionUtils.shouldShowRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    showPermissionRationaleDialog();
                }
            }
        }
    }

    private void showPermissionRationaleDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permissão necessária")
                .setMessage("Você precisa permitir o acesso à galeria para alterar a imagem.")
                .setPositiveButton("OK", (dialog, which) -> {
                    PermissionUtils.requestPermission(this, Manifest.permission.READ_MEDIA_IMAGES, REQUEST_CODE_GALLERY);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void escolherImagem() {
        if (PermissionUtils.isPermissionGranted(this, Manifest.permission.READ_MEDIA_IMAGES)) {
            openGallery();
        } else {
            PermissionUtils.requestPermission(this, Manifest.permission.READ_MEDIA_IMAGES, REQUEST_CODE_GALLERY);
        }
    }

    private void handleSelectedImage(Uri imageUri) {
        try {
            Bitmap originalBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

            if (originalBitmap == null) {
                Log.e("UPLOAD_IMAGEM", "Bitmap original veio nulo!");
                showToast("Erro ao carregar imagem.");
                return;
            }

            atualizarImagemUsuario(originalBitmap);
        } catch (FileNotFoundException e) {
            showToast("Erro ao carregar imagem");
            Log.e(TAG, "Erro ao abrir imagem", e);
        }
    }


    private void atualizarImagemUsuario(Bitmap bitmap) {
        String imagemBase64 = ImageUtils.bitmapToBase64(bitmap);

        Log.d(TAG, "Convertendo imagem para Base64. Tamanho: " + (imagemBase64 != null ? imagemBase64.length() : "null"));

        if (imagemBase64 == null || imagemBase64.isEmpty()) {
            Log.e(TAG, "Imagem Base64 vazia! Abortando envio.");
            Toast.makeText(this, "Imagem inválida. Tente novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        ImagemDto imagemDto = new ImagemDto(imagemBase64);

        Call<ResponseBody> call = apiService.atualizarImagem(token, imagemDto);

        Log.d(TAG, "Iniciando envio da imagem para o servidor...");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Resposta ao atualizar imagem: Código " + response.code());

                if (response.isSuccessful()) {
                    showToast("Imagem enviada com sucesso!");
                    buscarImagemUsuario();
                } else {
                    Log.e(TAG, "Erro ao enviar imagem: Código " + response.code());
                    Toast.makeText(getApplicationContext(), "Erro ao enviar imagem: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Falha na requisição de atualização de imagem", t);
                Toast.makeText(getApplicationContext(), "Falha na requisição: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String obterTokenUsuario() {
        String rawToken = AuthUtils.getToken(ConfigActivity.this);
        if (rawToken != null && !rawToken.isEmpty()) {
            return "Bearer " + rawToken;
        } else {
            showToast("Usuário não autenticado");
            return null;
        }
    }

    private void deslogarUsuario() {
        new AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Deseja realmente sair da sua conta?")
                .setPositiveButton("Sim", (dialog, which) -> executarLogout())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void executarLogout() {
        ProgressDialog progressDialog = createProgressDialog("Saindo...");
        progressDialog.show();

        AuthUtils.logout(this);
        limparDadosSessao();
        RetrofitClient.resetClient();
        apiService = RetrofitClient.getApiService(getApplicationContext());

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            progressDialog.dismiss();
            redirecionarParaLogin();
        }, PROGRESS_DIALOG_DELAY_MS);
    }

    private ProgressDialog createProgressDialog(String message) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    private void buscarImagemUsuario() {
        Log.d(TAG, "Iniciando busca da imagem do usuário...");

        Call<ResponseBody> call = apiService.buscarImagemUsuario(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Resposta da API buscarImagemUsuario: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseString = response.body().string();
                        Log.d(TAG, "Resposta JSON da imagem: " + responseString);

                        JSONObject jsonObject = new JSONObject(responseString);
                        String imagemBase64 = jsonObject.getString("imagemBase64");

                        byte[] imageBytes = Base64.decode(imagemBase64, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                        userIcon.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao processar imagem do usuário", e);
                        showToast("Erro ao carregar imagem do usuário.");
                    }
                } else {
                    Log.e(TAG, "Erro ao buscar imagem: Código " + response.code());
                    showToast("Erro ao buscar imagem: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Falha na requisição buscarImagemUsuario", t);
                showToast("Falha na conexão ao buscar imagem.");
            }
        });
    }


    private void showToast(String message) {
        Toast.makeText(ConfigActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void limparDadosSessao() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        prefs.edit()
                .remove("ultimo_usuario")
                .remove("configuracoes_usuario")
                .apply();
    }

    private void redirecionarParaLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void desativarUsuario() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmação")
                .setMessage("Deseja realmente desativar sua conta?")
                .setPositiveButton("Sim", (dialog, which) -> iniciarProcessoDesativacao())
                .setNegativeButton("Não", null)
                .show();
    }

    private void iniciarProcessoDesativacao() {
        ProgressDialog progressDialog = createProgressDialog("Desativando conta...");
        progressDialog.show();

        Log.d(TAG, "Iniciando desativação de conta para o usuário: " + userId);

        Call<ResponseBody> call = apiService.desativarUsuario(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Resposta da API de desativação: Código " + response.code());
                progressDialog.dismiss();
                handleDesativacaoResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Falha na API ao desativar conta", t);
                progressDialog.dismiss();
                showToast("Falha na conexão. Tente novamente.");
            }
        });
    }

    private void handleDesativacaoResponse(Response<ResponseBody> response) {
        try {
            if (response.isSuccessful()) {
                Log.d(TAG, "Desativação bem-sucedida. Processando resposta...");
                handleSuccessfulDesativacao(response);
            } else {
                Log.w(TAG, "Desativação falhou. Código: " + response.code());
                handleFailedDesativacao(response);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao processar resposta da desativação", e);
            showToast("Erro ao processar resposta");
        }
    }


    private void handleSuccessfulDesativacao(Response<ResponseBody> response) throws Exception {
        String jsonResponse = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonResponse);

        if (jsonObject.has("mensagem") && jsonObject.getString("mensagem").contains("sucesso")) {
            showToast("Conta desativada com sucesso");
            deslogarUsuario();
        } else {
            showToast("Resposta inesperada do servidor");
        }
    }

    private void handleFailedDesativacao(Response<ResponseBody> response) throws Exception {
        String errorBody = response.errorBody().string();
        JSONObject errorObject = new JSONObject(errorBody);

        if (response.code() == 400 && errorBody.contains("já está desativado")) {
            showToast("Sua conta já está desativada");
        } else if (response.code() == 404) {
            showToast("Usuário não encontrado");
        } else {
            showToast("Erro ao desativar conta: " + response.code());
        }
    }
}