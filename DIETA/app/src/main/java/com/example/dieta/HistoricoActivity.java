package com.example.dieta;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dieta.configUIDialogs.ConfigCalendarUi;
import com.example.dieta.controller.EntradaController;
import com.example.dieta.modelo.Entrada;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoricoActivity extends AppCompatActivity {

    private TextView data1Txt;
    private TextView data2Txt;
    private TextView mediaFiltro;
    private TextView caloriasFiltro;
    private TextView mediaCafeManha;
    private TextView mediaDesjejum;
    private TextView mediaAlmoco;
    private TextView mediaLancheTarde;
    private TextView mediaHappyHour;
    private TextView mediaJantar;
    private TextView mediaSobremesa;
    private TextView mediaOutros;

    private ArrayList<Entrada> dataModels;
    private LocalDateTime tempoInicial;
    private LocalDateTime tempoFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        //ESCONDE BARRA DA ACTIVITY
        getSupportActionBar().hide();

        //REFERENCIA IDS
        data1Txt = findViewById(R.id.dataInicialTxt);
        data2Txt = findViewById(R.id.dataFinalTxt);
        mediaFiltro = findViewById(R.id.media_caloria_entre_datas);
        caloriasFiltro = findViewById(R.id.caloria_entre_datas);
        mediaCafeManha = findViewById(R.id.media_cafe_manha);
        mediaDesjejum = findViewById(R.id.media_desjejum);
        mediaAlmoco = findViewById(R.id.media_almoco);
        mediaLancheTarde = findViewById(R.id.media_lanche_tarde);
        mediaHappyHour = findViewById(R.id.media_happy);
        mediaJantar = findViewById(R.id.media_jantar);
        mediaSobremesa = findViewById(R.id.media_sobremesa);
        mediaOutros = findViewById(R.id.media_outros);
        dataModels= new ArrayList<>();

        configuraDatas();
        registraEventos();
        consultaEventos();
        atualizarMedia();

    }
    //MAPEANDO REGISTRO DE EVENTOS
    private void registraEventos(){
        data1Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigCalendarUi.configuraCalendario(data1Txt, Calendar.getInstance(), HistoricoActivity.this);
            }
        });

        data2Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigCalendarUi.configuraCalendario(data2Txt, Calendar.getInstance(), HistoricoActivity.this);
            }
        });

        //CODIGO DESENVOLVIDO EM SALA PARA CONF DATAS
        data1Txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    tempoInicial = Instant.ofEpochMilli(formatter.parse(data1Txt.getText().toString()).getTime()).atZone(ZoneOffset.UTC).toLocalDateTime();
                    tempoFinal = tempoInicial.withDayOfMonth(tempoInicial.getMonth().length(tempoInicial.toLocalDate().isLeapYear()));
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    data2Txt.setText(format.format(tempoFinal));

                    consultaEventos();
                    atualizarMedia();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        data2Txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    tempoFinal = Instant.ofEpochMilli(formatter.parse(data2Txt.getText().toString()).getTime()).atZone(ZoneOffset.UTC).toLocalDateTime();
                    consultaEventos();
                    atualizarMedia();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void consultaEventos(){
        dataModels.clear();
        EntradaController controller = new EntradaController(HistoricoActivity.this);
        try {
            dataModels.addAll(controller.buscaEntradasPorData(tempoInicial,tempoFinal));
        } catch (Exception e) {
            Toast.makeText(HistoricoActivity.this,"Não foi possível recuperar eventos para este intervalo", Toast.LENGTH_SHORT).show();
        }
    }

    private void configuraDatas(){
        Bundle parametros = getIntent().getExtras();
        long t1 = parametros.getLong("tempo1",-1);
        long t2 = parametros.getLong("tempo2",-1);

        if(t1 != -1 && t2 != -1){
            tempoInicial = Instant.ofEpochMilli(t1).atZone(ZoneOffset.UTC).toLocalDateTime();
            tempoFinal = Instant.ofEpochMilli(t2).atZone(ZoneOffset.UTC).toLocalDateTime();
        }else{
            tempoInicial = LocalDateTime.now().withDayOfMonth(1);
            tempoFinal = tempoInicial.withDayOfMonth(tempoInicial.getMonth().length(tempoInicial.toLocalDate().isLeapYear()));
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        data1Txt.setText(format.format(tempoInicial));
        data2Txt.setText(format.format(tempoFinal));

    }
    //ATUALIZA MEDIA TOTAL E MEDIA POR CLASSIFICACAO
    private void atualizarMedia() {
        int somaCal = 0, cafeCal = 0, desjejumCal = 0, almocoCal = 0, lancheCal = 0, happyCal = 0,
                jantarCal = 0, sobremesaCal = 0, outrosCal = 0, cont = 0 ;

        for(Entrada et : dataModels){
            somaCal +=  et.getValorKcal() ;

            if(et.getClassificacao().equals("Café da Manha")) cafeCal += et.getValorKcal();
            else if(et.getClassificacao().equals("Desjejum")) desjejumCal += et.getValorKcal();
            else if(et.getClassificacao().equals("Almoço")) almocoCal += et.getValorKcal();
            else if(et.getClassificacao().equals("Lanche da Tarde")) lancheCal += et.getValorKcal();
            else if(et.getClassificacao().equals("Happy Hour")) happyCal += et.getValorKcal();
            else if (et.getClassificacao().equals("Jantar")) jantarCal += et.getValorKcal();
            else if (et.getClassificacao().equals("Sobremesa")) sobremesaCal += et.getValorKcal();
            else if (et.getClassificacao().equals("Outros")) outrosCal += et.getValorKcal();

            cont ++;
        }
        //SET NA ACTIVIT
        caloriasFiltro.setText(String.valueOf(somaCal));
        mediaFiltro.setText(String.valueOf(somaCal/cont));
        mediaCafeManha.setText(String.valueOf(cafeCal/cont));
        mediaDesjejum.setText(String.valueOf(desjejumCal/cont));
        mediaAlmoco.setText(String.valueOf(almocoCal/cont));
        mediaLancheTarde.setText(String.valueOf(lancheCal/cont));
        mediaHappyHour.setText(String.valueOf(happyCal));
        mediaJantar.setText(String.valueOf(jantarCal));
        mediaSobremesa.setText(String.valueOf(sobremesaCal));
        mediaOutros.setText(String.valueOf(outrosCal));

    }

}
