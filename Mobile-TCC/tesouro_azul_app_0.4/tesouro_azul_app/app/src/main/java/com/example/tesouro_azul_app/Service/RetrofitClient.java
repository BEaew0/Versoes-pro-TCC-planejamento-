package com.example.tesouro_azul_app.Service;

// ApiClient.java
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;


/**
 * Classe utilitária para configuração e gerenciamento das conexões HTTP com a API
 * utilizando a biblioteca Retrofit e OkHttp.
 * Implementa padrão Singleton para reutilização da instância Retrofit.
 */
public class RetrofitClient {

    // URL base da API - deve terminar com barra (/)
    private static final String BASE_URL = "https://srv869019.hstgr.cloud/";

    // Instância única do Retrofit (padrão Singleton)
    private static Retrofit retrofit = null;

    /**
     * Obtém a instância do serviço API configurado.
     * Cria uma nova instância do Retrofit se necessário.
     *
     * @param context Contexto da aplicação (para interceptors que precisam de contexto)
     * @return Instância do serviço API configurado
     */
    public static ApiService getApiService(Context context) {
        // Verifica se já existe uma instância do Retrofit
        if (retrofit == null) {
            // Configura uma nova instância do Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)  // Define a URL base
                    .client(createSecureOkHttpClient(context))  // Configura cliente HTTP seguro
                    .addConverterFactory(GsonConverterFactory.create())  // Conversor JSON
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    /**
     * Cria e configura um cliente HTTP seguro com interceptors e timeout.
     *
     * @param context Contexto da aplicação
     * @return OkHttpClient configurado
     */
    private static OkHttpClient createSecureOkHttpClient(Context context) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        try {
            // Configuração SSL/TLS para conexões seguras
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getTrustManagers(), new java.security.SecureRandom());

            // Aplica as configurações SSL ao cliente
            clientBuilder.sslSocketFactory(
                    sslContext.getSocketFactory(),
                    (X509TrustManager) getTrustManagers()[0]
            );
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        // Configuração dos interceptors (a ordem de adição é importante)
        clientBuilder
                // Interceptor para CORS (Cross-Origin Resource Sharing)
                .addInterceptor(new CorsInterceptor())

                // Interceptor para autenticação (adiciona token JWT)
                .addInterceptor(new AuthInterceptor(context))

                // Interceptor para logging (exibe logs das requisições/respostas)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))

                // Timeouts para evitar requisições travadas
                .connectTimeout(30, TimeUnit.SECONDS)  // Timeout de conexão
                .readTimeout(30, TimeUnit.SECONDS)     // Timeout de leitura
                .writeTimeout(30, TimeUnit.SECONDS);   // Timeout de escrita

        return clientBuilder.build();
    }

    /**
     * Implementação de TrustManager que aceita todos os certificados SSL.
     * ATENÇÃO: Esta abordagem é insegura para produção - deve ser usada apenas
     * em desenvolvimento ou com certificados autoassinados em ambientes controlados.
     *
     * @return Array de TrustManagers configurados
     */
    private static TrustManager[] getTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        // Não valida certificados do cliente
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        // Não valida certificados do servidor
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    /**
     * Reseta a instância do Retrofit.
     * Útil para forçar a criação de uma nova instância, por exemplo:
     * - Após logout (para limpar credenciais)
     * - Ao mudar configurações de conexão
     * - Em casos de necessidade de reconexão
     */
    public static void resetClient() {
        retrofit = null;
    }
}


