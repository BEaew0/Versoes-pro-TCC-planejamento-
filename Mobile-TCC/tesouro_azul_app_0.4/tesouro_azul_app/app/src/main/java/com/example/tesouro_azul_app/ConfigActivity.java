package com.example.tesouro_azul_app;


import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ConfigActivity extends AppCompatActivity {

    private Switch swicthTheme;
    private SharedPreferences sharedPreferences;


    ImageView Xleave,themeIcon;
    RelativeLayout trocarSenha,SairConta,ExcluirConta;

    // Nome do arquivo de preferências e chave booleana usada para salvar o modo escuro
    private static final String PREF_NAME = "ThemePrefs";
    private static final String NIGHT_MODE_KEY = "night_mode";

    //Declara uma constante para identificar o código da requisição da galeria,
    // o ícone do usuário e o launcher para abrir a galeria e receber o resultado.
    private static final int REQUEST_CODE_GALLERY = 1001;
    private ShapeableImageView userIcon;
    private ActivityResultLauncher<Intent> galleryLauncher;

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


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Referência ao Switch no layout (XML)
        swicthTheme = findViewById(R.id.swicthTheme);
        userIcon = findViewById(R.id.User_icon);
        themeIcon = findViewById(R.id.ThemeMode);

        Xleave = (ImageView) findViewById(R.id.Xleave);
        trocarSenha = (RelativeLayout) findViewById(R.id.trocarSenha);
        SairConta = (RelativeLayout) findViewById(R.id.SairConta);
        ExcluirConta = (RelativeLayout) findViewById(R.id.ExcluirConta);

        // Define o estado inicial do Switch de acordo com a preferência
        swicthTheme.setChecked(isNightMode);
        updateThemeIcon(isNightMode);

        //Prepara o launcher para abrir a galeria e, se o usuário selecionar uma imagem, define essa imagem como o novo ícone do usuário.
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        userIcon.setImageURI(imageUri);
                    }
                });


        Xleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //Ao clicar no ícone do usuário, verifica se tem permissão para acessar a galeria.
        // Se não tiver, solicita. Se tiver, abre a galeria para o usuário escolher uma imagem.
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ConfigActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            ConfigActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_GALLERY
                    );
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                galleryLauncher.launch(intent);
            }
        });


        // Listener para detectar mudanças no Switch
        swicthTheme.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
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

        SairConta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Processo que remove o login do aplicativo

            }
        });

        ExcluirConta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

    }

    // Função auxiliar para salvar a preferência no SharedPreferences
    private void saveThemePreference(boolean isNight)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NIGHT_MODE_KEY, isNight); // Salva o valor true ou false
        editor.apply(); // Aplica as mudanças
    }

    private void updateThemeIcon(boolean isNightMode)
    {
        if(isNightMode){
            themeIcon.setImageResource(R.drawable.nigth_mode_icon);
            themeIcon.setBackgroundResource(R.drawable.round_back_night);
        }
        else {
            themeIcon.setImageResource(R.drawable.ligth_mode_icon);
            themeIcon.setBackgroundResource(R.drawable.round_back_white);
        }

    }


}
