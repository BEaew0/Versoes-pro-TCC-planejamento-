package com.example.tesouro_azul_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesouro_azul_app.Class.ApiOperation;
import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.Util.AuthUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private String Host="https://tesouroazul1.hospedagemdesites.ws/api";

    private ProgressBar progressBar;
    private EditText txtNomeReg,txtSenhaReg,txtConfirmSenha,txtEmail,txtCPF_CNPJ_Reg,txtNascimento;

    private EditText txtCPF_CNPJ,txtSenha;

    Button btnEnter,btnRegister;

    CheckBox mostrarSenha,showSenhaReg,showSenhaConfir;

    private TextView txtRegistrar, txtLoading;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        ApiOperation apiOperation = new ApiOperation(
                LoginActivity.this,
                findViewById(R.id.progressBar),
                findViewById(R.id.txtLoading),//Apenas para testes
                findViewById(R.id.txtCPF_CNPJ),
                findViewById(R.id.txtSenha),
                findViewById(R.id.btnEnter),
                findViewById(R.id.txtRegistrar)
        );

            try {
                apiOperation.ConectarAPI();
            } catch (Exception e) {
                Log.e("LoginActivity", "Erro ao conectar API", e);
                Toast.makeText(this, "Erro ao inicializar aplicativo", Toast.LENGTH_SHORT).show();
            }

            // Verificar se usuário está logado
            if (!AuthUtils.isLoggedIn(this)) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }


        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtCPF_CNPJ.getText().toString().trim();
                String senha = txtSenha.getText().toString().trim();

                apiOperation.realizarLogin(email,senha);
            }
        });


        if (mostrarSenha.isChecked())
        {
            txtSenha.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else
        {
            txtSenha.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        txtRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, EntradaActivity.class);
                startActivity(intent);
            }
        });
    }
}