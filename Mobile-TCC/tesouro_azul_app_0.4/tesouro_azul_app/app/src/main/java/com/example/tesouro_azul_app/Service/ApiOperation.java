package com.example.tesouro_azul_app.Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesouro_azul_app.Class.SuperClassUser;
import com.example.tesouro_azul_app.Pages.MainActivity;
import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.Service.RetrofitClient;
import com.example.tesouro_azul_app.Util.AuthUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe responsável por gerenciar todas as operações de comunicação com a API,
 * incluindo conexão, autenticação e tratamento de erros.
 */
public class ApiOperation {
    // Componentes de UI
    private final Context context;
    private final ProgressBar progressBar;
    private final TextView txtLoading;
    private final EditText txtEmail;
    private final EditText txtSenha;
    private final Button btnEnter;
    private final TextView txtRegistrar;
    private final CheckBox mostrarSenhas;

    // Controle de tentativas de reconexão
    private int tentativa = 0;
    private static final int MAX_TENTATIVAS = 5;
    private static final int BACKOFF_INICIAL_MS = 1000; // 1 segundo

    /**
     * Construtor que inicializa todos os componentes de UI necessários
     * para exibir status e interação com o usuário.
     */
    public ApiOperation(Context context,
                        ProgressBar progressBar,
                        TextView txtLoading,
                        EditText txtEmail,
                        EditText txtSenha,
                        Button btnEnter,
                        CheckBox mostrarSenhas,
                        TextView txtRegistrar) {
        this.context = context;
        this.progressBar = progressBar;
        this.txtLoading = txtLoading;
        this.txtEmail = txtEmail;
        this.txtSenha = txtSenha;
        this.btnEnter = btnEnter;
        this.mostrarSenhas = mostrarSenhas;
        this.txtRegistrar = txtRegistrar;
    }

    /**
     * Método principal para iniciar a conexão com a API.
     * Configura a UI para estado de carregamento e inicia a verificação.
     */
    public void ConectarAPI() {
        ApiService apiService = RetrofitClient.getApiService(context);

        // Configura UI para estado de carregamento
        progressBar.setVisibility(View.VISIBLE);
        txtLoading.setText("Verificando conexões...");
        txtLoading.setVisibility(View.VISIBLE);

        // Oculta campos de entrada durante a conexão
        txtEmail.setVisibility(View.GONE);
        txtSenha.setVisibility(View.GONE);
        btnEnter.setVisibility(View.GONE);
        mostrarSenhas.setVisibility(View.GONE);
        txtRegistrar.setVisibility(View.GONE);

        verificarStatusAPI(apiService);
    }

    /**
     * Verifica o status da API através de uma chamada de teste.
     * @param apiService Instância do serviço API configurado
     */
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

    /**
     * Trata erros retornados pela API (respostas não bem-sucedidas).
     * @param response Resposta da API contendo o erro
     */
    private void tratarErroAPI(Response<ResponseBody> response) {
        runOnUiThread(() -> {
            try {
                String errorBody = response.errorBody() != null ?
                        response.errorBody().string() : "Erro desconhecido na API";
                txtLoading.setText("API com problemas");
                Toast.makeText(context, "Problema na API: " + errorBody, Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", errorBody);
            } catch (IOException e) {
                tratarErro("Erro ao processar resposta da API", e);
            }
            agendarNovaTentativaComBackoff();
        });
    }

    /**
     * Trata falhas na conexão com a API.
     * @param t Exceção contendo detalhes do erro
     */
    private void tratarFalhaConexao(Throwable t) {
        runOnUiThread(() -> {
            txtLoading.setText("Sem conexão");
            Toast.makeText(context, "Erro de rede: " + t.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("NETWORK_ERROR", "Erro de rede", t);
            agendarNovaTentativaComBackoff();
        });
    }

    /**
     * Tratamento genérico para erros do sistema.
     * @param mensagem Mensagem amigável para o usuário
     * @param e Exceção original
     */
    private void tratarErro(String mensagem, Exception e) {
        runOnUiThread(() -> {
            txtLoading.setText("Erro no sistema");
            Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show();
            Log.e("SYSTEM_ERROR", mensagem, e);
            agendarNovaTentativaComBackoff();
        });
    }

    /**
     * Agenda uma nova tentativa de conexão usando estratégia de backoff exponencial.
     * Aumenta o intervalo entre tentativas progressivamente.
     */
    private void agendarNovaTentativaComBackoff() {
        runOnUiThread(() -> {
            if (tentativa < MAX_TENTATIVAS) {
                long delay = BACKOFF_INICIAL_MS * (long) Math.pow(2, tentativa);
                tentativa++;

                new Handler().postDelayed(() -> {
                    txtLoading.setText("Tentando reconectar (" + tentativa + "/" + MAX_TENTATIVAS + ")...");
                    ConectarAPI();
                }, delay);
            } else {
                // Máximo de tentativas alcançado
                txtLoading.setText("Falha na conexão");
                progressBar.setVisibility(View.GONE);
                btnEnter.setVisibility(View.VISIBLE);
                btnEnter.setText("Tentar novamente");
                btnEnter.setOnClickListener(v -> {
                    tentativa = 0;
                    ConectarAPI();
                });
            }
        });
    }

    /**
     * Atualiza a UI quando a conexão é estabelecida com sucesso.
     */
    private void tratarConexaoBemSucedida() {
        runOnUiThread(() -> {
            txtLoading.setText("Sucesso");
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                txtLoading.setVisibility(View.GONE);
                txtEmail.setVisibility(View.VISIBLE);
                txtSenha.setVisibility(View.VISIBLE);
                btnEnter.setVisibility(View.VISIBLE);

                mostrarSenhas.setVisibility(View.VISIBLE);

                txtRegistrar.setVisibility(View.VISIBLE);
                btnEnter.setText("Entrar");
                btnEnter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = txtEmail.getText().toString().trim();
                        String senha = txtSenha.getText().toString().trim();

                        realizarLogin(email, senha);
                    }
                });
            }, 1000);
        });
    }

    /**
     * Executa ações na thread principal (UI Thread).
     * @param action Ação a ser executada
     */
    private void runOnUiThread(Runnable action) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(action);
        }
    }

    /**
     * Realiza o processo de autenticação do usuário.
     * @param email Email do usuário
     * @param senha Senha do usuário
     */
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
                    // Login bem-sucedido
                    SuperClassUser.LoginResponseDto loginResponse = response.body();

                    // Armazena o token JWT
                    AuthUtils.saveToken(context, loginResponse.getToken());

                    // Navega para a tela principal
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }

                } else {
                    // Trata diferentes códigos de erro
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Erro desconhecido";

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