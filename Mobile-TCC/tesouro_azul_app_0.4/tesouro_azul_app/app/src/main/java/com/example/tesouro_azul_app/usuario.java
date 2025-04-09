package com.example.tesouro_azul_app;

public class usuario {
    private String CPF_CNPJreg;
    private String senhaReg;
    private String senhaConfirm;
    private String nascimento;
    private String nomeReg;
    private String email;

    //Método construtor
    public usuario(
                   String CPF_CNPJreg, String senhaReg,
                   String senhaConfirm, String nascimento,
                   String nomeReg, String email) {

        this.CPF_CNPJreg = CPF_CNPJreg;
        this.senhaReg = senhaReg;
        this.senhaConfirm = senhaConfirm;
        this.nascimento = nascimento;
        this.nomeReg = nomeReg;
        this.email = email;
    }

    public String getCPF_CNPJreg() {
        return CPF_CNPJreg;
    }

    public void setCPF_CNPJreg(String CPF_CNPJreg) {
        this.CPF_CNPJreg = CPF_CNPJreg;
    }

    public String getSenhaReg() {
        return senhaReg;
    }

    public void setSenhaReg(String senhaReg) {
        this.senhaReg = senhaReg;
    }

    public String getSenhaConfirm() {
        return senhaConfirm;
    }

    public void setSenhaConfirm(String senhaConfirm) {
        this.senhaConfirm = senhaConfirm;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getNomeReg() {
        return nomeReg;
    }

    public void setNomeReg(String nomeReg) {
        this.nomeReg = nomeReg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
