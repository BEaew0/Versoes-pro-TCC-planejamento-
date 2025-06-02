package com.example.tesouro_azul_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//Adiciona automaticamente o token JWT às requisições.
public class AuthInterceptor implements Interceptor {
    private SharedPreferences sharedPreferences;
    private static final String TAG = "AuthInterceptor";
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Obter o token JWT salvo
        String token = sharedPreferences.getString("jwt_token", null);

        if (token == null) {
            // Se não há token, prossegue sem autenticação
            return chain.proceed(originalRequest);
        }

        //Adciona o Token para a requisição
        Request authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        //Executa a requisição
        Response response = chain.proceed(authenticatedRequest);

        // Verificar se o token expirou (código 401 - Unauthorized)
        if (response.code() == 401) {
            response.close(); // Fechar a resposta atual

            Log.d(TAG, "Token expirado ou inválido, realizando logout...");

            // Limpa o token inválido
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("jwt_token");
            editor.apply();

            //Redirecionar para tela de login (se estiver em uma Activity)
            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Sessão expirada. Faça login novamente.", Toast.LENGTH_LONG).show();
                    Intent entradaIntent = new Intent(context, EntradaActivity.class);
                    entradaIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(entradaIntent);
                    ((android.app.Activity) context).finish();
                });
            }
            //Lançar exceção para interromper a requisição atual
            throw new IOException("Token de autenticação expirado");
        }
        return response;
    }

}

