package com.example.tesouro_azul_app.Util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String converterParaISO(String dataBr) {
        try {
            // Formato de entrada (ex: "21/06/2025")
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Parseando a data
            LocalDate data = LocalDate.parse(dataBr, formatoEntrada);

            // Formatando para ISO (ex: "2025-06-21T00:00:00.000Z")
            return data.atStartOfDay().toString() + ".000Z";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
