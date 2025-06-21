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
    public static class PedidoCompraCompletoDto {
        @SerializedName("pedido")
        public PedidoDto pedido;

        @SerializedName("item")
        public List<ItemCompraDto> item;

        public PedidoCompraCompletoDto(PedidoDto pedido, List<ItemCompraDto> itens) {
            this.pedido = pedido;
            this.item = itens;
        }

        public PedidoDto getPedido() {
            return pedido;
        }

        public void setPedido(PedidoDto pedido) {
            this.pedido = pedido;
        }

        public List<ItemCompraDto> getItem() {
            return item;
        }

        public void setItem() {
            setItem(null);
        }

        public void setItem(List<ItemCompraDto> item) {
            this.item = item;
        }
    }
    public static class PedidoCompraResponse {
        @SerializedName("Pedido")
        private PedidoCompraCompletoDto pedidoCompra;

        @SerializedName("Itens")
        private List<ItemCompraDto> itensCompra;

        // Construtor
        public PedidoCompraResponse(PedidoCompraCompletoDto pedidoCompra, List<ItemCompraDto> itensCompra) {
            this.pedidoCompra = pedidoCompra;
            this.itensCompra = itensCompra;
        }

        // Getters e Setters
        public PedidoCompraCompletoDto getPedidoCompra() {
            return pedidoCompra;
        }

        public void setPedidoCompra(PedidoCompraCompletoDto pedidoCompra) {
            this.pedidoCompra = pedidoCompra;
        }

        public List<ItemCompraDto> getItensCompra() {
            return itensCompra;
        }

        public void setItensCompra(List<ItemCompraDto> itensCompra) {
            this.itensCompra = itensCompra;
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

    public static class PedidoDto {

        @SerializedName("valoR_PEDIDO_VENDA")
        public Double valorPedidoVenda;

        public PedidoDto(Double valorPedidoVenda) {
            this.valorPedidoVenda = valorPedidoVenda;
        }

        public Double getValorPedidoVenda() {
            return valorPedidoVenda;
        }

        public void setValorPedidoVenda(Double valorPedidoVenda) {
            this.valorPedidoVenda = valorPedidoVenda;
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

    public static class ItemCompraDto {

        @SerializedName("iD_PRODUTO_FK")
        private int idProdutoFk;

        @SerializedName("iD_PEDIDO_FK")
        private int idPedidoFk;

        @SerializedName("vaL_ITEM_COMPRA")
        private String valItemCompra;

        @SerializedName("lotE_COMPRA")
        private String loteCompra;

        @SerializedName("quantidadE_ITEM_COMPRA")
        private int quantidadeItemCompra;

        @SerializedName("n_ITEM_COMPRA")
        private int nItemCompra;

        @SerializedName("valoR_TOTAL_ITEM_COMPRA")
        private double valorTotalItemCompra;

        // Construtor
        public ItemCompraDto(int idProdutoFk, int idPedidoFk, String valItemCompra,
                             String loteCompra, int quantidadeItemCompra,
                             int nItemCompra, double valorTotalItemCompra) {
            this.idProdutoFk = idProdutoFk;
            this.idPedidoFk = idPedidoFk;
            this.valItemCompra = valItemCompra;
            this.loteCompra = loteCompra;
            this.quantidadeItemCompra = quantidadeItemCompra;
            this.nItemCompra = nItemCompra;
            this.valorTotalItemCompra = valorTotalItemCompra;
        }

        // Getters e Setters
        public int getIdProdutoFk() {
            return idProdutoFk;
        }

        public void setIdProdutoFk(int idProdutoFk) {
            this.idProdutoFk = idProdutoFk;
        }

        public int getIdPedidoFk() {
            return idPedidoFk;
        }

        public void setIdPedidoFk(int idPedidoFk) {
            this.idPedidoFk = idPedidoFk;
        }

        public String getValItemCompra() {
            return valItemCompra;
        }

        public void setValItemCompra(String valItemCompra) {
            this.valItemCompra = valItemCompra;
        }

        public String getLoteCompra() {
            return loteCompra;
        }

        public void setLoteCompra(String loteCompra) {
            this.loteCompra = loteCompra;
        }

        public int getQuantidadeItemCompra() {
            return quantidadeItemCompra;
        }

        public void setQuantidadeItemCompra(int quantidadeItemCompra) {
            this.quantidadeItemCompra = quantidadeItemCompra;
        }

        public int getnItemCompra() {
            return nItemCompra;
        }

        public void setnItemCompra(int nItemCompra) {
            this.nItemCompra = nItemCompra;
        }

        public double getValorTotalItemCompra() {
            return valorTotalItemCompra;
        }

        public void setValorTotalItemCompra(double valorTotalItemCompra) {
            this.valorTotalItemCompra = valorTotalItemCompra;
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


