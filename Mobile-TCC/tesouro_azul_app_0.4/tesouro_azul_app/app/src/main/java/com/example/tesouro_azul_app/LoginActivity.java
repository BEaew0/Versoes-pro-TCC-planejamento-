package com.example.tesouro_azul_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesouro_azul_app.Pages.MainActivity;
import com.example.tesouro_azul_app.Service.ApiOperation;
import com.example.tesouro_azul_app.Service.ApiService;
import com.example.tesouro_azul_app.Util.AuthUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private EditText txtCPF_CNPJ,txtSenha;

    Button btnEnter;

    CheckBox mostrarSenha;

    private TextView txtRegistrar, txtLoading;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AuthUtils.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://vps59025.publiccloud.com.br:5232/")// <- Coloque a URL base da sua API aqui
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

        txtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
/*
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtCPF_CNPJ.getText().toString().trim();
                String senha = txtSenha.getText().toString().trim();

                apiOperation.realizarLogin(email,senha);
            }
        });
        */


        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mostrarSenha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mostrarSenha.isChecked()) {
                    // Mostrar senha
                    txtSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // Ocultar senha
                    txtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                // Colocar o cursor no final do texto após a mudança
                txtSenha.setSelection(txtSenha.getText().length());
            }
        });

        txtRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, EntradaActivity.class);
                startActivity(intent);
            }
        });

        try {
            apiOperation.ConectarAPI();
        } catch (Exception e) {
            Log.e("LoginActivity", "Erro ao conectar API", e);
            Toast.makeText(this, "Erro ao inicializar aplicativo", Toast.LENGTH_SHORT).show();
        }

    }
}