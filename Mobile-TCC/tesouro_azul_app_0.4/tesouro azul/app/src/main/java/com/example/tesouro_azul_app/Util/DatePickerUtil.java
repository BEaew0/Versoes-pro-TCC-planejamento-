package com.example.tesouro_azul_app.Util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.tesouro_azul_app.Service.AuthInterceptor;

import java.util.Calendar;

public class DatePickerUtil
{
    public static void showDatePicker(final Context context, final EditText editText, boolean limitarHoje) {

        //Cria uma instância de Calendar com a data/hora atual
        final Calendar calendar = Calendar.getInstance();

        //Extrai ano, mês e dia para usar como valores iniciais no DatePicker
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        //Configura um listener que é chamado quando o usuário seleciona uma data
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
                {
                    //Preenche o EditText com a data formatada
                    @Override
                    public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado)
                    {
                        //Formata a data no padrão "dd/MM/yyyy" (adicionando 1 ao mês pois Calendar.MONTH começa em 0)
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
