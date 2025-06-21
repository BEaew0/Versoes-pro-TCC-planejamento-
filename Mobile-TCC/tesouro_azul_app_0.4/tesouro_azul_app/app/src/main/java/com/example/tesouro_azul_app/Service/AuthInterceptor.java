package com.example.tesouro_azul_app.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tesouro_azul_app.EntradaActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

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
                handleUnauthorized();
                throw new IOException("Token de autenticação expirado");

            case 403: // Forbidden
                showToast("Acesso negado");
                break;

            case 500: // Server Error
                showToast("Erro interno no servidor");
                break;

            // Outros casos de erro se quiser...
        }

        return response;
    }

    private void handleUnauthorized() {
        // Limpa o token
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwt_token");
        editor.apply();

        // Toast + Redirecionamento para tela de login
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(context, "Sessão expirada. Faça login novamente.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, EntradaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        });
    }
}
