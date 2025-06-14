package com.example.tesouro_azul_app.Service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CorsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Adiciona headers necessários
        Request.Builder builder = request.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");

        Response response = chain.proceed(builder.build());

        return response;
    }

}
