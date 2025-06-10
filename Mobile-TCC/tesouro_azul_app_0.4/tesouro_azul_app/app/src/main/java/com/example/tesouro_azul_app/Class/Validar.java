package com.example.tesouro_azul_app.Class;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Validar {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    // Verifica se é CPF (11 dígitos)
    public boolean isCPF(String documento) {
        String docLimpo = documento.replaceAll("\\D", "");
        return docLimpo.length() == 11;
    }

    // Verifica se é CNPJ (14 dígitos)
    public boolean isCNPJ(String documento) {
        String docLimpo = documento.replaceAll("\\D", "");
        return docLimpo.length() == 14;
    }

    // Identifica o tipo de documento
    public String identificarTipo(String documento) {
        if (isCPF(documento)) return "CPF";
        if (isCNPJ(documento)) return "CNPJ";
        return "INVALIDO";
    }

    // Valida CPF
    public boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Cálculo do primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int digito1 = 11 - (soma % 11);
        if (digito1 >= 10) digito1 = 0;

        // Cálculo do segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        int digito2 = 11 - (soma % 11);
        if (digito2 >= 10) digito2 = 0;

        return cpf.endsWith(digito1 + "" + digito2);
    }

    // Valida CNPJ
    public boolean validarCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        // Cálculo do primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos1[i];
        }
        int digito1 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        // Cálculo do segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos2[i];
        }
        int digito2 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        return cnpj.endsWith(digito1 + "" + digito2);
    }

    // Verifica se é maior de idade
    public boolean isMaiorIdade(Date dataNascimento) {
        Calendar hoje = Calendar.getInstance();
        Calendar nascimento = Calendar.getInstance();
        nascimento.setTime(dataNascimento);

        int idade = hoje.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR);

        if (hoje.get(Calendar.DAY_OF_YEAR) < nascimento.get(Calendar.DAY_OF_YEAR)) {
            idade--;
        }

        return idade >= 18;
    }

    // Converte String para Date
    public Date parseDate(String dataString) throws ParseException {
        return DATE_FORMAT.parse(dataString);
    }
}
