package com.example.tesouro_azul_app;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//Adicionar automaticamente o token JWT às requisições HTTP.
public class AuthInterceptor implements Interceptor {
    private SharedPreferences sharedPreferences;

    public AuthInterceptor(Context context)
    {
        sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request originalRequest = chain.request();

        // Obter o token JWT salvo
        String token = sharedPreferences.getString("jwt_token", null);

        if (token != null) {
            // Adicionar o token ao cabeçalho Authorization
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(originalRequest);
    }
}
