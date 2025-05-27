package com.example.tesouro_azul_app;

import java.util.Date;

public class superClassUser {

    static class UsuarioPost {
        public UsuarioPost(String nomE_USUARIO, String emaiL_USUARIO, Date datA_NASC_USUARIO, String cpF_USUARIO, String cnpJ_USUARIO, int iD_ASSINATURA_FK, String fotO_USUARIO, String senhA_USUARIO, String statuS_USUARIO) {
            this.nomE_USUARIO = nomE_USUARIO;
            this.emaiL_USUARIO = emaiL_USUARIO;
            this.datA_NASC_USUARIO = datA_NASC_USUARIO;
            this.cpF_USUARIO = cpF_USUARIO;
            this.cnpJ_USUARIO = cnpJ_USUARIO;
            this.iD_ASSINATURA_FK = iD_ASSINATURA_FK;
            this.fotO_USUARIO = fotO_USUARIO;
            this.senhA_USUARIO = senhA_USUARIO;
            this.statuS_USUARIO = statuS_USUARIO;
        }

        public String nomE_USUARIO;
        public String emaiL_USUARIO;
        public Date datA_NASC_USUARIO;
        public String cpF_USUARIO;
        public String cnpJ_USUARIO;
        public int iD_ASSINATURA_FK;
        public String fotO_USUARIO;
        public String senhA_USUARIO;
        public String statuS_USUARIO;

        public String getNomE_USUARIO() {
            return nomE_USUARIO;
        }

        public void setNomE_USUARIO(String nomE_USUARIO) {
            this.nomE_USUARIO = nomE_USUARIO;
        }

        public String getEmaiL_USUARIO() {
            return emaiL_USUARIO;
        }

        public void setEmaiL_USUARIO(String emaiL_USUARIO) {
            this.emaiL_USUARIO = emaiL_USUARIO;
        }

        public Date getDatA_NASC_USUARIO() {
            return datA_NASC_USUARIO;
        }

        public void setDatA_NASC_USUARIO(Date datA_NASC_USUARIO) {
            this.datA_NASC_USUARIO = datA_NASC_USUARIO;
        }

        public String getCpF_USUARIO() {
            return cpF_USUARIO;
        }

        public void setCpF_USUARIO(String cpF_USUARIO) {
            this.cpF_USUARIO = cpF_USUARIO;
        }

        public String getCnpJ_USUARIO() {
            return cnpJ_USUARIO;
        }

        public void setCnpJ_USUARIO(String cnpJ_USUARIO) {
            this.cnpJ_USUARIO = cnpJ_USUARIO;
        }

        public int getiD_ASSINATURA_FK() {
            return iD_ASSINATURA_FK;
        }

        public void setiD_ASSINATURA_FK(int iD_ASSINATURA_FK) {
            this.iD_ASSINATURA_FK = iD_ASSINATURA_FK;
        }

        public String getFotO_USUARIO() {
            return fotO_USUARIO;
        }

        public void setFotO_USUARIO(String fotO_USUARIO) {
            this.fotO_USUARIO = fotO_USUARIO;
        }

        public String getSenhA_USUARIO() {
            return senhA_USUARIO;
        }

        public void setSenhA_USUARIO(String senhA_USUARIO) {
            this.senhA_USUARIO = senhA_USUARIO;
        }

        public String getStatuS_USUARIO() {
            return statuS_USUARIO;
        }

        public void setStatuS_USUARIO(String statuS_USUARIO) {
            this.statuS_USUARIO = statuS_USUARIO;
        }
    }

    static class UsuarioGet {
        public String nomE_USUARIO;
        public String emaiL_USUARIO;
        public Date datA_NASC_USUARIO;
        public String cpF_USUARIO;
        public String cnpJ_USUARIO;
        public int iD_ASSINATURA_FK;
        public String fotO_USUARIO;
        public String senhA_USUARIO;
        public String statuS_USUARIO;
        public Date datA_INICIO_ASSINATURA_USUARIO;
        public Date datA_VALIDADE_ASSINATURA_USUARIO;

        public UsuarioGet(String nomE_USUARIO, String emaiL_USUARIO, Date datA_NASC_USUARIO, String cpF_USUARIO, String cnpJ_USUARIO, int iD_ASSINATURA_FK, String fotO_USUARIO, String senhA_USUARIO, String statuS_USUARIO, Date datA_INICIO_ASSINATURA_USUARIO, Date datA_VALIDADE_ASSINATURA_USUARIO) {
            this.nomE_USUARIO = nomE_USUARIO;
            this.emaiL_USUARIO = emaiL_USUARIO;
            this.datA_NASC_USUARIO = datA_NASC_USUARIO;
            this.cpF_USUARIO = cpF_USUARIO;
            this.cnpJ_USUARIO = cnpJ_USUARIO;
            this.iD_ASSINATURA_FK = iD_ASSINATURA_FK;
            this.fotO_USUARIO = fotO_USUARIO;
            this.senhA_USUARIO = senhA_USUARIO;
            this.statuS_USUARIO = statuS_USUARIO;
            this.datA_INICIO_ASSINATURA_USUARIO = datA_INICIO_ASSINATURA_USUARIO;
            this.datA_VALIDADE_ASSINATURA_USUARIO = datA_VALIDADE_ASSINATURA_USUARIO;
        }

        public String getNomE_USUARIO() {
            return nomE_USUARIO;
        }

        public void setNomE_USUARIO(String nomE_USUARIO) {
            this.nomE_USUARIO = nomE_USUARIO;
        }

        public String getEmaiL_USUARIO() {
            return emaiL_USUARIO;
        }

        public void setEmaiL_USUARIO(String emaiL_USUARIO) {
            this.emaiL_USUARIO = emaiL_USUARIO;
        }

        public Date getDatA_NASC_USUARIO() {
            return datA_NASC_USUARIO;
        }

        public void setDatA_NASC_USUARIO(Date datA_NASC_USUARIO) {
            this.datA_NASC_USUARIO = datA_NASC_USUARIO;
        }

        public String getCpF_USUARIO() {
            return cpF_USUARIO;
        }

        public void setCpF_USUARIO(String cpF_USUARIO) {
            this.cpF_USUARIO = cpF_USUARIO;
        }

        public String getCnpJ_USUARIO() {
            return cnpJ_USUARIO;
        }

        public void setCnpJ_USUARIO(String cnpJ_USUARIO) {
            this.cnpJ_USUARIO = cnpJ_USUARIO;
        }

        public int getiD_ASSINATURA_FK() {
            return iD_ASSINATURA_FK;
        }

        public void setiD_ASSINATURA_FK(int iD_ASSINATURA_FK) {
            this.iD_ASSINATURA_FK = iD_ASSINATURA_FK;
        }

        public String getFotO_USUARIO() {
            return fotO_USUARIO;
        }

        public void setFotO_USUARIO(String fotO_USUARIO) {
            this.fotO_USUARIO = fotO_USUARIO;
        }

        public String getSenhA_USUARIO() {
            return senhA_USUARIO;
        }

        public void setSenhA_USUARIO(String senhA_USUARIO) {
            this.senhA_USUARIO = senhA_USUARIO;
        }

        public String getStatuS_USUARIO() {
            return statuS_USUARIO;
        }

        public void setStatuS_USUARIO(String statuS_USUARIO) {
            this.statuS_USUARIO = statuS_USUARIO;
        }

        public Date getDatA_INICIO_ASSINATURA_USUARIO() {
            return datA_INICIO_ASSINATURA_USUARIO;
        }

        public void setDatA_INICIO_ASSINATURA_USUARIO(Date datA_INICIO_ASSINATURA_USUARIO) {
            this.datA_INICIO_ASSINATURA_USUARIO = datA_INICIO_ASSINATURA_USUARIO;
        }

        public Date getDatA_VALIDADE_ASSINATURA_USUARIO() {
            return datA_VALIDADE_ASSINATURA_USUARIO;
        }

        public void setDatA_VALIDADE_ASSINATURA_USUARIO(Date datA_VALIDADE_ASSINATURA_USUARIO) {
            this.datA_VALIDADE_ASSINATURA_USUARIO = datA_VALIDADE_ASSINATURA_USUARIO;
        }
    }

}

    /*Usuario post
{
        "nomE_USUARIO": "string",
        "emaiL_USUARIO": "user@example.com",
        "datA_NASC_USUARIO": "2025-05-27T19:31:27.483Z",
        "cpF_USUARIO": "string",
        "cnpJ_USUARIO": "string",
        "iD_ASSINATURA_FK": 0,
        "fotO_USUARIO": "string",
        "senhA_USUARIO": "string",
        "statuS_USUARIO": "string"
        }

        Usuario Get
        [
        {
        "iD_USUARIO": 0,
        "nomE_USUARIO": "string",
        "emaiL_USUARIO": "user@example.com",
        "datA_NASC_USUARIO": "2025-05-27T19:31:52.530Z",
        "cpF_USUARIO": "string",
        "cnpJ_USUARIO": "string",
        "iD_ASSINATURA_FK": 0,
        "fotO_USUARIO": "string",
        "senhA_USUARIO": "stringst",
        "statuS_USUARIO": "s",
        "datA_INICIO_ASSINATURA_USUARIO": "2025-05-27T19:31:52.530Z",
        "datA_VALIDADE_ASSINATURA_USUARIO": "2025-05-27T19:31:52.530Z",
        "assinatura": null
        }
        }
        ]

        Produto Post
        {
        "iD_USUARIO": 0,
        "coD_PRODUTO": "string",
        "nomE_PRODUTO": "string",
        "valoR_PRODUTO": 0,
        "tipO_PRODUTO": "string",
        "imG_PRODUTO": "string"
        }

        Produto Get
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

