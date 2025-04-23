package com.example.tesouro_azul_app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiService {
    @POST("/api/usuario") // <-- coloque o endpoint da sua API aqui
    @GET("/api/usuario") // <-- coloque o endpoint da sua API aqui
    // Significa que a resposta da API não tem corpo (só código HTTP). Se sua API retorna algo (como um JSON com status ou mensagem)
        // você pode trocar Void por uma classe de resposta.
    Call<Void> enviarUsuario(@Body Usuario usuario);
   // Call<List<Usuario>> getUsuario();//Isso prepara a requisição, mas ainda não a executa!


}
