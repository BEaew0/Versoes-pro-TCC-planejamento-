package com.example.tesouro_azul_app;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    // URL base da sua API — deve terminar com /
    private static final String BASE_URL = "https://suaapi.com.br/";

    private static Retrofit retrofit;

    // Retorna uma instância Retrofit pronta para uso
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Converte JSON automaticamente
                    .build();
        }
        return retrofit;
    }
}
