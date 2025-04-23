package com.example.tesouro_azul_app;

import java.util.Date;

public class Usuario {

        public String NOME_USUARIO;
        public String EMAIL_USUARIO;
        public Date DATA_NASC_USUARIO;
        public String CPF_USUARIO;
        public String CNPJ_USUARIO;
        public int ID_ASSINATURA_FK;
        public byte FOTO_USUARIO;
        public String SENHA_USUARIO;

        public Usuario(String NOME_USUARIO, String EMAIL_USUARIO, Date DATA_NASC_USUARIO, String CPF_USUARIO, String CNPJ_USUARIO, String SENHA_USUARIO) {
            this.NOME_USUARIO = NOME_USUARIO;
            this.EMAIL_USUARIO = EMAIL_USUARIO;
            this.DATA_NASC_USUARIO = DATA_NASC_USUARIO;
            this.CPF_USUARIO = CPF_USUARIO;
            this.CNPJ_USUARIO = CNPJ_USUARIO;
            this.ID_ASSINATURA_FK = ID_ASSINATURA_FK;
            this.FOTO_USUARIO = FOTO_USUARIO;
            this.SENHA_USUARIO = SENHA_USUARIO;
        }

        public String getNOME_USUARIO() {
            return NOME_USUARIO;
        }

        public String getEMAIL_USUARIO() {
            return EMAIL_USUARIO;
        }

        public Date getDATA_NASC_USUARIO() {
            return DATA_NASC_USUARIO;
        }

        public String getCPF_USUARIO() {
            return CPF_USUARIO;
        }

        public String getCNPJ_USUARIO() {
            return CNPJ_USUARIO;
        }

        public int getID_ASSINATURA_FK() {
            return ID_ASSINATURA_FK;
        }

        public byte getFOTO_USUARIO() {
            return FOTO_USUARIO;
        }

        public String getSENHA_USUARIO() {
            return SENHA_USUARIO;
        }
    }

