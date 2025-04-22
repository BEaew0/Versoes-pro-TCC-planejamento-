package com.example.tesouro_azul_app;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.DatePickerDialog;
import android.content.Context;

import android.widget.DatePicker;
import android.widget.EditText;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    private String Host="...";
    private String url,ret;

    private EditText txtNomeReg,txtSenhaReg,txtConfirmSenha,txtEmail,txtCPF_CNPJ_Reg,txtNascimento;

    public static String nomeReg,senhaReg,conSenhaReg,emailReg,CPF_CNPJ_reg,nascReg;
    private EditText txtCPF_CNPJ,txtSenha;

    Button btnEnter,btnRegister;

    TextView txtRegistrar;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date dataAtual = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.entrada);

            txtCPF_CNPJ = (EditText) findViewById(R.id.txtCPF_CNPJ);
            txtSenha = (EditText) findViewById(R.id.txtSenha);
            btnEnter = (Button) findViewById(R.id.btnEnter);

            String CPF_CNPJ = txtCPF_CNPJ.getText().toString().trim();
            String senha = txtSenha.getText().toString().trim();

            txtRegistrar = (TextView) findViewById(R.id.txtRegistrar);

            btnEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Após periodo de testes ajeitar essa parte
                    Intent intent = new Intent(EntradaActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            });
        }
        catch (Exception e) {}

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

            txtNascimento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerUtil.showDatePicker(EntradaActivity.this, txtNascimento, true);
                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //isso é pra converter data de nascimento,
                    // como somos obrigados a usar uma versão velha do java ela é feia assim mesmo
                   if (validarCadastro(view,getApplicationContext()))
                   {

                      sendToJSON();

                       Toast.makeText(EntradaActivity.this,"Validação bem sucedida",Toast.LENGTH_SHORT).show();

                       //processo de validação concluido
                       Intent intent = new Intent(EntradaActivity.this, MainActivity.class);
                       startActivity(intent);
                   }

                }

                public void sendToJSON()
                {

                    String CPF_CNPJreg = txtCPF_CNPJ_Reg.getText().toString().trim();

                    ValidarClass validator = new ValidarClass();

                    if (validarCadastro(v,EntradaActivity.this))
                    {
                        // Identifica o tipo de documento
                        String tipoDocumento = validator.identificarTipo(CPF_CNPJreg);

                        String CPF_USUARIO = null;
                        String CNPJ_USUARIO = null;

                        if ("CPF".equals(tipoDocumento))
                        {
                            CPF_USUARIO = txtCPF_CNPJ_Reg.getText().toString().trim();
                        }
                        else if ("CNPJ".equals(tipoDocumento))
                        {
                            CNPJ_USUARIO = txtCPF_CNPJ_Reg.getText().toString().trim();
                        }
                        else
                        {
                            Toast.makeText(EntradaActivity.this, "Documento inválido!", Toast.LENGTH_SHORT).show();
                            return; // Interrompe a execução se o documento for inválido

                        }

                        String EMAIL_USUARIO = txtEmail.getText().toString().trim();
                        String SENHA_USUARIO = txtSenhaReg.getText().toString().trim();

                        String NOME_USUARIO = txtNomeReg.getText().toString().trim();

                        String birthUser = txtNascimento.getText().toString().trim();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date DATA_NASC_USUARIO = null;

                        try
                        {
                            DATA_NASC_USUARIO = sdf.parse(birthUser);

                        } catch (ParseException e)
                        {
                            e.printStackTrace();
                        }

                        Usuario usuario = new Usuario(NOME_USUARIO,EMAIL_USUARIO,DATA_NASC_USUARIO,CPF_USUARIO,CNPJ_USUARIO,SENHA_USUARIO);

                        //Conversão para JSON
                        Gson gson = new Gson();
                        String json = gson.toJson(usuario);

                        Toast.makeText(EntradaActivity.this,json,Toast.LENGTH_SHORT).show();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://suaapi.com.br/") // coloque a URL base da sua API
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();

                        // Crie uma instância da interface da API
                        ApiService apiService = retrofit.create(ApiService.class);


                        Call<Void> call = apiService.enviarUsuario(usuario);

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(EntradaActivity.this, "Usuário enviado com sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EntradaActivity.this, "Erro ao enviar: " + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(EntradaActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


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
                            nomeReg.isEmpty() || CPF_CNPJreg.isEmpty() || nascimento.isEmpty())
                    {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    // Instância do validador
                    ValidarClass validator = new ValidarClass();

                    // Identifica o tipo de documento
                    String tipoDocumento = validator.identificarTipo(CPF_CNPJreg);

                    try {
                        // Validação de maioridade
                        if (!validator.MaiorIdade(true)) {
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
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date dataNascimento = dateFormat.parse(nascimento);

                        } catch (ParseException e) {
                            Toast.makeText(context, "Formato de data inválido", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    } catch (Exception e)
                    {
                        //Se algo deu errado
                        Toast.makeText(context, "Erro ao processar dados", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return false;
                    }

                    return true; // Se passou por todas as validações
                }


                class ValidarClass
                {

                    //retira todos os caractéres especiais como pontos e traços e define se é CPF
                    public boolean isCPF(String CPF_CNJPreg)
                    {
                        return CPF_CNJPreg != null && CPF_CNJPreg.replaceAll("\\D", "").length() == 11;
                    }

                    //retira todos os caractéres especiais como pontos e traços e define se é CNPJ
                    public boolean isCNPJ(String CFP_CNPJreg)
                    {
                        return  CFP_CNPJreg != null && CFP_CNPJreg.replaceAll("\\D", "").length() == 14;
                    }

                    //responsavel por informar o tipo, alterando o valor da string tipo para outras operações
                    public String identificarTipo(String CPF_CNJPreg)
                    {
                        if (isCPF(CPF_CNJPreg)) return "CPF";
                        if (isCNPJ(CPF_CNJPreg)) return "CNPJ";
                        return "Invalido";
                    }

                    //lógica para verificar se o cpf é valido
                    public boolean validarCPF(String CPF_CNJPreg )
                    {
                        CPF_CNJPreg = CPF_CNJPreg.replaceAll("\\D", "");

                        //detecta todos os numeros repitidos
                        if (CPF_CNJPreg.length() != 11 || CPF_CNJPreg.matches("(\\d)\\1{10}")) return false;

                        int soma = 0, peso = 10;
                        for (int i = 0; i < 9; i++) soma += (CPF_CNJPreg.charAt(i) - '0') * peso--;
                        int digito1 = 11 - (soma % 11);
                        if (digito1 >= 10) digito1 = 0;

                        soma = 0; peso = 11;
                        for (int i = 0; i < 10; i++) soma += (CPF_CNJPreg.charAt(i) - '0') * peso--;
                        int digito2 = 11 - (soma % 11);
                        if (digito2 >= 10) digito2 = 0;

                        return CPF_CNJPreg.endsWith(digito1 + "" + digito2);
                    }

                    //lógica para verificar se a cnpj é valida
                    public boolean validarCNPJ(String CFP_CNPJreg)
                    {
                        CFP_CNPJreg = CFP_CNPJreg.replaceAll("\\D", "");
                        if (CFP_CNPJreg.length() != 14 || CFP_CNPJreg.matches("(\\d)\\1{13}")) return false;

                        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
                        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

                        int soma = 0;
                        for (int i = 0; i < 12; i++) soma += (CFP_CNPJreg.charAt(i) - '0') * pesos1[i];
                        int digito1 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

                        soma = 0;
                        for (int i = 0; i < 13; i++) soma += (CFP_CNPJreg.charAt(i) - '0') * pesos2[i];
                        int digito2 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

                        return CFP_CNPJreg.endsWith(digito1 + "" + digito2);
                    }



                    public boolean MaiorIdade( Boolean mais18) throws ParseException
                    {
                        String nascimento = txtNascimento.getText().toString().trim();

                        Date birth = sdf.parse(nascimento);

                        //para pegar a data atual
                        Calendar calendar = Calendar.getInstance();

                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(birth);

                        int Idade = calendar.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);

                        return Idade < 18;
                    }
                }

            });

        });

    }

}