package com.example.tesouro_azul_app;

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

        public int getID_USUARIO() { return ID_USUARIO; }
        public String getCOD_PRODUTO() { return COD_PRODUTO; }
        public String getNOME_PRODUTO() { return NOME_PRODUTO; }
        public double getVALOR_PRODUTO() { return VALOR_PRODUTO; }
        public String getTIPO_PRODUTO() { return TIPO_PRODUTO; }
        public String getIMG_PRODUTO() { return IMG_PRODUTO; }
    }

    // DTO para atualização de campos
   static class CamposProdutoDto {
        private String Campo;
        private String NovoValor;

        public CamposProdutoDto(String campo, String novoValor) {
            this.Campo = campo;
            this.NovoValor = novoValor;
        }

        public String getCampo() { return Campo; }
        public String getNovoValor() { return NovoValor; }
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

        public int getID_PRODUTO() { return ID_PRODUTO; }
        public int getID_USUARIO_FK() { return ID_USUARIO_FK; }
        public String getCOD_PRODUTO() { return COD_PRODUTO; }
        public String getNOME_PRODUTO() { return NOME_PRODUTO; }
        public double getVALOR_PRODUTO() { return VALOR_PRODUTO; }
        public String getTIPO_PRODUTO() { return TIPO_PRODUTO; }
        public byte[] getIMG_PRODUTO() { return IMG_PRODUTO; }
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


