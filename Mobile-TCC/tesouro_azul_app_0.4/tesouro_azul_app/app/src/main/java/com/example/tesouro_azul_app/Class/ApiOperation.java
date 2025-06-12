package com.example.tesouro_azul_app.Class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesouro_azul_app.Pages.MainActivity;
import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.Service.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiOperation {
    private final Context context;
    private final ProgressBar progressBar;
    private final TextView txtLoading;
    private final EditText txtCPF_CNPJ;
    private final EditText txtSenha;
    private final Button btnEnter;
    private final TextView txtRegistrar;

    public ApiOperation(Context context,
                        ProgressBar progressBar,
                        TextView txtLoading,
                        EditText txtCPF_CNPJ,
                        EditText txtSenha,
                        Button btnEnter,
                        TextView txtRegistrar) {
        this.context = context;
        this.progressBar = progressBar;
        this.txtLoading = txtLoading;
        this.txtCPF_CNPJ = txtCPF_CNPJ;
        this.txtSenha = txtSenha;
        this.btnEnter = btnEnter;
        this.txtRegistrar = txtRegistrar;
    }

    public void ConectarAPI() {
        ApiService apiService = RetrofitClient.getApiService(context);

        progressBar.setVisibility(View.VISIBLE);
        txtLoading.setText("Verificando conexões...");
        txtLoading.setVisibility(View.VISIBLE);

        txtCPF_CNPJ.setVisibility(View.GONE);
        txtSenha.setVisibility(View.GONE);
        btnEnter.setVisibility(View.GONE);
        txtRegistrar.setVisibility(View.GONE);

        verificarStatusAPI(apiService);
    }

    private void verificarStatusAPI(ApiService apiService) {
        Call<ResponseBody> apiCall = apiService.testarConexaoAPI();
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("API_STATUS", "Resposta da API: " + responseBody);
                        tratarConexaoBemSucedida();

                    } catch (IOException e) {
                        tratarErro("Erro ao ler resposta da API", e);
                    }
                } else {
                    tratarErroAPI(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tratarFalhaConexao(t);
            }
        });
    }



    private void tratarErroAPI(Response<ResponseBody> response) {
        runOnUiThread(() -> {
            try {
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido na API";
                txtLoading.setText("API com problemas");
                Toast.makeText(context, "Problema na API: " + errorBody, Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", errorBody);
            } catch (IOException e) {
                tratarErro("Erro ao processar resposta da API", e);
            }
            agendarNovaTentativa();
        });
    }



    private void tratarFalhaConexao(Throwable t) {
        runOnUiThread(() -> {
            txtLoading.setText("Sem conexão");
            Toast.makeText(context, "Erro de rede: " + t.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("NETWORK_ERROR", "Erro de rede", t);
            agendarNovaTentativa();
        });
    }

    private void tratarErro(String mensagem, Exception e) {
        runOnUiThread(() -> {
            txtLoading.setText("Erro no sistema");
            Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show();
            Log.e("SYSTEM_ERROR", mensagem, e);
            agendarNovaTentativa();
        });
    }

    private void agendarNovaTentativa() {
        runOnUiThread(() -> {
            new Handler().postDelayed(this::ConectarAPI, 5000);
        });
    }

    private void tratarConexaoBemSucedida() {
        runOnUiThread(() -> {
            txtLoading.setText("Tudo conectado com sucesso!");
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                txtLoading.setVisibility(View.GONE);
                txtCPF_CNPJ.setVisibility(View.VISIBLE);
                txtSenha.setVisibility(View.VISIBLE);
                btnEnter.setVisibility(View.VISIBLE);
                txtRegistrar.setVisibility(View.VISIBLE);
            }, 1000);
        });
    }

    private void runOnUiThread(Runnable action) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(action);
        }
    }

    public void realizarLogin(String email, String senha) {
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(context, "Preencha email e senha", Toast.LENGTH_SHORT).show();
            return;
        }

        SuperClassUser.LoginRequestDto loginDto = new SuperClassUser.LoginRequestDto(email, senha);
        ApiService apiService = RetrofitClient.getApiService(context);
        Call<SuperClassUser.LoginResponseDto> call = apiService.loginUsuario(loginDto);

        call.enqueue(new Callback<SuperClassUser.LoginResponseDto>() {
            @Override
            public void onResponse(Call<SuperClassUser.LoginResponseDto> call,
                                   Response<SuperClassUser.LoginResponseDto> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    SuperClassUser.LoginResponseDto loginResponse = response.body();

                    SharedPreferences sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("jwt_token", loginResponse.getToken());
                    editor.apply();

                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }

                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";

                        if (response.code() == 401) {
                            Toast.makeText(context, "Email ou senha inválidos", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Toast.makeText(context, "Usuário inativo", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Erro: " + errorBody, Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        Toast.makeText(context, "Erro ao processar resposta", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuperClassUser.LoginResponseDto> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginError", "Erro: ", t);
            }
        });
    }
}

