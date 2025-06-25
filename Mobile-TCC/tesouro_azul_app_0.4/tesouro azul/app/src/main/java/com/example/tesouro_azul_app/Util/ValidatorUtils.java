package com.example.tesouro_azul_app.Util;

import java.util.Calendar;
import java.util.Date;

public class ValidatorUtils{

    public static boolean isCPF(String cpfCnpj) {
        return cpfCnpj != null && cpfCnpj.replaceAll("\\D", "").length() == 11;
    }

    public static boolean isCNPJ(String cpfCnpj) {
        return cpfCnpj != null && cpfCnpj.replaceAll("\\D", "").length() == 14;
    }

    public static String identificarTipo(String cpfCnpj) {
        if (isCPF(cpfCnpj)) return "CPF";
        if (isCNPJ(cpfCnpj)) return "CNPJ";
        return "Invalido";
    }

    public static boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        int soma = 0, peso = 10;
        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * peso--;
        int digito1 = 11 - (soma % 11);
        if (digito1 >= 10) digito1 = 0;

        soma = 0;
        peso = 11;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * peso--;
        int digito2 = 11 - (soma % 11);
        if (digito2 >= 10) digito2 = 0;

        return cpf.endsWith(digito1 + "" + digito2);
    }

    public static boolean validarCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) return false;

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma = 0;
        for (int i = 0; i < 12; i++) soma += (cnpj.charAt(i) - '0') * pesos1[i];
        int digito1 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        soma = 0;
        for (int i = 0; i < 13; i++) soma += (cnpj.charAt(i) - '0') * pesos2[i];
        int digito2 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        return cnpj.endsWith(digito1 + "" + digito2);
    }

    public static boolean maiorIdade(Date nascimento) {
        Calendar hoje = Calendar.getInstance();
        Calendar dataNasc = Calendar.getInstance();
        dataNasc.setTime(nascimento);

        int idade = hoje.get(Calendar.YEAR) - dataNasc.get(Calendar.YEAR);
        if (hoje.get(Calendar.DAY_OF_YEAR) < dataNasc.get(Calendar.DAY_OF_YEAR)) idade--;

        return idade >= 18;
    }
}
