package com.example.tesouro_azul_app;

import java.util.Date;

public class ProdutoObject {
    public String COD_PRODUTO;
    public String NOME_PRODUTO;
    public String TIPO_PRODUTO;
    public Date DATA_VAL_PRODUTO;

    public ProdutoObject(String COD_PRODUTO, String NOME_PRODUTO, String TIPO_PRODUTO, Date DATA_VAL_PRODUTO) {
        this.COD_PRODUTO = COD_PRODUTO;
        this.NOME_PRODUTO = NOME_PRODUTO;
        this.TIPO_PRODUTO = TIPO_PRODUTO;
        this.DATA_VAL_PRODUTO = DATA_VAL_PRODUTO;
    }

    public String getCOD_PRODUTO() {
        return COD_PRODUTO;
    }

    public void setCOD_PRODUTO(String COD_PRODUTO) {
        this.COD_PRODUTO = COD_PRODUTO;
    }

    public String getNOME_PRODUTO() {
        return NOME_PRODUTO;
    }

    public void setNOME_PRODUTO(String NOME_PRODUTO) {
        this.NOME_PRODUTO = NOME_PRODUTO;
    }

    public String getTIPO_PRODUTO() {
        return TIPO_PRODUTO;
    }

    public void setTIPO_PRODUTO(String TIPO_PRODUTO) {
        this.TIPO_PRODUTO = TIPO_PRODUTO;
    }

    public Date getDATA_VAL_PRODUTO() {
        return DATA_VAL_PRODUTO;
    }

    public void setDATA_VAL_PRODUTO(Date DATA_VAL_PRODUTO) {
        this.DATA_VAL_PRODUTO = DATA_VAL_PRODUTO;
    }
}
