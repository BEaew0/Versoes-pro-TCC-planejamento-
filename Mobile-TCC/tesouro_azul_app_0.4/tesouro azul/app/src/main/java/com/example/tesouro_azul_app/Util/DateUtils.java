package com.example.tesouro_azul_app.Util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;


public class DateUtils {

    private static final String TAG = "DateUtils";
    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private DateUtils() {}

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

    public static java.util.Date parseDataNascimento(String dataStr, String formato) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.getDefault());
            return sdf.parse(dataStr); // Retorna java.util.Date
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }




    /**
     * Obtém a data/hora atual no formato ISO 8601 (UTC)
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDateTimeISO8601() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_FORMAT, Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.format(new java.util.Date());
        } catch (Exception e) {
            Log.e(TAG, "Erro ao formatar data atual", e);
            return "";
        }
    }

    /**
     * Converte java.util.Date para java.sql.Date
     */
    public static Date utilDateToSqlDate(java.util.Date utilDate) {
        return utilDate != null ? new Date(utilDate.getTime()) : null;
    }

    /**
     * Converte java.sql.Date para java.util.Date
     */
    public static java.util.Date sqlDateToUtilDate(Date sqlDate) {
        return sqlDate != null ? new java.util.Date(sqlDate.getTime()) : null;
    }

    /**
     * Converte string ISO8601 para java.sql.Date
     */
    public static Date iso8601ToSqlDate(String isoDateString) {
        java.util.Date utilDate = iso8601ToUtilDate(isoDateString);
        return utilDate != null ? new Date(utilDate.getTime()) : null;
    }

    /**
     * Converte string ISO8601 para java.util.Date
     */
    public static java.util.Date iso8601ToUtilDate(String isoDateString) {
        if (isoDateString == null || isoDateString.isEmpty()) return null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_FORMAT, Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.parse(isoDateString);
        } catch (ParseException e) {
            Log.e(TAG, "Erro ao converter ISO8601 para Date", e);
            return null;
        }
    }

    /**
     * Converte java.sql.Date para string ISO8601
     */
    public static String sqlDateToISO8601(Date sqlDate) {
        return sqlDate != null ? dateToISO8601(new java.util.Date(sqlDate.getTime())) : "";
    }

    /**
     * Converte java.util.Date para string ISO8601
     */
    public static String dateToISO8601(java.util.Date date) {
        if (date == null) return "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_FORMAT, Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.format(date);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao converter Date para ISO8601", e);
            return "";
        }
    }
}


