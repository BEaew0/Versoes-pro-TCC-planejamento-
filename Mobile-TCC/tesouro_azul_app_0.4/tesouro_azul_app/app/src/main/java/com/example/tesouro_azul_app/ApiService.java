package com.example.tesouro_azul_app;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Criar Usuario
    @POST("api/Usuarios")
    Call<SuperClassUser.Usuario> criarUsuario(@Body SuperClassUser.CriarUsuarioDto usuarioDto);

    // Buscar todos os Usuarios
    @GET("api/Usuarios")
    Call<List<SuperClassUser.Usuario>> buscarUsuarios();

    // Buscar Usuario por ID
    @GET("api/Usuarios/{id}")
    Call<SuperClassUser.Usuario> buscarUsuarioPorId(@Path("id") int id);

    // Atualizar campo do Usuario
    @PATCH("api/Usuarios/{id}/alterar-campo")
    Call<SuperClassUser.Usuario> alterarCamposUsuario(@Path("id") int id, @Body SuperClassUser.AtualizarCampoUsuarioDto dto);

    // Atualizar Imagem
    @PATCH("api/Usuarios/{id}/imagem")
    Call<SuperClassUser.Usuario> atualizarImagem(@Path("id") int id, @Body SuperClassUser.ImagemDto dto);

    // Deletar Usuario
    @DELETE("api/Usuarios/{id}")
    Call<Void> deletarUsuario(@Path("id") int id);

    @POST("api/Usuarios/login")
    Call<SuperClassUser.LoginResponseDto> loginUsuario(@Body SuperClassUser.LoginRequestDto loginDto);

    //////////////////////////////////////////////////////////////////////////////////////////////////

    @GET("api/TestarConexao/StatusAPI")
    Call<Void> testarConexaoAPI();

    @GET("api/TestarConexao/StatusBanco")
    Call<ResponseBody> testarConexaoBanco();

    //////////////////////////////////////////////////////////////////////////////////////////////////
    @POST("api/Produtos")
    Call<Void> cadastrarProduto(@Body SuperClassProd.CadastrarProdutoDto produtoDto);

    // Busca Produtos por Campo
    @POST("api/Produtos/Buscar-por-campo")
    Call<List<SuperClassProd.Produto>> buscarProdutosPorCampo(
            @Query("id_usuario_fk") int idUsuario,
            @Body SuperClassProd.CamposProdutoDto filtro);

    // Lista Todos os Produtos
    @GET("api/Produtos")
    Call<List<SuperClassProd.Produto>> buscarTodosProdutos();

    // Busca Produtos por Usuário
    @GET("api/Produtos/usuario/{id_usuario}")
    Call<List<SuperClassProd.Produto>> buscarProdutosPorUsuario(@Path("id_usuario") int idUsuario);

    // Busca Produto por ID
    @GET("api/Produtos/produto/{id}")
    Call<SuperClassProd.Produto> buscarProdutoPorId(@Path("id") int id);

    // Atualiza o Campo do Produto
    @PATCH("api/Produtos/{id}")
    Call<SuperClassProd.Produto> alterarProduto(
            @Path("id") int id,
            @Body SuperClassProd.CamposProdutoDto campo);

    // Altera Imagem do Produto
    @PATCH("api/Produtos/Alterar-Imagem-por-{id}")
    Call<SuperClassProd.Produto> alterarImagemProduto(@Path("id") int id);

    // Deleta o Produto
    @DELETE("api/Produtos/{id}")
    Call<Void> deletarProduto(@Path("id") int id);

    //Realiza o pedido de compra
    @POST("api/PedidoCompra")
    Call<Void> criarPedidoCompra(@Body SuperClassProd.PedidoCompraCompletoDto pedidoDto);


}


