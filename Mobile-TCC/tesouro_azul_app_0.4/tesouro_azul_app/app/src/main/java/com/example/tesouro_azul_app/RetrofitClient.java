package com.example.tesouro_azul_app;

// ApiClient.java
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String BASE_URL = "https://tesouroazul1.hospedagemdesites.ws/api"; // Substitua pela sua URL
    private static Retrofit retrofit = null;

    // Método para obter o serviço API com suporte a JWT
    public static ApiService getApiService(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

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
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    // Método para resetar o cliente (útil após logout)
    public static void resetClient() {
        retrofit = null;
    }
}

