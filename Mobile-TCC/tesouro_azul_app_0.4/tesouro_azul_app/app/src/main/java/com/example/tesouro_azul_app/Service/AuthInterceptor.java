package com.example.tesouro_azul_app.Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tesouro_azul_app.EntradaActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//Adiciona automaticamente o token JWT às requisições.
public class AuthInterceptor implements Interceptor {
    private SharedPreferences sharedPreferences;
    private static final String TAG = "AuthInterceptor";
    private final Context context;

    public AuthInterceptor(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    // Versão melhorada com tratamento de mais códigos de erro
    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = sharedPreferences.getString("jwt_token", null);

        if (token == null) {
            return chain.proceed(originalRequest);
        }

        Request authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        Response response = chain.proceed(authenticatedRequest);

        switch (response.code()) {
            case 401: // Unauthorized
                handleUnauthorized(context);
                throw new IOException("Token de autenticação expirado");

            case 403: // Forbidden
                // Tratar acesso negado
                Toast.makeText(context, "Acesso negado", Toast.LENGTH_LONG).show();
                break;
            case 500: // Server Error
                // Tratar erro do servidor
                Toast.makeText(context, "Merda do miguel.", Toast.LENGTH_LONG).show();
                break;
            // outros casos...
        }
        return response;
    }

    private void handleUnauthorized(Context context) {
        // Extrair para método separado para reutilização
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwt_token");
        editor.apply();

        if (context instanceof Activity) {

            ((Activity) context).runOnUiThread(() -> {
                Toast.makeText(context, "Sessão expirada. Faça login novamente.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, EntradaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                ((Activity) context).finish();
            });
        }
    }


}

