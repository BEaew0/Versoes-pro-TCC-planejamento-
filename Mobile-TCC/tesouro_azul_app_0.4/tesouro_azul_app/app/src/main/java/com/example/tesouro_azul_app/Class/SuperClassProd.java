package com.example.tesouro_azul_app.Class;

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

        public double qtdTotalEstoque; // Quantidade de estoque vinda da API

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

        // Getter e Setter
        public double getQtdTotalEstoque() {
            return qtdTotalEstoque;
        }

        public void setQtdTotalEstoque(double qtdTotalEstoque) {
            this.qtdTotalEstoque = qtdTotalEstoque;
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

    public class PedidoDtoQuantidade {

        @SerializedName("iD_PEDIDO")
        private int idPedido;

        @SerializedName("iD_USUARIO_FK")
        private int idUsuarioFk;

        @SerializedName("iD_FORNECEDOR_FK")
        private Integer idFornecedorFk;  // Nullable

        @SerializedName("datA_PEDIDO")
        private String dataPedido;

        @SerializedName("valoR_PEDIDO")
        private double valorPedido;

        // Getters e Setters
        public int getIdPedido() {
            return idPedido;
        }

        public void setIdPedido(int idPedido) {
            this.idPedido = idPedido;
        }

        public int getIdUsuarioFk() {
            return idUsuarioFk;
        }

        public void setIdUsuarioFk(int idUsuarioFk) {
            this.idUsuarioFk = idUsuarioFk;
        }

        public Integer getIdFornecedorFk() {
            return idFornecedorFk;
        }

        public void setIdFornecedorFk(Integer idFornecedorFk) {
            this.idFornecedorFk = idFornecedorFk;
        }

        public String getDataPedido() {
            return dataPedido;
        }

        public void setDataPedido(String dataPedido) {
            this.dataPedido = dataPedido;
        }

        public double getValorPedido() {
            return valorPedido;
        }

        public void setValorPedido(double valorPedido) {
            this.valorPedido = valorPedido;
        }
    }

    public class ItemCompraDtoQuant {
        @SerializedName("quantidadE_ITEM_COMPRA")
        public int quantidade;

        @SerializedName("pedidoCompra")
        public PedidoCompraDto pedidoCompra;
    }

    public class PedidoCompraDto {
        @SerializedName("datA_PEDIDO")
        public String dataPedido;
    }

    public static class PedidoDto {

        @SerializedName("iD_FORNECEDOR")
        public String idFornecedor;

        @SerializedName("valoR_VALOR")
        public double valorPedido;

        public PedidoDto(String idFornecedor, double valorPedido) {
            this.idFornecedor = idFornecedor;
            this.valorPedido = valorPedido;
        }

        public String getIdFornecedor() {
            return idFornecedor;
        }

        public void setIdFornecedor(String idFornecedor) {
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
        private VendaDto pedido;

        @SerializedName("item")
        private List<ItemVendaDto> item;

        public PedidoVendaCompletoDto(VendaDto pedido, List<ItemVendaDto> item) {
            this.pedido = pedido;
            this.item = item;
        }

        public VendaDto getPedido() {
            return pedido;
        }

        public void setPedido(VendaDto pedido) {
            this.pedido = pedido;
        }

        public List<ItemVendaDto> getItem() {
            return item;
        }

        public void setItem(List<ItemVendaDto> item) {
            this.item = item;
        }
    }

        // DTO interno - Pedido (PedidoDto no backend)
        public static class VendaDto {

            @SerializedName("valor_Pedido_Venda")  // JSON correto que a API espera
            private double valorPedidoVenda;

            public VendaDto(double valorPedidoVenda) {
                this.valorPedidoVenda = valorPedidoVenda;
            }

            public double getValorPedidoVenda() {
                return valorPedidoVenda;
            }

            public void setValorPedidoVenda(double valorPedidoVenda) {
                this.valorPedidoVenda = valorPedidoVenda;
            }
        }

        // DTO interno - Itens (ItemVendaDto no backend)
        public static class ItemVendaDto {

            @SerializedName("id_Produto_FK")
            private int idProdutoFk;

            @SerializedName("lote_Venda")
            private String loteVenda;

            @SerializedName("qts_Item_Venda")
            private Double qtsItemVenda;

            @SerializedName("n_Item_Venda")
            private int nItemVenda;

            @SerializedName("desconto_Item_Venda")
            private Double descontoItemVenda;

            @SerializedName("valor_Total_Item_Venda")
            private Double valorTotalItemVenda;

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

            public int getNItemVenda() {
                return nItemVenda;
            }

            public void setNItemVenda(int nItemVenda) {
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

    public static class ItemVendaDtoQuant {
        @SerializedName("qtS_ITEM_VENDA")
        public int quantidade;

        @SerializedName("pedidoVenda")
        public PedidoVenda pedidoVenda;

        public static class PedidoVenda {
            @SerializedName("datA_PEDIDO_VENDA")
            public String dataPedidoVenda;
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

    public static class EstoqueProdutoDto {
        @SerializedName("qTD_TOTAL_ESTOQUE")
        public double qtdTotalEstoque;
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


