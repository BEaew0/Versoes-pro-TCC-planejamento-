package com.example.tesouro_azul_app;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tesouro_azul_app.Pages.MainActivity;
import com.example.tesouro_azul_app.Util.ValidatorUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.Class.SuperClassUser;
import com.example.tesouro_azul_app.Util.DatePickerUtil;
import com.example.tesouro_azul_app.Service.RetrofitClient;
import com.google.gson.Gson;

import android.content.Context;

import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private EditText txtCPF_CNPJ,txtSenha;

    Button btnEnter,btnRegister;

    CheckBox mostrarSenha,showSenhaReg,showSenhaConfir;

    private TextView txtRegistrar, txtLoading;

    private ApiService apiService;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date dataAtual = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.entrada);

            // Configura Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://tesouroazul1.hospedagemdesites.ws/api/")// <- Coloque a URL base da sua API aqui
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);

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
                    String CPF_CNPJreg = txtCPF_CNPJ_Reg.getText().toString().trim();
                    ValidatorUtils validator = new ValidatorUtils(); // Se usar métodos estáticos, nem precisa instanciar


                    // Validação dos campos (usando o método existente)
                    if (!validarCadastro(btnRegister, EntradaActivity.this)) {
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
        }
    }

