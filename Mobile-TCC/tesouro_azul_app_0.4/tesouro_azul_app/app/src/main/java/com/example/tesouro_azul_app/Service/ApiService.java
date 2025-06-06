package com.example.tesouro_azul_app.Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tesouro_azul_app.Class.SuperClassProd;
import com.example.tesouro_azul_app.Class.SuperClassUser;
import com.example.tesouro_azul_app.Pages.EntradaActivity;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
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

    //O "ResponseBody" é a parte principal da resposta de uma solicitação HTTP.
    //Ele contém os dados que o servidor está retornando ao cliente
    @GET("api/TestarConexao/StatusAPI")
    Call<ResponseBody> testarConexaoAPI();

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


        // Pedidos de Compra Endpoints
        @POST("api/PedidoCompra/criar-pedido-compra")
        Call<SuperClassProd.PedidoCompraResponse> criarPedidoCompra(@Body SuperClassProd.PedidoCompraCompletoDto dto);

        @POST("api/PedidoCompra/inserir-itens-em-pedido")
        Call<List<SuperClassProd.ItemCompra>> inserirItemCompra(@Body List<SuperClassProd.ItemCompraDto> dto);

        @POST("api/PedidoCompra/pedido-compra/Buscar-por-campo")
        Call<List<SuperClassProd.PedidoCompra>> pedidoBuscarPorCampo(@Body SuperClassProd.CamposDto filtro);

        @POST("api/PedidoCompra/item-compra/Buscar-por-campo")
        Call<List<SuperClassProd.ItemCompra>> itemBuscarPorCampo(
                @Query("id_pedido") int idPedido,
                @Body SuperClassProd.CamposDto filtro);

        @GET("api/PedidoCompra/buscar-todos-pedidos")
        Call<List<SuperClassProd.PedidoCompra>> buscarComprasPedido();

        @GET("api/PedidoCompra/Itens/{id_pedido}")
        Call<List<SuperClassProd.ItemCompra>> buscarItensCompraPorPedido(@Path("id_pedido") int idPedido);

        @GET("api/PedidoCompra/buscar-pedidos-usuario")
        Call<List<SuperClassProd.PedidoCompra>> buscarComprasPedidoPorUsuario();

        @PATCH("api/PedidoCompra/alterar-pedido-por-campo/{id_pedido}")
        Call<SuperClassProd.PedidoCompra> alterarPedidoCompra(
                @Path("id_pedido") int idPedido,
                @Body SuperClassProd.CamposDto dto);

        @PATCH("api/PedidoCompra/alterar-item-do-pedido/{id_item}")
        Call<SuperClassProd.ItemCompra> alterarItemCompra(
                @Path("id_item") int idItem,
                @Body SuperClassProd.CamposDto dto);

        @DELETE("api/PedidoCompra/{id_pedido}")
        Call<Void> deletarPedidoCompra(@Path("id_pedido") int idPedido);

        @DELETE("api/PedidoCompra/Itens/{id_item}")
        Call<Void> deletarItemCompra(@Path("id_item") int idItem);



}


