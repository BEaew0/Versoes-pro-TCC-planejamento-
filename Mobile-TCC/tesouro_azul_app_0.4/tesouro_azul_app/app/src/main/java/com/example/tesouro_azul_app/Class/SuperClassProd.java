package com.example.tesouro_azul_app.Class;

import java.util.List;

//@SerializedName("nomeExatoDoCampoJSON")
import com.google.gson.annotations.SerializedName;

public class SuperClassProd {

    // DTO para cadastro de produto
    public static class ProdutoDto {

        @SerializedName("iD_USUARIO")
        private int idUsuario;

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

        // Construtor
        public ProdutoDto(int idUsuario, String codProduto, String nomeProduto,
                          double valorProduto, String tipoProduto, String imgProduto) {
            this.idUsuario = idUsuario;
            this.codProduto = codProduto;
            this.nomeProduto = nomeProduto;
            this.valorProduto = valorProduto;
            this.tipoProduto = tipoProduto;
            this.imgProduto = imgProduto;
        }

        // Getters e Setters
        public int getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(int idUsuario) {
            this.idUsuario = idUsuario;
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
    }

    public static class PedidoCompraCompletoDto {
        @SerializedName("pedido")
        public PedidoDto pedido;

        @SerializedName("item")
        public List<ItemVendaDto> item;

        public PedidoCompraCompletoDto(PedidoDto pedido, List<ItemVendaDto> itens) {
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

        public void setItem(List<ItemVendaDto> item) {
            this.item = item;
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
        public int idProdutoFk;

        @SerializedName("lotE_VENDA")
        public String loteVenda;

        @SerializedName("qtS_ITEM_VENDA")
        public int quantidadeItemVenda;

        @SerializedName("n_ITEM_VENDA")
        public int nItemVenda;

        @SerializedName("descontO_ITEM_VENDA")
        public double descontoItemVenda;

        @SerializedName("valoR_TOTAL_ITEM_VENDA")
        public double valorTotalItemVenda;

        public ItemCompraDto(int idProduto, String lote, int quantidade, int nItem, double desconto, double valorTotal) {
            this.idProdutoFk = idProduto;
            this.loteVenda = lote;
            this.quantidadeItemVenda = quantidade;
            this.nItemVenda = nItem;
            this.descontoItemVenda = desconto;
            this.valorTotalItemVenda = valorTotal;
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

        public int getQuantidadeItemVenda() {
            return quantidadeItemVenda;
        }

        public void setQuantidadeItemVenda(int quantidadeItemVenda) {
            this.quantidadeItemVenda = quantidadeItemVenda;
        }

        public int getnItemVenda() {
            return nItemVenda;
        }

        public void setnItemVenda(int nItemVenda) {
            this.nItemVenda = nItemVenda;
        }

        public double getDescontoItemVenda() {
            return descontoItemVenda;
        }

        public void setDescontoItemVenda(double descontoItemVenda) {
            this.descontoItemVenda = descontoItemVenda;
        }

        public double getValorTotalItemVenda() {
            return valorTotalItemVenda;
        }

        public void setValorTotalItemVenda(double valorTotalItemVenda) {
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

    public static class ImagemDto{

        @SerializedName("imagemBase64")
        private String imagemBase64;

        public ImagemDto(String imagemBase64) {
            this.imagemBase64 = imagemBase64;
        }

        public String getImagemBase64() {
            return imagemBase64;
        }

        public void setImagemBase64(String imagemBase64) {
            this.imagemBase64 = imagemBase64;
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


