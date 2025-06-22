package com.example.tesouro_azul_app.Class;

import java.util.Date;
import java.util.List;

//@SerializedName("nomeExatoDoCampoJSON")
import com.google.gson.annotations.SerializedName;

public class SuperClassProd {

    // DTO para cadastro de produto
    public static class ProdutoDto {

        @SerializedName("coD_PRODUTO")
        private String codProduto;

        @SerializedName("nomE_PRODUTO")
        private String nomeProduto;

        @SerializedName("valoR_PRODUTO")
        private double valorProduto;

        @SerializedName("tipO_PRODUTO")
        private String tipoProduto;

        @SerializedName("imG_PRODUTO")
        private String imgProduto;

        public ProdutoDto( String codProduto, String nomeProduto, double valorProduto, String tipoProduto, String imgProduto) {

            this.codProduto = codProduto;
            this.nomeProduto = nomeProduto;
            this.valorProduto = valorProduto;
            this.tipoProduto = tipoProduto;
            this.imgProduto = imgProduto;
        }

        // Getters e Setters

        public String getCodProduto() {
            return codProduto;
        }

        public void setCodProduto(String codProduto) {
            this.codProduto = codProduto;
        }

        public String getNomeProduto() {
            return nomeProduto;
        }

        public void setNomeProduto(String nomeProduto) {
            this.nomeProduto = nomeProduto;
        }

        public double getValorProduto() {
            return valorProduto;
        }

        public void setValorProduto(double valorProduto) {
            this.valorProduto = valorProduto;
        }

        public String getTipoProduto() {
            return tipoProduto;
        }

        public void setTipoProduto(String tipoProduto) {
            this.tipoProduto = tipoProduto;
        }

        public String getImgProduto() {
            return imgProduto;
        }

        public void setImgProduto(String imgProduto) {
            this.imgProduto = imgProduto;
        }


        // ... outros getters e setters
    }

    public static class ProdutoDtoArray {

        @SerializedName("iD_PRODUTO")
        private int idProduto;

        @SerializedName("iD_USUARIO_FK")
        private int idUsuarioFk;

        @SerializedName("coD_PRODUTO")
        private String codProduto;

        @SerializedName("nomE_PRODUTO")
        private String nomeProduto;

        @SerializedName("valoR_PRODUTO")
        private double valorProduto;

        @SerializedName("tipO_PRODUTO")
        private String tipoProduto;

        @SerializedName("imG_PRODUTO")
        private String imgProduto;

        public ProdutoDtoArray(int idProduto, int idUsuarioFk, String codProduto, String nomeProduto, double valorProduto, String tipoProduto, String imgProduto) {
            this.idProduto = idProduto;
            this.idUsuarioFk = idUsuarioFk;
            this.codProduto = codProduto;
            this.nomeProduto = nomeProduto;
            this.valorProduto = valorProduto;
            this.tipoProduto = tipoProduto;
            this.imgProduto = imgProduto;
        }

        public int getIdProduto() {
            return idProduto;
        }

        public void setIdProduto(int idProduto) {
            this.idProduto = idProduto;
        }

        public int getIdUsuarioFk() {
            return idUsuarioFk;
        }

        public void setIdUsuarioFk(int idUsuarioFk) {
            this.idUsuarioFk = idUsuarioFk;
        }

        public String getCodProduto() {
            return codProduto;
        }

        public void setCodProduto(String codProduto) {
            this.codProduto = codProduto;
        }

        public String getNomeProduto() {
            return nomeProduto;
        }

        public void setNomeProduto(String nomeProduto) {
            this.nomeProduto = nomeProduto;
        }

        public double getValorProduto() {
            return valorProduto;
        }

        public void setValorProduto(double valorProduto) {
            this.valorProduto = valorProduto;
        }

        public String getTipoProduto() {
            return tipoProduto;
        }

        public void setTipoProduto(String tipoProduto) {
            this.tipoProduto = tipoProduto;
        }

        public String getImgProduto() {
            return imgProduto;
        }

        public void setImgProduto(String imgProduto) {
            this.imgProduto = imgProduto;
        }

        // ... outros getters e setters
    }


    public static class PedidoDto {

        @SerializedName("iD_FORNECEDOR")
        public Integer idFornecedor;

        @SerializedName("valoR_VALOR")
        public double valorPedido;

        public PedidoDto(Integer idFornecedor, double valorPedido) {
            this.idFornecedor = idFornecedor;
            this.valorPedido = valorPedido;
        }

        public Integer getIdFornecedor() {
            return idFornecedor;
        }

        public void setIdFornecedor(Integer idFornecedor) {
            this.idFornecedor = idFornecedor;
        }

        public double getValorPedido() {
            return valorPedido;
        }

        public void setValorPedido(double valorPedido) {
            this.valorPedido = valorPedido;
        }
    }
    public static class ItemCompraDto {

        @SerializedName("iD_PRODUTO_FK")
        public int idProdutoFk;

        @SerializedName("iD_PEDIDO_FK")
        public int idPedidoFk;  // Pode ser 0, pois a API ignora esse campo

        @SerializedName("vaL_ITEM_COMPRA")
        public String valItemCompra;  // Data em formato ISO

        @SerializedName("lotE_COMPRA")
        public String loteCompra;

        @SerializedName("quantidadE_ITEM_COMPRA")
        public int quantidadeItemCompra;

        @SerializedName("n_ITEM_COMPRA")
        public int nItemCompra;

        @SerializedName("valoR_TOTAL_ITEM_COMPRA")
        public double valorTotalItemCompra;

        public ItemCompraDto(int idProdutoFk, int idPedidoFk, String valItemCompra, String loteCompra,
                             int quantidadeItemCompra, int nItemCompra, double valorTotalItemCompra) {
            this.idProdutoFk = idProdutoFk;
            this.idPedidoFk = idPedidoFk;
            this.valItemCompra = valItemCompra;
            this.loteCompra = loteCompra;
            this.quantidadeItemCompra = quantidadeItemCompra;
            this.nItemCompra = nItemCompra;
            this.valorTotalItemCompra = valorTotalItemCompra;
        }
    }
    public static class PedidoCompraCompletoDto {

        @SerializedName("pedido")
        public PedidoDto pedido;

        @SerializedName("item")
        public List<ItemCompraDto> item;

        public PedidoCompraCompletoDto(PedidoDto pedido, List<ItemCompraDto> item) {
            this.pedido = pedido;
            this.item = item;
        }
    }

    public static class PedidoVendaCompletoDto {
        @SerializedName("pedido")
        public PedidoDto pedido;

        @SerializedName("item")
        public List<ItemVendaDto> item;

        public PedidoVendaCompletoDto(PedidoDto pedido, List<ItemVendaDto> itens) {
            this.pedido = pedido;
            this.item = itens;
        }

        public PedidoDto getPedido() {
            return pedido;
        }

        public void setPedido(PedidoDto pedido) {
            this.pedido = pedido;
        }

        public List<ItemVendaDto> getItem() {
            return item;
        }

        public void setItem() {
            setItem(null);
        }

        public void setItem(List<ItemVendaDto> item) {
            this.item = item;
        }
    }
    public static class PedidoVendaResponse {
        @SerializedName("PedidoVenda")
        private PedidoVendaCompletoDto pedidoVenda;

        @SerializedName("ItensVenda")
        private List<ItemVendaDto> itensVenda;

        // Construtor
        public PedidoVendaResponse(PedidoVendaCompletoDto pedidoVenda, List<ItemVendaDto> itensVenda) {
            this.pedidoVenda = pedidoVenda;
            this.itensVenda = itensVenda;
        }

        // Getters e Setters
        public PedidoVendaCompletoDto getPedidoVenda() {
            return pedidoVenda;
        }

        public void setPedidoVenda(PedidoVendaCompletoDto pedidoVenda) {
            this.pedidoVenda = pedidoVenda;
        }

        public List<ItemVendaDto> getItensVenda() {
            return itensVenda;
        }

        public void setItensVenda(List<ItemVendaDto> itensVenda) {
            this.itensVenda = itensVenda;
        }
    }

    public static class ItemVendaDto {

        @SerializedName("iD_PRODUTO_FK")
        public int idProdutoFk;

        @SerializedName("lotE_VENDA")
        public String loteVenda;

        @SerializedName("qtS_ITEM_VENDA")
        public Double qtsItemVenda;

        @SerializedName("n_ITEM_VENDA")
        public int nItemVenda;

        @SerializedName("descontO_ITEM_VENDA")
        public Double descontoItemVenda;

        @SerializedName("valoR_TOTAL_ITEM_VENDA")
        public Double valorTotalItemVenda;

        public ItemVendaDto(int idProdutoFk, String loteVenda, Double qtsItemVenda, int nItemVenda,
                            Double descontoItemVenda, Double valorTotalItemVenda) {
            this.idProdutoFk = idProdutoFk;
            this.loteVenda = loteVenda;
            this.qtsItemVenda = qtsItemVenda;
            this.nItemVenda = nItemVenda;
            this.descontoItemVenda = descontoItemVenda;
            this.valorTotalItemVenda = valorTotalItemVenda;
        }

        public ItemVendaDto(int idProdutoFk, String lote, int quantidade, int nItemVenda, double descontoItemVenda, double valorTotal) {
        }

        public int getIdProdutoFk() {
            return idProdutoFk;
        }

        public void setIdProdutoFk(int idProdutoFk) {
            this.idProdutoFk = idProdutoFk;
        }

        public String getLoteVenda() {
            return loteVenda;
        }

        public void setLoteVenda(String loteVenda) {
            this.loteVenda = loteVenda;
        }

        public Double getQtsItemVenda() {
            return qtsItemVenda;
        }

        public void setQtsItemVenda(Double qtsItemVenda) {
            this.qtsItemVenda = qtsItemVenda;
        }

        public int getnItemVenda() {
            return nItemVenda;
        }

        public void setnItemVenda(int nItemVenda) {
            this.nItemVenda = nItemVenda;
        }

        public Double getDescontoItemVenda() {
            return descontoItemVenda;
        }

        public void setDescontoItemVenda(Double descontoItemVenda) {
            this.descontoItemVenda = descontoItemVenda;
        }

        public Double getValorTotalItemVenda() {
            return valorTotalItemVenda;
        }

        public void setValorTotalItemVenda(Double valorTotalItemVenda) {
            this.valorTotalItemVenda = valorTotalItemVenda;
        }
    }


    public static class CamposProdutoDto {

        @SerializedName("campo")
        private String campo;

        @SerializedName("novoValor")
        private String novoValor;

        public CamposProdutoDto(String campo, String novoValor) {
            this.campo = campo;
            this.novoValor = novoValor;
        }

        public String getCampo() {
            return campo;
        }

        public void setCampo(String campo) {
            this.campo = campo;
        }

        public String getNovoValor() {
            return novoValor;
        }

        public void setNovoValor(String novoValor) {
            this.novoValor = novoValor;
        }
    }

}


     /*Produto Get
        [
        {
        "iD_PRODUTO": 0,
        "iD_USUARIO_FK": 0,
        "coD_PRODUTO": "string",
        "nomE_PRODUTO": "string",
        "valoR_PRODUTO": 0,
        "tipO_PRODUTO": "string",
        "imG_PRODUTO": "string",
        "fornecedor": null
        }*/


