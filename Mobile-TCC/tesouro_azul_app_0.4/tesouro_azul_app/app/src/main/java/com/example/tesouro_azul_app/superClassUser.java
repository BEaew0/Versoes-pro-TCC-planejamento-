package com.example.tesouro_azul_app;

public class SuperClassUser {

    // CriarUsuarioDto.java
    public static class CriarUsuarioDto {
        private String NOME_USUARIO;
        private String EMAIL_USUARIO;
        private String DATA_NASC_USUARIO;
        private String CPF_USUARIO;
        private String CNPJ_USUARIO;
        private int ID_ASSINATURA_FK;
        private String FOTO_USUARIO;
        private String SENHA_USUARIO;
        private String STATUS_USUARIO;

        // Construtores, getters e setters
        public CriarUsuarioDto(String nome, String email, String dataNasc, String cpf,
                               String cnpj, int idAssinatura, String foto, String senha, String status) {
            this.NOME_USUARIO = nome;
            this.EMAIL_USUARIO = email;
            this.DATA_NASC_USUARIO = dataNasc;
            this.CPF_USUARIO = cpf;
            this.CNPJ_USUARIO = cnpj;
            this.ID_ASSINATURA_FK = idAssinatura;
            this.FOTO_USUARIO = foto;
            this.SENHA_USUARIO = senha;
            this.STATUS_USUARIO = status;
        }

        // Getters e Setters...

        public String getNOME_USUARIO() {
            return NOME_USUARIO;
        }

        public void setNOME_USUARIO(String NOME_USUARIO) {
            this.NOME_USUARIO = NOME_USUARIO;
        }

        public String getEMAIL_USUARIO() {
            return EMAIL_USUARIO;
        }

        public void setEMAIL_USUARIO(String EMAIL_USUARIO) {
            this.EMAIL_USUARIO = EMAIL_USUARIO;
        }

        public String getDATA_NASC_USUARIO() {
            return DATA_NASC_USUARIO;
        }

        public void setDATA_NASC_USUARIO(String DATA_NASC_USUARIO) {
            this.DATA_NASC_USUARIO = DATA_NASC_USUARIO;
        }

        public String getCPF_USUARIO() {
            return CPF_USUARIO;
        }

        public void setCPF_USUARIO(String CPF_USUARIO) {
            this.CPF_USUARIO = CPF_USUARIO;
        }

        public String getCNPJ_USUARIO() {
            return CNPJ_USUARIO;
        }

        public void setCNPJ_USUARIO(String CNPJ_USUARIO) {
            this.CNPJ_USUARIO = CNPJ_USUARIO;
        }

        public int getID_ASSINATURA_FK() {
            return ID_ASSINATURA_FK;
        }

        public void setID_ASSINATURA_FK(int ID_ASSINATURA_FK) {
            this.ID_ASSINATURA_FK = ID_ASSINATURA_FK;
        }

        public String getFOTO_USUARIO() {
            return FOTO_USUARIO;
        }

        public void setFOTO_USUARIO(String FOTO_USUARIO) {
            this.FOTO_USUARIO = FOTO_USUARIO;
        }

        public String getSENHA_USUARIO() {
            return SENHA_USUARIO;
        }

        public void setSENHA_USUARIO(String SENHA_USUARIO) {
            this.SENHA_USUARIO = SENHA_USUARIO;
        }

        public String getSTATUS_USUARIO() {
            return STATUS_USUARIO;
        }

        public void setSTATUS_USUARIO(String STATUS_USUARIO) {
            this.STATUS_USUARIO = STATUS_USUARIO;
        }
    }

    // AtualizarCampoUsuarioDto.java
    public class AtualizarCampoUsuarioDto {
        private String Campo;
        private String NovoValor;

        public AtualizarCampoUsuarioDto(String campo, String novoValor) {
            this.Campo = campo;
            this.NovoValor = novoValor;
        }

        // Getters e Setters...

        public String getCampo() {
            return Campo;
        }

        public void setCampo(String campo) {
            Campo = campo;
        }

        public String getNovoValor() {
            return NovoValor;
        }

        public void setNovoValor(String novoValor) {
            NovoValor = novoValor;
        }
    }

    // ImagemDto.java
    public class ImagemDto {
        private String ImagemBase64;

        public ImagemDto(String imagemBase64) {
            this.ImagemBase64 = imagemBase64;
        }

        // Getters e Setters...

        public String getImagemBase64() {
            return ImagemBase64;
        }

        public void setImagemBase64(String imagemBase64) {
            ImagemBase64 = imagemBase64;
        }
    }

    // Usuario.java (modelo de retorno)
    public class Usuario {
        private int ID_USUARIO;
        private String NOME_USUARIO;
        private String EMAIL_USUARIO;
        private String DATA_NASC_USUARIO;
        private String CPF_USUARIO;
        private String CNPJ_USUARIO;
        private int ID_ASSINATURA_FK;
        private byte[] FOTO_USUARIO;
        private String SENHA_USUARIO;
        private String STATUS_USUARIO;

        // Getters e Setters...

        public int getID_USUARIO() {
            return ID_USUARIO;
        }

        public void setID_USUARIO(int ID_USUARIO) {
            this.ID_USUARIO = ID_USUARIO;
        }

        public String getNOME_USUARIO() {
            return NOME_USUARIO;
        }

        public void setNOME_USUARIO(String NOME_USUARIO) {
            this.NOME_USUARIO = NOME_USUARIO;
        }

        public String getEMAIL_USUARIO() {
            return EMAIL_USUARIO;
        }

        public void setEMAIL_USUARIO(String EMAIL_USUARIO) {
            this.EMAIL_USUARIO = EMAIL_USUARIO;
        }

        public String getDATA_NASC_USUARIO() {
            return DATA_NASC_USUARIO;
        }

        public void setDATA_NASC_USUARIO(String DATA_NASC_USUARIO) {
            this.DATA_NASC_USUARIO = DATA_NASC_USUARIO;
        }

        public String getCPF_USUARIO() {
            return CPF_USUARIO;
        }

        public void setCPF_USUARIO(String CPF_USUARIO) {
            this.CPF_USUARIO = CPF_USUARIO;
        }

        public String getCNPJ_USUARIO() {
            return CNPJ_USUARIO;
        }

        public void setCNPJ_USUARIO(String CNPJ_USUARIO) {
            this.CNPJ_USUARIO = CNPJ_USUARIO;
        }

        public int getID_ASSINATURA_FK() {
            return ID_ASSINATURA_FK;
        }

        public void setID_ASSINATURA_FK(int ID_ASSINATURA_FK) {
            this.ID_ASSINATURA_FK = ID_ASSINATURA_FK;
        }

        public byte[] getFOTO_USUARIO() {
            return FOTO_USUARIO;
        }

        public void setFOTO_USUARIO(byte[] FOTO_USUARIO) {
            this.FOTO_USUARIO = FOTO_USUARIO;
        }

        public String getSENHA_USUARIO() {
            return SENHA_USUARIO;
        }

        public void setSENHA_USUARIO(String SENHA_USUARIO) {
            this.SENHA_USUARIO = SENHA_USUARIO;
        }

        public String getSTATUS_USUARIO() {
            return STATUS_USUARIO;
        }

        public void setSTATUS_USUARIO(String STATUS_USUARIO) {
            this.STATUS_USUARIO = STATUS_USUARIO;
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

