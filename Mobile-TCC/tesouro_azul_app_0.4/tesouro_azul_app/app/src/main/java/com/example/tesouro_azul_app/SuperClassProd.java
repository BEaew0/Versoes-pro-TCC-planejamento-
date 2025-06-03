package com.example.tesouro_azul_app;

import java.util.List;

public class SuperClassProd {

    static class CadastrarProdutoDto {
        private int ID_USUARIO;
        private String COD_PRODUTO;
        private String NOME_PRODUTO;
        private double VALOR_PRODUTO;
        private String TIPO_PRODUTO;
        private String IMG_PRODUTO;

        // Construtor
        public CadastrarProdutoDto(int idUsuario, String codProduto, String nomeProduto,
                                   double valorProduto, String tipoProduto, String imgProduto) {
            this.ID_USUARIO = idUsuario;
            this.COD_PRODUTO = codProduto;
            this.NOME_PRODUTO = nomeProduto;
            this.VALOR_PRODUTO = valorProduto;
            this.TIPO_PRODUTO = tipoProduto;
            this.IMG_PRODUTO = imgProduto;
        }

        // Getters e Setters...

        public void setID_USUARIO(int ID_USUARIO) {
            this.ID_USUARIO = ID_USUARIO;
        }

        public void setCOD_PRODUTO(String COD_PRODUTO) {
            this.COD_PRODUTO = COD_PRODUTO;
        }

        public void setNOME_PRODUTO(String NOME_PRODUTO) {
            this.NOME_PRODUTO = NOME_PRODUTO;
        }

        public void setVALOR_PRODUTO(double VALOR_PRODUTO) {
            this.VALOR_PRODUTO = VALOR_PRODUTO;
        }

        public void setTIPO_PRODUTO(String TIPO_PRODUTO) {
            this.TIPO_PRODUTO = TIPO_PRODUTO;
        }

        public void setIMG_PRODUTO(String IMG_PRODUTO) {
            this.IMG_PRODUTO = IMG_PRODUTO;
        }

        public int getID_USUARIO() {
            return ID_USUARIO;
        }

        public String getCOD_PRODUTO() {
            return COD_PRODUTO;
        }

        public String getNOME_PRODUTO() {
            return NOME_PRODUTO;
        }

        public double getVALOR_PRODUTO() {
            return VALOR_PRODUTO;
        }

        public String getTIPO_PRODUTO() {
            return TIPO_PRODUTO;
        }

        public String getIMG_PRODUTO() {
            return IMG_PRODUTO;
        }
    }

    // DTO para atualização de campos
    static class CamposProdutoDto {
        private String Campo;
        private String NovoValor;

        public CamposProdutoDto(String campo, String novoValor) {
            this.Campo = campo;
            this.NovoValor = novoValor;
        }

        public String getCampo() {
            return Campo;
        }

        public String getNovoValor() {
            return NovoValor;
        }
    }

    // Modelo de Produto
    static class Produto {
        private int ID_PRODUTO;
        private int ID_USUARIO_FK;
        private String COD_PRODUTO;
        private String NOME_PRODUTO;
        private double VALOR_PRODUTO;
        private String TIPO_PRODUTO;
        private byte[] IMG_PRODUTO;

        // Getters e Setters...

        public void setID_PRODUTO(int ID_PRODUTO) {
            this.ID_PRODUTO = ID_PRODUTO;
        }

        public void setID_USUARIO_FK(int ID_USUARIO_FK) {
            this.ID_USUARIO_FK = ID_USUARIO_FK;
        }

        public void setCOD_PRODUTO(String COD_PRODUTO) {
            this.COD_PRODUTO = COD_PRODUTO;
        }

        public void setNOME_PRODUTO(String NOME_PRODUTO) {
            this.NOME_PRODUTO = NOME_PRODUTO;
        }

        public void setVALOR_PRODUTO(double VALOR_PRODUTO) {
            this.VALOR_PRODUTO = VALOR_PRODUTO;
        }

        public void setTIPO_PRODUTO(String TIPO_PRODUTO) {
            this.TIPO_PRODUTO = TIPO_PRODUTO;
        }

        public void setIMG_PRODUTO(byte[] IMG_PRODUTO) {
            this.IMG_PRODUTO = IMG_PRODUTO;
        }

        public int getID_PRODUTO() {
            return ID_PRODUTO;
        }

        public int getID_USUARIO_FK() {
            return ID_USUARIO_FK;
        }

        public String getCOD_PRODUTO() {
            return COD_PRODUTO;
        }

        public String getNOME_PRODUTO() {
            return NOME_PRODUTO;
        }

        public double getVALOR_PRODUTO() {
            return VALOR_PRODUTO;
        }

        public String getTIPO_PRODUTO() {
            return TIPO_PRODUTO;
        }

        public byte[] getIMG_PRODUTO() {
            return IMG_PRODUTO;
        }
    }

    static class PedidoCompraCompletoDto {
        public PedidoDto Pedido;
        public List<ItemCompraDto> Item;

        public PedidoCompraCompletoDto(PedidoDto pedido, List<ItemCompraDto> itens) {

        }
    }

    static class PedidoDto {
        public int ID_FORNECEDOR;
        public float VALOR_VALOR;
        public Double VALOR_PEDIDO_VENDA;

        public PedidoDto(int idUsuario, int i, double valorTotal) {

        }
    }

    static class ItemCompraDto {
        public int ID_PRODUTO_FK;
        public int ID_PEDIDO_FK;
        public String VAL_ITEM_COMPRA;
        public String LOTE_COMPRA;
        public float QUANTIDADE_ITEM_COMPRA;
        public int N_ITEM_COMPRA;
        public float VALOR_TOTAL_ITEM_COMPRA;


        public ItemCompraDto(int idProduto, String lote, double quantidade, int i, double valorTotal, String dataFormatada) {

        }
    }

    class CamposDto {
        public String Campo;
        public String NovoValor;
    }

    class PedidoCompraResponse {
        public int PedidoID;
        public List<ItemResponse> Itens;
    }

    class ItemResponse {
        public int ID_ITEM_COMPRA;
        public int ID_PRODUTO_FK;
        public float QUANTIDADE_ITEM_COMPRA;
    }

    class PedidoCompra {
        public int ID_PEDIDO;
        public int ID_USUARIO_FK;
        public int ID_FORNECEDOR_FK;
        public float VALOR_PEDIDO;
        public String DATA_PEDIDO;
    }

    class ItemCompra {
        public int ID_ITEM_COMPRA;
        public int ID_PRODUTO_FK;
        public int ID_PEDIDO_FK;
        public String VAL_ITEM_COMPRA;
        public String LOTE_COMPRA;
        public float QUANTIDADE_ITEM_COMPRA;
        public int N_ITEM_COMPRA;
        public float VALOR_TOTAL_ITEM_COMPRA;
        public String ESTADO_ITEM_COMPRA;
    }

    class PedidoVendaCompletoDto {
        public PedidoDto Pedido;
        public List<ItemVendaDto> Item;
    }

    class ItemVendaDto {
        public int ID_PRODUTO_FK;
        public String LOTE_VENDA;
        public Double QTS_ITEM_VENDA;
        public int N_ITEM_VENDA;
        public Double DESCONTO_ITEM_VENDA;
        public Double VALOR_TOTAL_ITEM_VENDA;
    }

    class CamposDinamicoDto {
        public String Campo;
        public String Valor;
    }

    class PedidoVendaResponse {
        public PedidoVenda PedidoVenda;
        public List<ItemVenda> ItensVenda;
    }

    class PedidoVenda {
        public int ID_PEDIDO_VENDA;
        public int ID_USUARIO_FK;
        public Double VALOR_PEDIDO_VENDA;
        public String DATA_PEDIDO_VENDA;
        // Include other fields as needed
    }

    class ItemVenda {
        public int ID_ITEM_VENDA;
        public int ID_PRODUTO_FK;
        public int ID_PEDIDO_VENDA_FK;
        public String LOTE_VENDA;
        public Double QTS_ITEM_VENDA;
        public int N_ITEM_VENDA;
        public Double DESCONTO_ITEM_VENDA;
        public Double VALOR_TOTAL_ITEM_VENDA;
        // Include other fields as needed
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


