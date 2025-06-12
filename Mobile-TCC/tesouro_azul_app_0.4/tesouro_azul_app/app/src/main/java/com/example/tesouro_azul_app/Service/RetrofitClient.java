package com.example.tesouro_azul_app.Service;

// ApiClient.java
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String BASE_URL = "http://vps59025.publiccloud.com.br:5232/"; // Substitua pela sua URL
    private static Retrofit retrofit = null;

    // Método para obter o serviço API com suporte a JWT(Json Web Token)
    public static ApiService getApiService(Context context)
    {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();// Ferramenta para registrar detalhes das requisições e respostas HTTP no log.
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);// Faz log do corpo da requisição e resposta

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new AuthInterceptor(context)) // Adiciona o interceptor de autenticação
                    .connectTimeout(30, TimeUnit.SECONDS) // Timeout de conexão
                    .readTimeout(30, TimeUnit.SECONDS)    // Timeout de leitura
                    .writeTimeout(30, TimeUnit.SECONDS)   // Timeout de escrita
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())// Informa ao Retrofit para converter JSON usando a biblioteca Gson.
                    .build();
        }
        return retrofit.create(ApiService.class);//Cria uma instância da interface ApiService, que contém os endpoints da API.
    }
    //Zera a instância do Retrofit.
    //Útil, por exemplo, após logout, quando um novo token precisará ser usado
    public static void resetClient()
    {
        retrofit = null;
    }
}

