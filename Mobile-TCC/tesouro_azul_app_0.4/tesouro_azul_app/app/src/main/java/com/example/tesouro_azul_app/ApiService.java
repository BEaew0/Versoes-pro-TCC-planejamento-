package com.example.tesouro_azul_app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiService {
    @POST("https://tesouroazul1.hospedagemdesites.ws/api/Usuarios") // <-- coloque o endpoint da sua API aqui
        // Significa que a resposta da API não tem corpo (só código HTTP). Se sua API retorna algo (como um JSON com status ou mensagem)
        // você pode trocar Void por uma classe de resposta.
    Call<Void> enviarUsuario(@Body Usuario usuario);

    @GET("https://tesouroazul1.hospedagemdesites.ws/api/usuario/BuscarUsuarios")
    static Call<List<Usuario>> getUsuario()//Isso prepara a requisição, mas ainda não a executa!
    {
        return null;
    }
    @GET("https://tesouroazul1.hospedagemdesites.ws/api/conexao")
    static Call<Void> verificarConexao()//Isso prepara a requisição, mas ainda não a executa!
    {
        return null;
    }

    @GET("https://tesouroazul1.hospedagemdesites.ws/api/getImagem")
    static Call<Void> Imagem()//Isso prepara a requisição, mas ainda não a executa!
    {
        return null;
    }

    @POST("https://tesouroazul1.hospedagemdesites.ws/api/produto/CriarProduto")
    Call<Void> criarProduto(@Body ProdutoObject produtoObject);


}
