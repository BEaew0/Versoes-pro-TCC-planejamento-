package com.example.tesouro_azul_app;

// Importações necessárias
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ConfigActivity extends AppCompatActivity {

    // Declaração do Switch e SharedPreferences
    private Switch swicthTheme;
    private SharedPreferences sharedPreferences;

    ImageView Xleave,themeIcon,userIcon;
    RelativeLayout trocarSenha,SairConta,ExcluirConta;

    // Nome do arquivo de preferências e chave booleana usada para salvar o modo escuro
    private static final String PREF_NAME = "ThemePrefs";
    private static final String NIGHT_MODE_KEY = "night_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        // Antes de carregar o layout, verificamos o tema salvo
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Recupera se o modo escuro estava ativado na última vez
        boolean isNightMode = sharedPreferences.getBoolean(NIGHT_MODE_KEY, false);

        // Define o tema do app conforme a preferência salva
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Agora que o tema está definido, carregamos a tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        userIcon = findViewById(R.id.User_icon);
        themeIcon = findViewById(R.id.ThemeMode);
        Xleave = (ImageView) findViewById(R.id.Xleave);

        trocarSenha = (RelativeLayout) findViewById(R.id.trocarSenha);
        SairConta = (RelativeLayout) findViewById(R.id.SairConta);
        ExcluirConta = (RelativeLayout) findViewById(R.id.ExcluirConta);


        // Referência ao Switch no layout (XML)
        swicthTheme = findViewById(R.id.swicthTheme);

        // Define o estado inicial do Switch de acordo com a preferência
        swicthTheme.setChecked(isNightMode);
        updateThemeIcon(isNightMode);



        Xleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Listener para detectar mudanças no Switch
        swicthTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Se o usuário ativar, muda para o modo escuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveThemePreference(true);
                updateThemeIcon(true);// Salva essa escolha


            } else {
                // Se o usuário desativar, volta para o modo claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveThemePreference(false);
                updateThemeIcon(false);// Salva essa escolha
            }

            // Atualiza a tela com o novo tema
            recreate();
        });

        trocarSenha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {




            }
        });

        SairConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Processo que remove o login do aplicativo
            }
        });

        ExcluirConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    // Função auxiliar para salvar a preferência no SharedPreferences
    private void saveThemePreference(boolean isNight) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NIGHT_MODE_KEY, isNight); // Salva o valor true ou false
        editor.apply(); // Aplica as mudanças
    }

    private void updateThemeIcon(boolean isNightMode)
    {
        if(isNightMode){
            themeIcon.setImageResource(R.drawable.nigth_mode_icon);
            themeIcon.setBackgroundResource(R.drawable.round_back_night);
            userIcon.setImageResource(R.drawable.white_icon_img);


        }
        else {
            themeIcon.setImageResource(R.drawable.ligth_mode_icon);
            themeIcon.setBackgroundResource(R.drawable.round_back_white);
            userIcon.setImageResource(R.drawable.baseline_account_circle_24);
        }

    }
}
