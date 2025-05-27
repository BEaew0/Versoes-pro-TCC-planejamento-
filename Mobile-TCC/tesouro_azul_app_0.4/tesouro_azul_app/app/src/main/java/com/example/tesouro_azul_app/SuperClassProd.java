package com.example.tesouro_azul_app;

public class SuperClassProd {

    static class ProdutoPost {
        public int iD_USUARIO;
        public String coD_PRODUTO;
        public String nomE_PRODUTO;
        public Double valoR_PRODUTO;
        public String tipO_PRODUTO;
        public String imG_PRODUTO;

        public ProdutoPost(int iD_USUARIO, String coD_PRODUTO, String nomE_PRODUTO, Double valoR_PRODUTO, String tipO_PRODUTO, String imG_PRODUTO) {
            this.iD_USUARIO = iD_USUARIO;
            this.coD_PRODUTO = coD_PRODUTO;
            this.nomE_PRODUTO = nomE_PRODUTO;
            this.valoR_PRODUTO = valoR_PRODUTO;
            this.tipO_PRODUTO = tipO_PRODUTO;
            this.imG_PRODUTO = imG_PRODUTO;
        }

        public int getiD_USUARIO() {
            return iD_USUARIO;
        }

        public void setiD_USUARIO(int iD_USUARIO) {
            this.iD_USUARIO = iD_USUARIO;
        }

        public String getCoD_PRODUTO() {
            return coD_PRODUTO;
        }

        public void setCoD_PRODUTO(String coD_PRODUTO) {
            this.coD_PRODUTO = coD_PRODUTO;
        }

        public String getNomE_PRODUTO() {
            return nomE_PRODUTO;
        }

        public void setNomE_PRODUTO(String nomE_PRODUTO) {
            this.nomE_PRODUTO = nomE_PRODUTO;
        }

        public Double getValoR_PRODUTO() {
            return valoR_PRODUTO;
        }

        public void setValoR_PRODUTO(Double valoR_PRODUTO) {
            this.valoR_PRODUTO = valoR_PRODUTO;
        }

        public String getTipO_PRODUTO() {
            return tipO_PRODUTO;
        }

        public void setTipO_PRODUTO(String tipO_PRODUTO) {
            this.tipO_PRODUTO = tipO_PRODUTO;
        }

        public String getImG_PRODUTO() {
            return imG_PRODUTO;
        }

        public void setImG_PRODUTO(String imG_PRODUTO) {
            this.imG_PRODUTO = imG_PRODUTO;
        }
    }

    static class ProdutoGet {
        public int iD_PRODUTO;
        public int iD_USUARIO_FK;
        public String coD_PRODUTO;
        public String nomE_PRODUTO;
        public Double valoR_PRODUTO;
        public String tipO_PRODUTO;
        public String imG_PRODUTO;
        public String fornecedor;

        public ProdutoGet(int iD_PRODUTO, int iD_USUARIO_FK, String coD_PRODUTO, String nomE_PRODUTO, Double valoR_PRODUTO, String tipO_PRODUTO, String imG_PRODUTO, String fornecedor) {
            this.iD_PRODUTO = iD_PRODUTO;
            this.iD_USUARIO_FK = iD_USUARIO_FK;
            this.coD_PRODUTO = coD_PRODUTO;
            this.nomE_PRODUTO = nomE_PRODUTO;
            this.valoR_PRODUTO = valoR_PRODUTO;
            this.tipO_PRODUTO = tipO_PRODUTO;
            this.imG_PRODUTO = imG_PRODUTO;
            this.fornecedor = fornecedor;
        }

        public int getiD_PRODUTO() {
            return iD_PRODUTO;
        }

        public void setiD_PRODUTO(int iD_PRODUTO) {
            this.iD_PRODUTO = iD_PRODUTO;
        }

        public int getiD_USUARIO_FK() {
            return iD_USUARIO_FK;
        }

        public void setiD_USUARIO_FK(int iD_USUARIO_FK) {
            this.iD_USUARIO_FK = iD_USUARIO_FK;
        }

        public String getCoD_PRODUTO() {
            return coD_PRODUTO;
        }

        public void setCoD_PRODUTO(String coD_PRODUTO) {
            this.coD_PRODUTO = coD_PRODUTO;
        }

        public String getNomE_PRODUTO() {
            return nomE_PRODUTO;
        }

        public void setNomE_PRODUTO(String nomE_PRODUTO) {
            this.nomE_PRODUTO = nomE_PRODUTO;
        }

        public Double getValoR_PRODUTO() {
            return valoR_PRODUTO;
        }

        public void setValoR_PRODUTO(Double valoR_PRODUTO) {
            this.valoR_PRODUTO = valoR_PRODUTO;
        }

        public String getTipO_PRODUTO() {
            return tipO_PRODUTO;
        }

        public void setTipO_PRODUTO(String tipO_PRODUTO) {
            this.tipO_PRODUTO = tipO_PRODUTO;
        }

        public String getImG_PRODUTO() {
            return imG_PRODUTO;
        }

        public void setImG_PRODUTO(String imG_PRODUTO) {
            this.imG_PRODUTO = imG_PRODUTO;
        }

        public String getFornecedor() {
            return fornecedor;
        }

        public void setFornecedor(String fornecedor) {
            this.fornecedor = fornecedor;
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


