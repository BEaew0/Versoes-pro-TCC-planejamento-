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
import retrofit2.http.Header;
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
    Call<List<SuperClassUser.Usuario>> buscarUsuarios(@Header("Authorization") String token);

    // Buscar Usuario por ID
    @GET("api/Usuarios/{id}")
    Call<SuperClassUser.Usuario> buscarUsuarioPorId(@Header("Authorization") String token, @Path("id") int id);

    // Atualizar campo do Usuario
    @PATCH("api/Usuarios/{id}/alterar-campo")
    Call<SuperClassUser.Usuario> alterarCamposUsuario(@Header("Authorization") String token, @Path("id") int id, @Body SuperClassUser.AtualizarCampoUsuarioDto dto);

    // Atualizar Imagem
    @PATCH("api/Usuarios/{id}/imagem")
    Call<SuperClassUser.Usuario> atualizarImagem(@Header("Authorization") String token, @Path("id") int id, @Body SuperClassUser.ImagemDto dto);

    // Deletar Usuario
    @DELETE("api/Usuarios/{id}")
    Call<Void> deletarUsuario(@Header("Authorization") String token, @Path("id") int id);

    @POST("api/Usuarios/login")
    Call<SuperClassUser.LoginResponseDto> loginUsuario(@Body SuperClassUser.LoginRequestDto loginDto);

    //////////////////////////////////////////////////////////////////////////////////////////////////

    @GET("api/TestarConexao/StatusAPI")
    Call<ResponseBody> testarConexaoAPI();

    @GET("api/TestarConexao/StatusBanco")
    Call<ResponseBody> testarConexaoBanco();

    //////////////////////////////////////////////////////////////////////////////////////////////////

    @POST("api/Produtos/cadastrar-produto")
    Call<Void> cadastrarProduto(@Header("Authorization") String token, @Body SuperClassProd.ProdutoDto produtoDto);

    @POST("api/Produtos/buscar-produtos-por-nome-similar")
    Call<List<SuperClassProd.ProdutoDto>> buscarProdutosPorNomeSimilar(
            @Header("Authorization") String token,
            @Body String nome
    );

    @POST("api/Produtos/buscar-produtos-por-campo")
    Call<List<SuperClassProd.ProdutoDto>> buscarProdutosPorCampo(
            @Header("Authorization") String token,
            @Body SuperClassProd.CamposProdutoDto filtro
    );

    @GET("api/Produtos")
    Call<List<SuperClassProd.ProdutoDto>> buscarTodosProdutos(@Header("Authorization") String token);

    @GET("api/Produtos/usuario/{id_usuario}")
    Call<List<SuperClassProd.ProdutoDto>> buscarProdutosPorUsuario(@Header("Authorization") String token, @Path("id_usuario") int idUsuario);

    @GET("api/Produtos/produto/{id}")
    Call<SuperClassProd.ProdutoDto> buscarProdutoPorId(@Header("Authorization") String token, @Path("id") int id);

    @PATCH("api/Produtos/{id}")
    Call<SuperClassProd.ProdutoDto> alterarProduto(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body SuperClassProd.CamposProdutoDto campo);

    @PATCH("api/Produtos/Alterar-Imagem-por-{id}")
    Call<SuperClassProd.ProdutoDto> alterarImagemProduto(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body SuperClassProd.ImagemDto imagemDto);

    @DELETE("api/Produtos/{id}")
    Call<ResponseBody> deletarProduto(@Header("Authorization") String token, @Path("id") int id);

    // Pedidos de Compra Endpoints
    @POST("api/PedidoCompra/criar-pedido-compra")
    Call<SuperClassProd.PedidoCompraCompletoDto> criarPedidoCompra(@Header("Authorization") String token, @Body SuperClassProd.PedidoCompraCompletoDto dto);

    @POST("api/PedidoCompra/inserir-itens-em-pedido")
    Call<List<SuperClassProd.ItemCompraDto>> inserirItemCompra(@Header("Authorization") String token, @Body List<SuperClassProd.ItemCompraDto> dto);

    @POST("api/PedidoCompra/pedido-compra/Buscar-por-campo")
    Call<List<SuperClassProd.PedidoCompraCompletoDto>> pedidoBuscarPorCampo(@Header("Authorization") String token, @Body SuperClassProd.CamposProdutoDto filtro);

    @POST("api/PedidoCompra/item-compra/Buscar-por-campo")
    Call<List<SuperClassProd.ItemCompraDto>> itemBuscarPorCampo(
            @Header("Authorization") String token,
            @Body SuperClassProd.CamposProdutoDto filtro); // id_pedido deve estar dentro do filtro

    @GET("api/PedidoCompra/buscar-todos-pedidos")
    Call<List<SuperClassProd.PedidoCompraCompletoDto>> buscarComprasPedido(@Header("Authorization") String token);

    @GET("api/PedidoCompra/Itens/{id_pedido}")
    Call<List<SuperClassProd.ItemCompraDto>> buscarItensCompraPorPedido(@Header("Authorization") String token, @Path("id_pedido") int idPedido);

    @GET("api/PedidoCompra/buscar-pedidos-usuario")
    Call<List<SuperClassProd.PedidoCompraCompletoDto>> buscarComprasPedidoPorUsuario(@Header("Authorization") String token);

    @PATCH("api/PedidoCompra/alterar-pedido-por-campo/{id_pedido}")
    Call<SuperClassProd.PedidoCompraCompletoDto> alterarPedidoCompra(
            @Header("Authorization") String token,
            @Path("id_pedido") int idPedido,
            @Body SuperClassProd.CamposProdutoDto dto);

    @PATCH("api/PedidoCompra/alterar-item-do-pedido/{id_item}")
    Call<SuperClassProd.ItemCompraDto> alterarItemCompra(
            @Header("Authorization") String token,
            @Path("id_item") int idItem,
            @Body SuperClassProd.CamposProdutoDto dto);

    @DELETE("api/PedidoCompra/{id_pedido}")
    Call<Void> deletarPedidoCompra(@Header("Authorization") String token, @Path("id_pedido") int idPedido);

    @DELETE("api/PedidoCompra/Itens/{id_item}")
    Call<Void> deletarItemCompra(@Header("Authorization") String token, @Path("id_item") int idItem);
}



