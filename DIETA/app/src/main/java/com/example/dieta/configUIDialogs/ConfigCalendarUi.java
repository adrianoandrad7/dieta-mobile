package com.example.dieta.configUIDialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConfigCalendarUi {

    public static void configuraCalendario(TextView campoTexto, Calendar calendario, Context cont){

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                        calendario.set(ano, mes, dia);
                        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                        campoTexto.setText(sf.format(calendario.getTime()));
                    }

        };
        new DatePickerDialog(cont,listener,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

    //CONFIGURANDO CALENDARIO PARA CADASTRO
    public static void configuraCalendarioCadastro(TextView data1Txt, Calendar instance, Context context) {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                instance.set(Calendar.YEAR, year);
                instance.set(Calendar.MONTH, month);
                instance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                data1Txt.setText(simpleDateFormat.format(instance.getTime()));

            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateSetListener, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}
