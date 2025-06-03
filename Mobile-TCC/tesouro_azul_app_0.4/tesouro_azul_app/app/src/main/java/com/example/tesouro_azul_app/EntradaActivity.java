package com.example.tesouro_azul_app;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntradaActivity extends AppCompatActivity
{
    //quando tiver eu botoKKKKK
    private String Host="https://tesouroazul1.hospedagemdesites.ws/api";

    private ProgressBar progressBar;
    private EditText txtNomeReg,txtSenhaReg,txtConfirmSenha,txtEmail,txtCPF_CNPJ_Reg,txtNascimento;

    public static String nomeReg,senhaReg,conSenhaReg,emailReg,CPF_CNPJ_reg,nascReg;
    private EditText txtCPF_CNPJ,txtSenha;

    Button btnEnter,btnRegister;

    CheckBox mostrarSenha,showSenhaReg,showSenhaConfir;

    private TextView txtRegistrar, txtLoading;

    private ApiService apiService;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date dataAtual = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            ApiOperation apiOperation = new ApiOperation();

            super.onCreate(savedInstanceState);
            setContentView(R.layout.entrada);

            // Configura Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://tesouroazul1.hospedagemdesites.ws/api/")// <- Coloque a URL base da sua API aqui
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);

            mostrarSenha = (CheckBox) findViewById(R.id.mostrarSenhas);
            txtCPF_CNPJ = (EditText) findViewById(R.id.txtCPF_CNPJ);
            txtSenha = (EditText) findViewById(R.id.txtSenha);
            btnEnter = (Button) findViewById(R.id.btnEnter);

            String CPF_CNPJ = txtCPF_CNPJ.getText().toString().trim();
            String senha = txtSenha.getText().toString().trim();

            txtRegistrar = (TextView) findViewById(R.id.txtRegistrar);

            progressBar = findViewById(R.id.progressBar);
            txtLoading = findViewById(R.id.progress_text);
            
            apiOperation.ConectarAPI();

            // Verificar se usuário está logado
            if (!AuthUtils.isLoggedIn(this)) {
                startActivity(new Intent(this, EntradaActivity.class));
                finish();
                return;
            }


            btnEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String email = txtCPF_CNPJ.getText().toString().trim();
                    String senha = txtSenha.getText().toString().trim();

                    apiOperation.realizarLogin(email,senha);

                    //Após periodo de testes ajeitar essa parte
                    Intent intent = new Intent(EntradaActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            });
        } catch (Exception e) {}


        if (mostrarSenha.isChecked())
        {
            txtSenha.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else
        {
            txtSenha.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        txtRegistrar.setOnClickListener(v ->
        {
            setContentView(R.layout.registrar);//Muda para a tela de registro

            txtCPF_CNPJ_Reg = (EditText) findViewById(R.id.txtCPF_CNPJ_Reg);
            txtNomeReg = (EditText) findViewById(R.id.txtNomeProd);
            txtSenhaReg = (EditText) findViewById(R.id.txtSenhaReg);
            txtConfirmSenha = (EditText) findViewById(R.id.txtConfirmSenha);
            txtEmail = (EditText) findViewById(R.id.txtEmail);
            txtNascimento = (EditText) findViewById(R.id.txtNascimento);
            btnRegister = (Button) findViewById(R.id.btnRegister);
            showSenhaReg = (CheckBox) findViewById(R.id.mostrarSenha);
            showSenhaConfir = (CheckBox) findViewById(R.id.mostrarSenhaConfir);

            if (showSenhaConfir.isChecked())
            {
                txtConfirmSenha.setInputType(InputType.TYPE_CLASS_TEXT);
            }else{
                txtConfirmSenha.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            if (showSenhaReg.isChecked())
            {
                txtSenhaReg.setInputType(InputType.TYPE_CLASS_TEXT);
            }else{
                txtSenhaReg.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            txtNascimento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerUtil.showDatePicker(EntradaActivity.this, txtNascimento, true);
                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //isso é pra converter data de nascimento,
                    // como somos obrigados a usar uma versão velha do java ela é feia assim mesmo
                    if (validarCadastro(view, getApplicationContext())) {


                        Toast.makeText(EntradaActivity.this, "Validação bem sucedida", Toast.LENGTH_SHORT).show();

                        CriarUsuario();
                    }
                }

                //Utiliza o validarClass para permitir o processo de validação
                public boolean validarCadastro(View view, Context context) {
                    String email = txtEmail.getText().toString().trim();
                    String senhaReg = txtSenhaReg.getText().toString().trim();
                    String senhaConfim = txtConfirmSenha.getText().toString().trim();
                    String nomeReg = txtNomeReg.getText().toString().trim();
                    String CPF_CNPJreg = txtCPF_CNPJ_Reg.getText().toString().trim();
                    String nascimento = txtNascimento.getText().toString().trim();

                    // Verifica se todos os campos foram preenchidos
                    if (email.isEmpty() || senhaReg.isEmpty() || senhaConfim.isEmpty() ||
                            nomeReg.isEmpty() || CPF_CNPJreg.isEmpty() || nascimento.isEmpty()) {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    // Instância do validador
                    ValidarClass validator = new ValidarClass();

                    // Identifica o tipo de documento
                    String tipoDocumento = validator.identificarTipo(CPF_CNPJreg);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date dataNascimento = null;
                    try
                    {
                        dataNascimento = dateFormat.parse(nascimento);
                    }
                    catch (ParseException e)
                    {
                        throw new RuntimeException(e);
                    }

                    try {
                        // Validação de maioridade

                        if (!validator.MaiorIdade(dataNascimento)) {
                            Toast.makeText(context, "Menores de idade não são permitidos", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        // Validação de senha
                        if (!senhaReg.equals(senhaConfim)) {
                            Toast.makeText(context, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        // Validação de documento
                        switch (tipoDocumento) {
                            case "CPF":
                                if (!validator.validarCPF(CPF_CNPJreg)) {
                                    Toast.makeText(context, "CPF inválido", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                break;

                            case "CNPJ":
                                if (!validator.validarCNPJ(CPF_CNPJreg)) {
                                    Toast.makeText(context, "CNPJ inválido", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                break;

                            default:
                                Toast.makeText(context, "Documento inválido", Toast.LENGTH_SHORT).show();
                                return false;
                        }

                        // Validação da data de nascimento
                        try {
                             dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            dataNascimento = dateFormat.parse(nascimento);

                        } catch (ParseException e) {
                            Toast.makeText(context, "Formato de data inválido", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    } catch (Exception e) {
                        //Se algo deu errado
                        Toast.makeText(context, "Erro ao processar dados", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return false;
                    }

                    return true; // Se passou por todas as validações
                }

                class ValidarClass {

                    //retira todos os caractéres especiais como pontos e traços e define se é CPF
                    public boolean isCPF(String CPF_CNJPreg) {
                        return CPF_CNJPreg != null && CPF_CNJPreg.replaceAll("\\D", "").length() == 11;
                    }

                    //retira todos os caractéres especiais como pontos e traços e define se é CNPJ
                    public boolean isCNPJ(String CFP_CNPJreg) {
                        return CFP_CNPJreg != null && CFP_CNPJreg.replaceAll("\\D", "").length() == 14;
                    }

                    //responsavel por informar o tipo, alterando o valor da string tipo para outras operações
                    public String identificarTipo(String CPF_CNJPreg) {
                        if (isCPF(CPF_CNJPreg)) return "CPF";
                        if (isCNPJ(CPF_CNJPreg)) return "CNPJ";
                        return "Invalido";
                    }

                    //lógica para verificar se o cpf é valido
                    public boolean validarCPF(String CPF_CNJPreg) {
                        CPF_CNJPreg = CPF_CNJPreg.replaceAll("\\D", "");

                        //detecta todos os numeros repitidos
                        if (CPF_CNJPreg.length() != 11 || CPF_CNJPreg.matches("(\\d)\\1{10}"))
                            return false;

                        int soma = 0, peso = 10;
                        for (int i = 0; i < 9; i++) soma += (CPF_CNJPreg.charAt(i) - '0') * peso--;
                        int digito1 = 11 - (soma % 11);
                        if (digito1 >= 10) digito1 = 0;

                        soma = 0;
                        peso = 11;
                        for (int i = 0; i < 10; i++) soma += (CPF_CNJPreg.charAt(i) - '0') * peso--;
                        int digito2 = 11 - (soma % 11);
                        if (digito2 >= 10) digito2 = 0;

                        return CPF_CNJPreg.endsWith(digito1 + "" + digito2);
                    }

                    //lógica para verificar se a cnpj é valida
                    public boolean validarCNPJ(String CFP_CNPJreg) {
                        CFP_CNPJreg = CFP_CNPJreg.replaceAll("\\D", "");
                        if (CFP_CNPJreg.length() != 14 || CFP_CNPJreg.matches("(\\d)\\1{13}"))
                            return false;

                        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
                        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

                        int soma = 0;
                        for (int i = 0; i < 12; i++)
                            soma += (CFP_CNPJreg.charAt(i) - '0') * pesos1[i];
                        int digito1 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

                        soma = 0;
                        for (int i = 0; i < 13; i++)
                            soma += (CFP_CNPJreg.charAt(i) - '0') * pesos2[i];
                        int digito2 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

                        return CFP_CNPJreg.endsWith(digito1 + "" + digito2);
                    }

                    public boolean MaiorIdade(Date nascimento) {
                        Calendar hoje = Calendar.getInstance();
                        Calendar dataNascimento = Calendar.getInstance();
                        dataNascimento.setTime(nascimento);

                        int idade = hoje.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR);
                        if (hoje.get(Calendar.DAY_OF_YEAR) < dataNascimento.get(Calendar.DAY_OF_YEAR)) {
                            idade--;
                        }

                        return idade >= 18;
                    }
                }

                public void CriarUsuario() {
                    String CPF_CNPJreg = txtCPF_CNPJ_Reg.getText().toString().trim();
                    ValidarClass validator = new ValidarClass();

                    // Validação dos campos (usando o método existente)
                    if (!validarCadastro(v, EntradaActivity.this)) {
                        return; // Se a validação falhar, interrompe a execução
                    }

                    // Identifica o tipo de documento
                    String tipoDocumento = validator.identificarTipo(CPF_CNPJreg);

                    String CPF_USUARIO = null;
                    String CNPJ_USUARIO = null;

                    if ("CPF".equals(tipoDocumento)) {
                        CPF_USUARIO = CPF_CNPJreg.replaceAll("\\D", ""); // Remove caracteres não numéricos
                    } else if ("CNPJ".equals(tipoDocumento)) {
                        CNPJ_USUARIO = CPF_CNPJreg.replaceAll("\\D", ""); // Remove caracteres não numéricos
                    } else {
                        Toast.makeText(EntradaActivity.this, "Documento inválido!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String STATUS_USUARIO = "a"; // "a" para ativo (ou ajuste conforme sua regra)
                    String EMAIL_USUARIO = txtEmail.getText().toString().trim();
                    String SENHA_USUARIO = txtSenhaReg.getText().toString().trim();
                    String NOME_USUARIO = txtNomeReg.getText().toString().trim();
                    String birthUser = txtNascimento.getText().toString().trim();

                    // Formatar a data para o padrão esperado pela API (yyyy-MM-dd)
                    SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd");
                    String DATA_NASC_USUARIO = "";

                    try {
                        Date date = sdfInput.parse(birthUser);
                        DATA_NASC_USUARIO = sdfOutput.format(date);
                    } catch (ParseException e) {
                        Toast.makeText(EntradaActivity.this, "Formato de data inválido!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Cria o objeto CriarUsuarioDto
                    SuperClassUser.CriarUsuarioDto usuarioDto = new SuperClassUser.CriarUsuarioDto(
                            NOME_USUARIO,
                            EMAIL_USUARIO,
                            DATA_NASC_USUARIO,
                            CPF_USUARIO,
                            CNPJ_USUARIO,
                            1, // ID_ASSINATURA_FK (valor padrão 1, ajuste conforme necessário)
                            null, // FOTO_USUARIO (pode ser null ou string base64)
                            SENHA_USUARIO,
                            STATUS_USUARIO
                    );

                    // Conversão para JSON
                    Gson gson = new Gson();
                    String json = gson.toJson(usuarioDto);
                    Log.d("UsuarioPost", json); // Para verificar no Logcat(é uma ferramenta de linha de comando que despeja um registro de mensagens do sistema,
                    // incluindo mensagens que você escreveu no app com a classe Log)

                    // Usa o RetrofitClient já configurado
                    ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
                    Call<SuperClassUser.Usuario> call = apiService.criarUsuario(usuarioDto);

                    call.enqueue(new Callback<SuperClassUser.Usuario>() {
                        @Override
                        public void onResponse(Call<SuperClassUser.Usuario> call, Response<SuperClassUser.Usuario> response)
                        {
                            if (response.isSuccessful())
                            {
                                Toast.makeText(EntradaActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EntradaActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                try {
                                    String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";
                                    Toast.makeText(EntradaActivity.this, "Erro no cadastro: " + errorBody, Toast.LENGTH_LONG).show();
                                    Log.e("API Error", "Código: " + response.code() + ", Mensagem: " + errorBody);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SuperClassUser.Usuario> call, Throwable t) {
                            Toast.makeText(EntradaActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("API Failure", "Erro: ", t);
                        }
                    });
                }

            });
        });
    }

    public class ApiOperation {
        private void ConectarAPI() {
            // 1. Obter instância do ApiService
            ApiService apiService = RetrofitClient.getApiService(getApplicationContext());

            // 2. Mostrar estado de carregamento
            progressBar.setVisibility(View.VISIBLE);
            txtLoading.setText("Verificando conexões...");
            txtLoading.setVisibility(View.VISIBLE);

            // 3. Esconder componentes da UI durante o carregamento
            txtCPF_CNPJ.setVisibility(View.GONE);
            txtSenha.setVisibility(View.GONE);
            btnEnter.setVisibility(View.GONE);
            txtRegistrar.setVisibility(View.GONE);

            // 4. Primeiro verifica o status da API
            verificarStatusAPI(apiService);
        }

        private void verificarStatusAPI(ApiService apiService) {
            Call<ResponseBody> apiCall = apiService.testarConexaoAPI();
            apiCall.enqueue(new Callback<ResponseBody>()
            {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful())
                    {
                        try {
                            String responseBody = response.body().string();
                            Log.d("API_STATUS", "Resposta da API: " + responseBody);

                            // Se API está OK, verifica o banco de dados
                            verificarStatusBanco(apiService);
                        } catch (IOException e)
                        {
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

        private void verificarStatusBanco(ApiService apiService) {
            Call<ResponseBody> bancoCall = apiService.testarConexaoBanco();
            bancoCall.enqueue(new Callback<ResponseBody>()
            {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                    if (response.isSuccessful())
                    {
                        try {
                            String responseBody = response.body().string();
                            Log.d("BANCO_STATUS", "Resposta do banco: " + responseBody);
                            tratarConexaoBemSucedida();
                        } catch (IOException e) {
                            tratarErro("Erro ao ler resposta do banco", e);
                        }
                    } else {
                        tratarErroBanco(response);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    tratarFalhaConexao(t);
                }
            });
        }

        // Método auxiliar para tratamento de sucesso
        private void tratarConexaoBemSucedida() {
            //Garante que as atualizações de UI rodem na thread principal
            runOnUiThread(() ->
            {
                txtLoading.setText("Tudo conectado com sucesso!");

                // Aguardar 1 segundo antes de mostrar a interface
                new Handler().postDelayed(() -> {
                    progressBar.setVisibility(View.GONE);
                    txtLoading.setVisibility(View.GONE);

                    // Mostrar componentes da UI
                    txtCPF_CNPJ.setVisibility(View.VISIBLE);
                    txtSenha.setVisibility(View.VISIBLE);
                    btnEnter.setVisibility(View.VISIBLE);
                    txtRegistrar.setVisibility(View.VISIBLE);
                }, 1000);
            });
        }

        // Métodos auxiliares para tratamento de erros específicos
        private void tratarErroAPI(Response<ResponseBody> response)
        {
            runOnUiThread(() -> {
                try {
                    String errorBody = response.errorBody() != null ?
                            response.errorBody().string() : "Erro desconhecido na API";

                    String errorMsg = "Problema na API: " + errorBody;
                    txtLoading.setText("API com problemas");
                    Toast.makeText(EntradaActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", errorMsg);

                } catch (IOException e) {
                    tratarErro("Erro ao processar resposta da API", e);
                }
                agendarNovaTentativa();
            });
        }

        private void tratarErroBanco(Response<ResponseBody> response) {
            runOnUiThread(() -> {
                try {
                    String errorBody = response.errorBody() != null ?
                            response.errorBody().string() : "Erro desconhecido no banco";

                    String errorMsg = "Problema no banco de dados: " + errorBody;
                    txtLoading.setText("Banco offline");
                    Toast.makeText(EntradaActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("BANCO_ERROR", errorMsg);

                } catch (IOException e) {
                    tratarErro("Erro ao processar resposta do banco", e);
                }
                agendarNovaTentativa();
            });
        }

        private void tratarFalhaConexao(Throwable t) {
            runOnUiThread(() -> {
                txtLoading.setText("Sem conexão");
                String errorMsg = "Erro de rede: " + t.getMessage();

                Toast.makeText(EntradaActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                Log.e("NETWORK_ERROR", errorMsg, t);

                agendarNovaTentativa();
            });
        }

        private void tratarErro(String mensagem, Exception e) {
            runOnUiThread(() -> {
                txtLoading.setText("Erro no sistema");
                Toast.makeText(EntradaActivity.this, mensagem, Toast.LENGTH_LONG).show();
                Log.e("SYSTEM_ERROR", mensagem, e);
                agendarNovaTentativa();
            });
        }

        // Método auxiliar para agendar nova tentativa
        private void agendarNovaTentativa() {
            runOnUiThread(() -> {
                new Handler().postDelayed(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        ConectarAPI();
                    }
                }, 5000);
            });
        }

        private void mostrarErro (String mensagem){
            Toast.makeText(EntradaActivity.this,mensagem,Toast.LENGTH_LONG).show();

            //volta a tentar após alguns segundos
            new Handler().postDelayed(this::ConectarAPI, 3000);
        }

        //Depois arrume, quando o miguel melhorar
        //Serve para login
        public void realizarLogin(String email, String senha) {

            // Validação básica
            if (email.isEmpty() || senha.isEmpty())
            {
                Toast.makeText(EntradaActivity.this, "Preencha email e senha", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mostrar progresso
            progressBar.setVisibility(View.VISIBLE);

            // Criar DTO de login
            SuperClassUser.LoginRequestDto loginDto = new SuperClassUser.LoginRequestDto(email, senha);

            // Chamar API
            ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
            Call<SuperClassUser.LoginResponseDto> call = apiService.loginUsuario(loginDto);

            call.enqueue(new Callback<SuperClassUser.LoginResponseDto>()
            {
                @Override
                public void onResponse(Call<SuperClassUser.LoginResponseDto> call, Response<SuperClassUser.LoginResponseDto> response)
                {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null)
                    {
                        // Login bem-sucedido
                        SuperClassUser.LoginResponseDto loginResponse = response.body();

                        // Armazenar o token JWT
                        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("jwt_token", loginResponse.getToken());
                        editor.apply();

                        //  Navegar para a tela principal
                        Intent intent = new Intent(EntradaActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Tratar erros da API
                        try {
                            String errorBody = response.errorBody() != null ?
                                    response.errorBody().string() : "Erro desconhecido";

                            if (response.code() == 401) {
                                Toast.makeText(EntradaActivity.this,
                                        "Email ou senha inválidos", Toast.LENGTH_LONG).show();
                            } else if (response.code() == 400) {
                                Toast.makeText(EntradaActivity.this,
                                        "Usuário inativo", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EntradaActivity.this,
                                        "Erro: " + errorBody, Toast.LENGTH_LONG).show();
                            }

                        } catch (IOException e) {
                            Toast.makeText(EntradaActivity.this,
                                    "Erro ao processar resposta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SuperClassUser.LoginResponseDto> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EntradaActivity.this,
                            "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginError", "Erro: ", t);
                }
            });
        }
    }
}