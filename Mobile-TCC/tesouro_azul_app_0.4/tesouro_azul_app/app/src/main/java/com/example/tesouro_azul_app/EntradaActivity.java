package com.example.tesouro_azul_app;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tesouro_azul_app.Pages.MainActivity;
import com.example.tesouro_azul_app.Util.DateUtils;
import com.example.tesouro_azul_app.Util.ValidatorUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.Class.SuperClassUser;
import com.example.tesouro_azul_app.Util.DatePickerUtil;
import com.example.tesouro_azul_app.Service.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.content.Context;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntradaActivity extends AppCompatActivity
{
    private EditText txtNomeReg,txtSenhaReg,txtConfirmSenha,txtEmail,txtCPF_CNPJ_Reg,txtNascimento;

    Button btnRegister;

    CheckBox showSenhaReg,showSenhaConfir;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.entrada);

            // Configura Retrofit
            apiService = RetrofitClient.getApiService(getApplicationContext());

            txtCPF_CNPJ_Reg = (EditText) findViewById(R.id.txtCPF_CNPJ_Reg);
            txtNomeReg = (EditText) findViewById(R.id.txtNomeProd);
            txtSenhaReg = (EditText) findViewById(R.id.txtSenhaReg);
            txtConfirmSenha = (EditText) findViewById(R.id.txtConfirmSenha);
            txtEmail = (EditText) findViewById(R.id.txtEmail);
            txtNascimento = (EditText) findViewById(R.id.txtNascimento);
            btnRegister = (Button) findViewById(R.id.btnRegister);
            showSenhaReg = (CheckBox) findViewById(R.id.mostrarSenha);
            showSenhaConfir = (CheckBox) findViewById(R.id.mostrarSenhaConfir);

        txtConfirmSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
        txtSenhaReg.setTransformationMethod(PasswordTransformationMethod.getInstance());

       showSenhaReg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if (showSenhaReg.isChecked()) {
                   // Mostrar senha
                   txtSenhaReg.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
               } else {
                   // Ocultar senha
                   txtSenhaReg.setTransformationMethod(PasswordTransformationMethod.getInstance());
               }
               // Colocar o cursor no final do texto após a mudança
               txtSenhaReg.setSelection(txtSenhaReg.getText().length());
           }
       });

       showSenhaConfir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if (showSenhaConfir.isChecked()) {
                   // Mostrar senha
                   txtConfirmSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
               } else {
                   // Ocultar senha
                   txtConfirmSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
               }
               // Colocar o cursor no final do texto após a mudança
               txtConfirmSenha.setSelection(txtConfirmSenha.getText().length());
           }
       });

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
                    ValidatorUtils validator = new ValidatorUtils(); // Se usar métodos estáticos, nem precisa instanciar


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

                        if (!validator.maiorIdade(dataNascimento)) {
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

                public void CriarUsuario() {
                    // Verificação de nulos nos campos de texto
                    if (txtCPF_CNPJ_Reg.getText() == null || txtEmail.getText() == null ||
                            txtSenhaReg.getText() == null || txtNomeReg.getText() == null ||
                            txtNascimento.getText() == null) {
                        Toast.makeText(EntradaActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String CPF_CNPJreg = txtCPF_CNPJ_Reg.getText().toString().trim();
                    String EMAIL_USUARIO = txtEmail.getText().toString().trim();
                    String SENHA_USUARIO = txtSenhaReg.getText().toString().trim();
                    String NOME_USUARIO = txtNomeReg.getText().toString().trim();
                    String birthUser = txtNascimento.getText().toString().trim();

                    // Validação básica dos campos
                    if (EMAIL_USUARIO.isEmpty() || SENHA_USUARIO.isEmpty() ||
                            NOME_USUARIO.isEmpty() || birthUser.isEmpty() || CPF_CNPJreg.isEmpty()) {
                        Toast.makeText(EntradaActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validação de email
                    if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL_USUARIO).matches()) {
                        Toast.makeText(EntradaActivity.this, "Email inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validação de documento
                    String tipoDocumento = ValidatorUtils.identificarTipo(CPF_CNPJreg);
                    String CPF_USUARIO = null;
                    String CNPJ_USUARIO = null;

                    if ("CPF".equals(tipoDocumento)) {
                        if (!ValidatorUtils.validarCPF(CPF_CNPJreg)) {
                            Toast.makeText(EntradaActivity.this, "CPF inválido", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CPF_USUARIO = CPF_CNPJreg.replaceAll("\\D", "");
                    } else if ("CNPJ".equals(tipoDocumento)) {
                        if (!ValidatorUtils.validarCNPJ(CPF_CNPJreg)) {
                            Toast.makeText(EntradaActivity.this, "CNPJ inválido", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CNPJ_USUARIO = CPF_CNPJreg.replaceAll("\\D", "");
                    } else {
                        Toast.makeText(EntradaActivity.this, "Documento inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Formatação da data
                    SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    sdfInput.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String DATA_NASC_USUARIO;

                    DATA_NASC_USUARIO = DateUtils.converterParaISO(birthUser);

                    // Cria o objeto CriarUsuarioDto com os nomes de campos exatos que a API espera
                    SuperClassUser.Usuario usuarioDto = new SuperClassUser.Usuario(
                            NOME_USUARIO,
                            EMAIL_USUARIO,
                            DATA_NASC_USUARIO,
                            CPF_USUARIO,
                            CNPJ_USUARIO,
                            1,  // ID_ASSINATURA_FK padrão
                            null, // FOTO_USUARIO (pode ser ajustado se tiver imagem)
                            SENHA_USUARIO
                    );

                    // Chamada à API
                    Call<SuperClassUser.Usuario> call = apiService.criarUsuario(usuarioDto);
                    call.enqueue(new Callback<SuperClassUser.Usuario>() {
                        @Override
                        public void onResponse(Call<SuperClassUser.Usuario> call, Response<SuperClassUser.Usuario> response) {

                            if (response.isSuccessful()) {
                                Toast.makeText(EntradaActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EntradaActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Fecha a activity atual
                            } else {
                                tratarErroApi(response);
                            }
                        }

                        @Override
                        public void onFailure(Call<SuperClassUser.Usuario> call, Throwable t) {

                            Toast.makeText(EntradaActivity.this, "Falha na conexão. Tente novamente.", Toast.LENGTH_SHORT).show();
                            Log.e("API Failure", "Erro: ", t);
                        }
                    });
                }

                private void tratarErroApi(Response<SuperClassUser.Usuario> response) {
                    try {
                        String errorMsg = "Erro no cadastro";
                        String errorDetails = "";
                        int errorCode = response.code();

                        // Log detalhado do erro
                        Log.e("API_ERROR", "Código: " + errorCode + ", Mensagem: " + response.message());

                        // Tratamento específico para códigos de erro
                        switch (errorCode) {
                            case 400:
                                errorMsg = "Dados inválidos enviados ao servidor";
                                break;
                            case 401:
                                errorMsg = "Autenticação necessária";
                                break;
                            case 403:
                                errorMsg = "Acesso não autorizado";
                                break;
                            case 404:
                                errorMsg = "Recurso não encontrado";
                                break;
                            case 409:
                                errorMsg = "Conflito - Email ou documento já cadastrado";
                                break;
                            case 500:
                                errorMsg = "Erro interno no servidor";
                                break;
                        }

                        // Extrair detalhes do corpo da resposta
                        if (response.errorBody() != null) {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("API_ERROR_BODY", errorBody);

                                // Tentar parsear como JSON
                                try {
                                    JsonObject jsonError = JsonParser.parseString(errorBody).getAsJsonObject();

                                    if (jsonError.has("mensagem")) {
                                        errorMsg = jsonError.get("mensagem").getAsString();
                                    }
                                    if (jsonError.has("errors")) {
                                        errorDetails = jsonError.get("errors").toString();
                                    }
                                } catch (Exception e) {
                                    // Se não for JSON, usar o corpo diretamente
                                    if (!errorBody.isEmpty()) {
                                        errorDetails = errorBody;
                                    }
                                }
                            } catch (IOException e) {
                                Log.e("API_ERROR", "Erro ao ler corpo de erro", e);
                            }
                        }

                        // Mostrar mensagem detalhada
                        final String finalErrorMsg = errorMsg + (!errorDetails.isEmpty() ? "\nDetalhes: " + errorDetails : "");
                        String finalErrorMsg1 = errorMsg;
                        runOnUiThread(() -> {
                            Toast.makeText(EntradaActivity.this, finalErrorMsg, Toast.LENGTH_LONG).show();

                            // Adicionar tratamento visual nos campos com erro
                            if (finalErrorMsg1.contains("Email")) {
                                txtEmail.setError(finalErrorMsg1);
                                txtEmail.requestFocus();
                            } else if (finalErrorMsg1.contains("CPF") || finalErrorMsg1.contains("CNPJ")) {
                                txtCPF_CNPJ_Reg.setError(finalErrorMsg1);
                                txtCPF_CNPJ_Reg.requestFocus();
                            } else if (finalErrorMsg1.contains("Senha")) {
                                txtSenhaReg.setError(finalErrorMsg1);
                                txtSenhaReg.requestFocus();
                            }
                        });

                    } catch (Exception e) {
                        Log.e("API_ERROR", "Erro inesperado ao processar erro", e);
                        runOnUiThread(() ->
                                Toast.makeText(EntradaActivity.this, "Erro inesperado: " + e.getMessage(), Toast.LENGTH_LONG).show()
                        );
                    }
                }

            });
        }
    }

