package com.example.tesouro_azul_app.Service;

// ApiClient.java
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String BASE_URL = "http://vps59025.publiccloud.com.br:5232/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService(Context context) {
        if (retrofit == null) {
            // Configuração do OkHttpClient
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            // 1. PRIMEIRO: Adiciona o CorsInterceptor (deve vir antes dos outros)
            clientBuilder.addInterceptor(new CorsInterceptor());

            // 2. Adiciona os demais interceptors
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            clientBuilder
                    .addInterceptor(logging)
                    .addInterceptor(new AuthInterceptor(context))
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);

            // Cria instância Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    //Zera a instância do Retrofit.
    //Útil, por exemplo, após logout, quando um novo token precisará ser usado
    public static void resetClient()
    {
        retrofit = null;
    }
}

