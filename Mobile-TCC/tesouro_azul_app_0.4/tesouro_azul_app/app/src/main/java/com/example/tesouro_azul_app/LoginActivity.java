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
import com.example.tesouro_azul_app.Service.RetrofitClient;
import com.example.tesouro_azul_app.Util.AuthUtils;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private EditText txtEmail,txtSenha;

    Button btnEnter;

    CheckBox mostrarSenha;

    private TextView txtRegistrar, txtLoading;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica se o usuário já está logado (redireciona se verdadeiro)
        if (AuthUtils.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish(); // Finaliza a LoginActivity para não voltar com back button
            return;   // Sai do método para não continuar a criação da tela de login
        }

        // Carrega o layout da tela de login
        setContentView(R.layout.activity_login);

        //Configuração do Retrofit para chamadas à API
        apiService = RetrofitClient.getApiService(getApplicationContext());

        // Inicialização dos componentes da interface
        mostrarSenha = (CheckBox) findViewById(R.id.mostrarSenhas);
        txtEmail = (EditText) findViewById(R.id.txtEmailLogin);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        btnEnter = (Button) findViewById(R.id.btnEnter);
        txtRegistrar = (TextView) findViewById(R.id.txtRegistrar);
        progressBar = findViewById(R.id.progressBar);
        txtLoading = findViewById(R.id.progress_text);

        // Obtém valores iniciais dos campos
        String email = txtEmail.getText().toString().trim();
        String senha = txtSenha.getText().toString().trim();

        // Configura a ApiOperation (classe que centraliza operações com a API)
        ApiOperation apiOperation = new ApiOperation(
                LoginActivity.this,
                progressBar,
                txtLoading,
                txtEmail,
                txtSenha,
                btnEnter,
                txtRegistrar
        );

        // Define máscara de senha padrão (caracteres ocultos)
        txtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Listener para o botão de login
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chama método de login da ApiOperation
                apiOperation.realizarLogin(email, senha);
            }
        });

        /*
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navega para a tela principal
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
*/
        // Listener para o checkbox "mostrar senha"
        mostrarSenha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // Alterna entre mostrar e ocultar a senha
                if (isChecked) {
                    txtSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    txtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                // Posiciona o cursor no final do texto
                txtSenha.setSelection(txtSenha.getText().length());
            }
        });

        // Listener para o texto de registro
        txtRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega para a tela de registro
                startActivity(new Intent(LoginActivity.this, EntradaActivity.class));
            }
        });

        // Tenta conectar com a API (verifica disponibilidade)
        try {
            apiOperation.ConectarAPI();

        } catch (Exception e) {
            Log.e("LoginActivity", "Erro ao conectar API", e);
            Toast.makeText(this, "Erro ao inicializar aplicativo", Toast.LENGTH_SHORT).show();
        }
    }
}