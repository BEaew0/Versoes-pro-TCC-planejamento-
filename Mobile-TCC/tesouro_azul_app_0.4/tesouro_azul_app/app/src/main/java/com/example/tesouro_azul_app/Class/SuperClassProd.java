package com.example.tesouro_azul_app.Class;

import java.util.List;

public class SuperClassProd {

    static public class CadastrarProdutoDto {
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
    static public class CamposProdutoDto {
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
    static public class Produto {
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

    static public class PedidoCompraCompletoDto {
        public PedidoDto Pedido;
        public List<ItemCompraDto> Item;

        public PedidoCompraCompletoDto(PedidoDto pedido, List<ItemCompraDto> itens) {

        }
    }

    static public class PedidoDto {
        public int ID_FORNECEDOR;
        public float VALOR_VALOR;
        public Double VALOR_PEDIDO_VENDA;

        public PedidoDto(int idUsuario, int i, double valorTotal) {

        }
    }

    static public class ItemCompraDto {
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

    static public class CamposDto {
        public String Campo;
        public String NovoValor;
    }

    public class PedidoCompraResponse {
        public int PedidoID;
        public List<ItemResponse> Itens;
    }

    public class ItemResponse {
        public int ID_ITEM_COMPRA;
        public int ID_PRODUTO_FK;
        public float QUANTIDADE_ITEM_COMPRA;
    }

    public class PedidoCompra {
        public int ID_PEDIDO;
        public int ID_USUARIO_FK;
        public int ID_FORNECEDOR_FK;
        public float VALOR_PEDIDO;
        public String DATA_PEDIDO;
    }

    public class ItemCompra {
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

    public class PedidoVendaCompletoDto {
        public PedidoDto Pedido;
        public List<ItemVendaDto> Item;
    }

    public class ItemVendaDto {
        public int ID_PRODUTO_FK;
        public String LOTE_VENDA;
        public Double QTS_ITEM_VENDA;
        public int N_ITEM_VENDA;
        public Double DESCONTO_ITEM_VENDA;
        public Double VALOR_TOTAL_ITEM_VENDA;
    }

    static public class CamposDinamicoDto {
        public String Campo;
        public String Valor;
    }

    static public class PedidoVendaResponse {
        public PedidoVenda PedidoVenda;
        public List<ItemVenda> ItensVenda;
    }

    public class PedidoVenda {
        public int ID_PEDIDO_VENDA;
        public int ID_USUARIO_FK;
        public Double VALOR_PEDIDO_VENDA;
        public String DATA_PEDIDO_VENDA;
        // Include other fields as needed
    }

    public class ItemVenda {
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


    public static class SuperClassProd {

        // DTO para cadastro de produto
        public static class CadastrarProdutoDto {
            private int ID_USUARIO;
            private String COD_PRODUTO;
            private String NOME_PRODUTO;
            private double VALOR_PRODUTO;
            private String TIPO_PRODUTO;
            private String IMG_PRODUTO;

            public CadastrarProdutoDto(int idUsuario, String codProduto, String nomeProduto,
                                       double valorProduto, String tipoProduto, String imgProduto) {
                this.ID_USUARIO = idUsuario;
                this.COD_PRODUTO = codProduto;
                this.NOME_PRODUTO = nomeProduto;
                this.VALOR_PRODUTO = valorProduto;
                this.TIPO_PRODUTO = tipoProduto;
                this.IMG_PRODUTO = imgProduto;
            }

            // Getters e Setters
            public int getID_USUARIO() { return ID_USUARIO; }
            public String getCOD_PRODUTO() { return COD_PRODUTO; }
            public String getNOME_PRODUTO() { return NOME_PRODUTO; }
            public double getVALOR_PRODUTO() { return VALOR_PRODUTO; }
            public String getTIPO_PRODUTO() { return TIPO_PRODUTO; }
            public String getIMG_PRODUTO() { return IMG_PRODUTO; }

            public void setID_USUARIO(int ID_USUARIO) { this.ID_USUARIO = ID_USUARIO; }
            public void setCOD_PRODUTO(String COD_PRODUTO) { this.COD_PRODUTO = COD_PRODUTO; }
            public void setNOME_PRODUTO(String NOME_PRODUTO) { this.NOME_PRODUTO = NOME_PRODUTO; }
            public void setVALOR_PRODUTO(double VALOR_PRODUTO) { this.VALOR_PRODUTO = VALOR_PRODUTO; }
            public void setTIPO_PRODUTO(String TIPO_PRODUTO) { this.TIPO_PRODUTO = TIPO_PRODUTO; }
            public void setIMG_PRODUTO(String IMG_PRODUTO) { this.IMG_PRODUTO = IMG_PRODUTO; }
        }

        // DTO para atualização de campos
        public static class CamposProdutoDto {
            private String campo;
            private String novoValor;

            public CamposProdutoDto(String campo, String novoValor) {
                this.campo = campo;
                this.novoValor = novoValor;
            }

            // Getters
            public String getCampo() { return campo; }
            public String getNovoValor() { return novoValor; }
        }

        // Modelo de Produto
        public static class Produto {
            private int ID_PRODUTO;
            private int ID_USUARIO_FK;
            private String COD_PRODUTO;
            private String NOME_PRODUTO;
            private double VALOR_PRODUTO;
            private String TIPO_PRODUTO;
            private byte[] IMG_PRODUTO;

            // Getters e Setters
            public int getID_PRODUTO() { return ID_PRODUTO; }
            public int getID_USUARIO_FK() { return ID_USUARIO_FK; }
            public String getCOD_PRODUTO() { return COD_PRODUTO; }
            public String getNOME_PRODUTO() { return NOME_PRODUTO; }
            public double getVALOR_PRODUTO() { return VALOR_PRODUTO; }
            public String getTIPO_PRODUTO() { return TIPO_PRODUTO; }
            public byte[] getIMG_PRODUTO() { return IMG_PRODUTO; }

            public void setID_PRODUTO(int ID_PRODUTO) { this.ID_PRODUTO = ID_PRODUTO; }
            public void setID_USUARIO_FK(int ID_USUARIO_FK) { this.ID_USUARIO_FK = ID_USUARIO_FK; }
            public void setCOD_PRODUTO(String COD_PRODUTO) { this.COD_PRODUTO = COD_PRODUTO; }
            public void setNOME_PRODUTO(String NOME_PRODUTO) { this.NOME_PRODUTO = NOME_PRODUTO; }
            public void setVALOR_PRODUTO(double VALOR_PRODUTO) { this.VALOR_PRODUTO = VALOR_PRODUTO; }
            public void setTIPO_PRODUTO(String TIPO_PRODUTO) { this.TIPO_PRODUTO = TIPO_PRODUTO; }
            public void setIMG_PRODUTO(byte[] IMG_PRODUTO) { this.IMG_PRODUTO = IMG_PRODUTO; }
        }

        // DTOs para Pedido de Compra
        public static class PedidoCompraCompletoDto {
            private PedidoDto pedido;
            private List<ItemCompraDto> item;

            public PedidoCompraCompletoDto(PedidoDto pedido, List<ItemCompraDto> item) {
                this.pedido = pedido;
                this.item = item;
            }

            // Getters
            public PedidoDto getPedido() { return pedido; }
            public List<ItemCompraDto> getItem() { return item; }
        }

        public static class PedidoDto {
            private int ID_USUARIO_FK;
            private int ID_FORNECEDOR;
            private double VALOR_PEDIDO;

            public PedidoDto(int ID_USUARIO_FK, int ID_FORNECEDOR, double VALOR_PEDIDO) {
                this.ID_USUARIO_FK = ID_USUARIO_FK;
                this.ID_FORNECEDOR = ID_FORNECEDOR;
                this.VALOR_PEDIDO = VALOR_PEDIDO;
            }

            // Getters
            public int getID_USUARIO_FK() { return ID_USUARIO_FK; }
            public int getID_FORNECEDOR() { return ID_FORNECEDOR; }
            public double getVALOR_PEDIDO() { return VALOR_PEDIDO; }
        }

        public static class ItemCompraDto {
            private int ID_PRODUTO_FK;
            private String LOTE_COMPRA;
            private double QUANTIDADE_ITEM_COMPRA;
            private int N_ITEM_COMPRA;
            private double VALOR_TOTAL_ITEM_COMPRA;
            private String VAL_ITEM_COMPRA;

            public ItemCompraDto(int ID_PRODUTO_FK, String LOTE_COMPRA, double QUANTIDADE_ITEM_COMPRA,
                                 int N_ITEM_COMPRA, double VALOR_TOTAL_ITEM_COMPRA, String VAL_ITEM_COMPRA) {
                this.ID_PRODUTO_FK = ID_PRODUTO_FK;
                this.LOTE_COMPRA = LOTE_COMPRA;
                this.QUANTIDADE_ITEM_COMPRA = QUANTIDADE_ITEM_COMPRA;
                this.N_ITEM_COMPRA = N_ITEM_COMPRA;
                this.VALOR_TOTAL_ITEM_COMPRA = VALOR_TOTAL_ITEM_COMPRA;
                this.VAL_ITEM_COMPRA = VAL_ITEM_COMPRA;
            }

            // Getters
            public int getID_PRODUTO_FK() { return ID_PRODUTO_FK; }
            public String getLOTE_COMPRA() { return LOTE_COMPRA; }
            public double getQUANTIDADE_ITEM_COMPRA() { return QUANTIDADE_ITEM_COMPRA; }
            public int getN_ITEM_COMPRA() { return N_ITEM_COMPRA; }
            public double getVALOR_TOTAL_ITEM_COMPRA() { return VALOR_TOTAL_ITEM_COMPRA; }
            public String getVAL_ITEM_COMPRA() { return VAL_ITEM_COMPRA; }
        }

        // DTOs para Pedido de Venda
        public static class PedidoVendaCompletoDto {
            private PedidoVendaDto pedido;
            private List<ItemVendaDto> item;

            public PedidoVendaCompletoDto(PedidoVendaDto pedido, List<ItemVendaDto> item) {
                this.pedido = pedido;
                this.item = item;
            }

            // Getters
            public PedidoVendaDto getPedido() { return pedido; }
            public List<ItemVendaDto> getItem() { return item; }
        }

        public static class PedidoVendaDto {
            private int ID_USUARIO_FK;
            private double VALOR_PEDIDO_VENDA;

            public PedidoVendaDto(int ID_USUARIO_FK, double VALOR_PEDIDO_VENDA) {
                this.ID_USUARIO_FK = ID_USUARIO_FK;
                this.VALOR_PEDIDO_VENDA = VALOR_PEDIDO_VENDA;
            }

            // Getters
            public int getID_USUARIO_FK() { return ID_USUARIO_FK; }
            public double getVALOR_PEDIDO_VENDA() { return VALOR_PEDIDO_VENDA; }
        }

        public static class ItemVendaDto {
            private int ID_PRODUTO_FK;
            private String LOTE_VENDA;
            private double QTS_ITEM_VENDA;
            private int N_ITEM_VENDA;
            private double DESCONTO_ITEM_VENDA;
            private double VALOR_TOTAL_ITEM_VENDA;

            public ItemVendaDto(int ID_PRODUTO_FK, String LOTE_VENDA, double QTS_ITEM_VENDA,
                                int N_ITEM_VENDA, double DESCONTO_ITEM_VENDA, double VALOR_TOTAL_ITEM_VENDA) {
                this.ID_PRODUTO_FK = ID_PRODUTO_FK;
                this.LOTE_VENDA = LOTE_VENDA;
                this.QTS_ITEM_VENDA = QTS_ITEM_VENDA;
                this.N_ITEM_VENDA = N_ITEM_VENDA;
                this.DESCONTO_ITEM_VENDA = DESCONTO_ITEM_VENDA;
                this.VALOR_TOTAL_ITEM_VENDA = VALOR_TOTAL_ITEM_VENDA;
            }

            // Getters
            public int getID_PRODUTO_FK() { return ID_PRODUTO_FK; }
            public String getLOTE_VENDA() { return LOTE_VENDA; }
            public double getQTS_ITEM_VENDA() { return QTS_ITEM_VENDA; }
            public int getN_ITEM_VENDA() { return N_ITEM_VENDA; }
            public double getDESCONTO_ITEM_VENDA() { return DESCONTO_ITEM_VENDA; }
            public double getVALOR_TOTAL_ITEM_VENDA() { return VALOR_TOTAL_ITEM_VENDA; }
        }

        // Classes de resposta
        public static class PedidoCompraResponse {
            private int PedidoID;
            private List<ItemCompraResponse> Itens;

            public PedidoCompraResponse(int PedidoID, List<ItemCompraResponse> Itens) {
                this.PedidoID = PedidoID;
                this.Itens = Itens;
            }

            // Getters
            public int getPedidoID() { return PedidoID; }
            public List<ItemCompraResponse> getItens() { return Itens; }
        }

        public static class ItemCompraResponse {
            private int ID_ITEM_COMPRA;
            private int ID_PRODUTO_FK;
            private double QUANTIDADE_ITEM_COMPRA;

            public ItemCompraResponse(int ID_ITEM_COMPRA, int ID_PRODUTO_FK, double QUANTIDADE_ITEM_COMPRA) {
                this.ID_ITEM_COMPRA = ID_ITEM_COMPRA;
                this.ID_PRODUTO_FK = ID_PRODUTO_FK;
                this.QUANTIDADE_ITEM_COMPRA = QUANTIDADE_ITEM_COMPRA;
            }

            // Getters
            public int getID_ITEM_COMPRA() { return ID_ITEM_COMPRA; }
            public int getID_PRODUTO_FK() { return ID_PRODUTO_FK; }
            public double getQUANTIDADE_ITEM_COMPRA() { return QUANTIDADE_ITEM_COMPRA; }
        }

        public static class PedidoVendaResponse {
            private int PedidoVendaID;
            private List<ItemVendaResponse> Itens;

            public PedidoVendaResponse(int PedidoVendaID, List<ItemVendaResponse> Itens) {
                this.PedidoVendaID = PedidoVendaID;
                this.Itens = Itens;
            }

            // Getters
            public int getPedidoVendaID() { return PedidoVendaID; }
            public List<ItemVendaResponse> getItens() { return Itens; }
        }

        public static class ItemVendaResponse {
            private int ID_ITEM_VENDA;
            private int ID_PRODUTO_FK;
            private double QTS_ITEM_VENDA;

            public ItemVendaResponse(int ID_ITEM_VENDA, int ID_PRODUTO_FK, double QTS_ITEM_VENDA) {
                this.ID_ITEM_VENDA = ID_ITEM_VENDA;
                this.ID_PRODUTO_FK = ID_PRODUTO_FK;
                this.QTS_ITEM_VENDA = QTS_ITEM_VENDA;
            }

            // Getters
            public int getID_ITEM_VENDA() { return ID_ITEM_VENDA; }
            public int getID_PRODUTO_FK() { return ID_PRODUTO_FK; }
            public double getQTS_ITEM_VENDA() { return QTS_ITEM_VENDA; }
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


