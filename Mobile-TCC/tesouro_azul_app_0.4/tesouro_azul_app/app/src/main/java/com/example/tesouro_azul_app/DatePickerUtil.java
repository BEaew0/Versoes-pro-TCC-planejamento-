package com.example.tesouro_azul_app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class DatePickerUtil
{
    public static void showDatePicker(final Context context, final EditText editText, boolean limitarHoje) {
        final Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
                        String data = String.format("%02d/%02d/%04d", diaSelecionado, mesSelecionado + 1, anoSelecionado);
                        editText.setText(data);
                    }
                },
                ano, mes, dia
        );

        if (limitarHoje) {
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

        datePickerDialog.show();
    }
}
