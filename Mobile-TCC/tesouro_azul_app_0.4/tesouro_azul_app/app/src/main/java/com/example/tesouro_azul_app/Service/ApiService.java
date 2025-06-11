package com.example.tesouro_azul_app.Service;

import com.example.tesouro_azul_app.Class.SuperClassProd;
import com.example.tesouro_azul_app.Class.SuperClassUser;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Criar Usuario
    @POST("api/Usuarios")
    Call<SuperClassUser.Usuario> criarUsuario(@Body SuperClassUser.CriarUsuarioDto usuarioDto);

    @PATCH("api/desativar-usuario")
    Call<ResponseBody> desativarUsuario(
            @Header("Authorization") String token
    );

    // Buscar todos os Usuarios
    @GET("api/Usuarios")
    Call<List<SuperClassUser.Usuario>> buscarUsuarios(@Header("Authorization") String token);

    // Buscar Usuario por ID
    @GET("api/Usuarios/{id}")
    Call<SuperClassUser.Usuario> buscarUsuarioPorId(@Header("Authorization") String token, @Path("id") int id);

    // Atualizar campo do Usuario
    @PATCH("api/Usuarios/{id}/alterar-campo")
    Call<SuperClassUser.Usuario> alterarCamposUsuario(@Header("Authorization") String token, @Path("id") int id, @Body SuperClassUser.AtualizarCampoUsuarioDto dto);

    @GET("api/Usuarios/Buscar-Imagem")
    Call<ResponseBody> buscarUsuarioFoto(
            @Header("Authorization") String token
    );

    @PATCH("api/Usuarios/alterar-imagem")
    Call<ResponseBody> atualizarImagem(
            @Header("Authorization") String token,
            @Body SuperClassProd.ImagemDto dto
    );

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

    @POST("/buscar-produtos-por-nome-similar")
    Call<List<SuperClassProd.ProdutoDto>> buscarProdutosPorNomeSimilar(@Body String nomeProdTxt);


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


    // Endpoint para criar um pedido de venda completo
    @POST("criar-pedido-venda")
    Call<SuperClassProd.PedidoVendaCompletoDto> criarPedidoVenda(
            @Header("Authorization") String token,
            @Body SuperClassProd.PedidoVendaCompletoDto pedidoVenda
    );

    // Endpoint para inserir itens em um pedido de venda existente
    @POST("inserir-itens-em-pedido-venda/{id_pedido_venda}")
    Call<List<SuperClassProd.ItemVendaDto>> inserirItensEmPedidoVenda(
            @Header("Authorization") String token,
            @Body List<SuperClassProd.ItemVendaDto> itens
    );

    // Endpoint para buscar pedidos de venda por campo específico
    @POST("pedido-venda/buscar-por-campo")
    Call<List<SuperClassProd.PedidoVendaCompletoDto>> buscarPedidoVendaPorCampo(
            @Header("Authorization") String token,
            @Body SuperClassProd.CamposProdutoDto filtro
    );

    // Endpoint para buscar itens de venda por campo específico
    @POST("itens-venda/buscar-por-campo")
    Call<List<SuperClassProd.ItemVendaDto>> buscarItensVendaPorCampo(
            @Header("Authorization") String token,
            @Body SuperClassProd.CamposProdutoDto filtro
    );

    // Endpoint para buscar todos os pedidos de venda (apenas admin)
    @GET("buscar-todos-pedidos")
    Call<List<SuperClassProd.PedidoVendaCompletoDto>> buscarTodosPedidosVenda(
            @Header("Authorization") String token
    );

    // Endpoint para buscar pedidos de venda do usuário atual
    @GET("buscar-pedidos-por-usuario")
    Call<List<SuperClassProd.PedidoVendaCompletoDto>> buscarPedidosVendaPorUsuario(
            @Header("Authorization") String token
    );

    // Endpoint para buscar todos os itens de venda (apenas admin)
    @GET("buscar-todos-itens-venda")
    Call<List<SuperClassProd.ItemVendaDto>> buscarTodosItensVenda(
            @Header("Authorization") String token
    );

    // Endpoint para buscar itens de venda do usuário atual
    @GET("buscar-itens-venda-por-usuario")
    Call<List<SuperClassProd.ItemVendaDto>> buscarItensVendaPorUsuario(
            @Header("Authorization") String token
    );

    // Endpoint para alterar um pedido de venda por campo específico
    @PATCH("alterar-pedido-por-campo/{id_pedido_venda}")
    Call<SuperClassProd.PedidoVendaCompletoDto> alterarPedidoVenda(
            @Header("Authorization") String token,
            @Path("id_pedido_venda") int idPedidoVenda,
            @Body SuperClassProd.CamposProdutoDto campo
    );

    // Endpoint para alterar um item de venda específico
    @PATCH("alterar-item-do-pedido-{id}")
    Call<SuperClassProd.ItemVendaDto> alterarItemVenda(
            @Header("Authorization") String token,
            @Path("id") int idItemVenda,
            @Body SuperClassProd.CamposProdutoDto campo
    );

    // Endpoint para deletar um pedido de venda
    @DELETE("deletar-pedido-venda/{id}")
    Call<Void> deletarPedidoVenda(
            @Header("Authorization") String token,
            @Path("id") int idPedidoVenda
    );

    // Endpoint para deletar um item de venda específico
    @DELETE("deletar-item-venda/{id}")
    Call<Void> deletarItemVenda(
            @Header("Authorization") String token,
            @Path("id") int idItemVenda
    );
}




