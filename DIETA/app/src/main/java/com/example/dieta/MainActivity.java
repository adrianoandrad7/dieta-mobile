package com.example.dieta;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dieta.configuracao.SharedPreferencesAplicacao;
import com.example.dieta.controller.EntradaController;
import com.example.dieta.modelo.Entrada;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private TextView calorias_faltantes;
    private TextView valorTotalKcal;
    private Button cadastraEntradaBtn;
    private Button historicoBtn;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private LocalDateTime dataOperacao;

    private ActivityResultLauncher<Intent> callBackCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ESCONDE BARRA DA ACTIVITY
        getSupportActionBar().hide();

        calorias_faltantes = findViewById(R.id.calorias_faltantes_txt);
        cadastraEntradaBtn = findViewById(R.id.entradaBtn);
        valorTotalKcal = findViewById(R.id.caloriasTxt);
        historicoBtn = findViewById(R.id.historicoBtn);

        requisitaPermissoes();
        aplicaConfiguracoes();
        configuraDataInicial();
        registroEventos();
        atualizaDadosDia();

    }

    private void atualizaDadosDia(){
        EntradaController controller = new EntradaController(MainActivity.this);
        try {
            Vector<Entrada> entradas = controller.buscaEntradasPorData(dataOperacao.withDayOfMonth(1),
                                                                       dataOperacao.withDayOfMonth(dataOperacao.getMonth().
                                                                       length(dataOperacao.toLocalDate().isLeapYear())));

            int somaDiaria = 0;

            for(Entrada et : entradas){
                System.out.println(et.getValorKcal());
                somaDiaria +=  et.getValorKcal() ;
            }

            valorTotalKcal.setText(String.valueOf(somaDiaria));
            //ATUALIZA LIMITE CALORIAS
            int caloriasFaltantes = 2100 - somaDiaria;
            calorias_faltantes.setText(String.valueOf(caloriasFaltantes));

            if (caloriasFaltantes < 0 ){
            //INDICANDO ESTOURO NO LIMITE DE CALORIAS
                calorias_faltantes.setTextColor(Color.parseColor("#FF0000"));
                Snackbar.make(findViewById(R.id.calorias_faltantes_txt), "Você passou o limite de Calorias em: " + caloriasFaltantes * (-1),
                                Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).setTextColor(Color.RED).show();
            }

        }catch (Exception e){
            Toast.makeText(MainActivity.this,"Erro: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void requisitaPermissoes(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},0);

    }
    private void aplicaConfiguracoes(){
        SharedPreferencesAplicacao minhasConfiguracoes = new SharedPreferencesAplicacao(MainActivity.this);
        this.dataOperacao = minhasConfiguracoes.leituraData();
    }
    private void registroEventos(){
        //ORIENTACAO PASSADA EM AULA
        callBackCadastro = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        atualizaDadosDia();
                    }
                });
        cadastraEntradaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityEntrada = new Intent(MainActivity.this, CadastroActivity.class);
                callBackCadastro.launch(activityEntrada);
            }
        });
        historicoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configuraDataInicial();
                Intent activityHistorico = new Intent(MainActivity.this, HistoricoActivity.class);
                activityHistorico.putExtra("tempo1",dataInicial.toInstant(ZoneOffset.UTC).toEpochMilli());
                activityHistorico.putExtra("tempo2",dataFinal.toInstant(ZoneOffset.UTC).toEpochMilli());
                startActivity(activityHistorico);
            }
        });

    }
    private void configuraDataInicial(){
        dataInicial = dataOperacao.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        dataFinal = dataOperacao.withDayOfMonth(dataOperacao.getMonth().
                        length(dataOperacao.toLocalDate().isLeapYear())).withHour(23).
                withMinute(59).withSecond(59);
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferencesAplicacao minhasConfiguracoes = new SharedPreferencesAplicacao(MainActivity.this);
        minhasConfiguracoes.registraData(this.dataOperacao);

    }
}