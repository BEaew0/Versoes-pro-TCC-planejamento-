package com.example.tesouro_azul_app.Util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {

    public static double parseDoubleWithComma(String numberStr) {
        try {
            // Obtém o formato numérico para o locale padrão
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());

            // Converte a string para Number
            Number number = format.parse(numberStr);

            // Retorna como double
            return number.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0; // ou lance uma exceção
        }
    }

    public static double convertToDouble(String value) {
        if (value == null || value.isEmpty()) return 0.0;

        // Substitui vírgula por ponto e remove caracteres não numéricos
        String cleanedValue = value.replace(",", ".")
                .replaceAll("[^0-9.]", "");

        try {
            return Double.parseDouble(cleanedValue);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
