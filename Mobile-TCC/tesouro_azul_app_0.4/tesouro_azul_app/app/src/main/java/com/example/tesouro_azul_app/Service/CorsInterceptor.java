package com.example.tesouro_azul_app.Service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import okhttp3.Interceptor;
import okhttp3.Response;
import java.io.IOException;

public class CorsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Adiciona headers CORS à requisição
        Request.Builder builder = request.newBuilder()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

        // Tratamento especial para requisições OPTIONS (pré-flight)
        if (request.method().equals("OPTIONS")) {
            return chain.proceed(builder.method("OPTIONS", null).build())
                    .newBuilder()
                    .header("Access-Control-Max-Age", "600") // Cache por 10 minutos
                    .build();
        }

        // Prossegue com a requisição normal
        return chain.proceed(builder.build());
    }
}