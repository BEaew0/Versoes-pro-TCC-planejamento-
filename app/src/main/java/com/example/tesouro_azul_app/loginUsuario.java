package com.example.tesouro_azul_app;

public class loginUsuario {
    private String CPF_CNPJ;
    private String senha;

    public loginUsuario(String CPF_CNPJ, String senha) {
        this.CPF_CNPJ = CPF_CNPJ;
        this.senha = senha;
    }

    public String getCPF_CNPJ() {
        return CPF_CNPJ;
    }

    public void setCPF_CNPJ(String CPF_CNPJ) {
        this.CPF_CNPJ = CPF_CNPJ;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
