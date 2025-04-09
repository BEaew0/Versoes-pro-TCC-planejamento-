package com.example.tesouro_azul_app;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EntradaActivity extends AppCompatActivity
{

    //quando tiver eu botoKKKKK

    private String url,ret;

    EditText txtCPF_CNPJ,txtSenha,txtNomeReg,txtSenhaReg,txtConfirmSenha,txtEmail,txtCPF_CNPJ_Reg,txtNascimento;

    Button btnEnter,btnRegister;

    TextView txtRegistrar;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date dataAtual = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrada);


        txtCPF_CNPJ = (EditText) findViewById(R.id.txtCPF_CNPJ);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        btnEnter = (Button) findViewById(R.id.btnEnter);

        String CPF_CNPJ = txtCPF_CNPJ.getText().toString().trim();
        String senha = txtSenha.getText().toString().trim();

        txtRegistrar =(TextView) findViewById(R.id.txtRegistrar);

        btnEnter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //Após periodo de testes ajeitar essa parte
                Intent intent = new Intent(EntradaActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        txtRegistrar.setOnClickListener(v ->
        {
            setContentView(R.layout.registrar);//Muda para a tela de registro

            txtCPF_CNPJ_Reg = (EditText) findViewById(R.id.txtCPF_CNPJ_Reg);
            txtNomeReg = (EditText) findViewById(R.id.txtNomeProd);
            txtSenhaReg = (EditText) findViewById(R.id.txtSenhaReg);
            txtConfirmSenha = (EditText) findViewById(R.id.txtConfirmSenha);
            txtEmail = (EditText) findViewById(R.id.txtEmail);
            txtNascimento = (EditText) findViewById(R.id.txtValidade);
            btnRegister = (Button) findViewById(R.id.btnRegister);


            btnRegister.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    String email = txtEmail.getText().toString().trim();
                    String senhaReg = txtSenhaReg.getText().toString().trim();
                    String senhaConfim = txtConfirmSenha.getText().toString().trim();
                    String nomeReg = txtNomeReg.getText().toString().trim();
                    String CPF_CNPJreg = txtCPF_CNPJ_Reg.getText().toString().trim();
                    String nascimento = txtNascimento.getText().toString().trim();

                    //isso é pra converter data de nascimento,
                    // como somos obrigados a usar uma versão velha do java ela é feia assim mesmo
                    try
                    {
                        // Formatação de data
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date dataNascimento = null;

                        try
                        {
                            dataNascimento = dateFormat.parse(nascimento);
                        } catch (ParseException e) {
                            throw new RuntimeException("Formato de data inválido", e);
                        }

                        Calendar calendar = Calendar.getInstance();
                        // Configurar a data no calendar se necessário
                        // calendar.setTime(dataNascimento);

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    // Instância do validador
                    ValidarClass validator = new ValidarClass();

                    // Verifica se todos os campos foram preenchidos
                    if (email.isEmpty() || senhaReg.isEmpty() || senhaConfim.isEmpty() ||
                            nomeReg.isEmpty() || CPF_CNPJreg.isEmpty() || nascimento.isEmpty())
                    {
                        Toast.makeText(view.getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                        setContentView(R.layout.activity_main);
                        return;
                    }

                    // Identifica o tipo de documento (CPF ou CNPJ)
                    String tipoDocumento = validator.identificarTipo(CPF_CNPJreg);

                    try {
                        // Validação de maioridade
                        if (!validator.MaiorIdade(true)) {
                            Toast.makeText(view.getContext(), "Menores de idade não são permitidos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Validação de senha
                        if (!senhaReg.equals(senhaConfim)) {
                            Toast.makeText(view.getContext(), "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Validação de documento
                        switch (tipoDocumento)
                        {
                            case "CPF":
                                if (!validator.validarCPF(CPF_CNPJreg))
                                {
                                    Toast.makeText(view.getContext(), "CPF inválido", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                break;

                            case "CNPJ":
                                if (!validator.validarCNPJ(CPF_CNPJreg))
                                {
                                    Toast.makeText(view.getContext(), "CNPJ inválido", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                break;

                            default:
                                Toast.makeText(view.getContext(), "Documento inválido", Toast.LENGTH_SHORT).show();
                                return;
                        }

                        // Se todas as validações passarem, continua com o processo de registro
                        // ...

                    } catch (ParseException e)
                    {
                        throw new RuntimeException("Erro ao processar dados", e);
                    }

                }

                private void inserir()
                {
                    //Quando tiver um processo eu passo

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