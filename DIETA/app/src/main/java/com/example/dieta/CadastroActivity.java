package com.example.dieta;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dieta.configUIDialogs.ConfigCalendarUi;
import com.example.dieta.controller.EntradaController;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

public class CadastroActivity extends AppCompatActivity {

        private EditText nomeAlimentoTxt;
        private EditText valorKcalTxt;
        private TextView dataTxt;
        private Spinner classeSpin;
        private Button salvarBtn;

        private Calendar dataSelecionada;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cadastro);
            //ESCONDE BARRA DA ACTIVITY
            getSupportActionBar().hide();

            //REFERENCIA IDS
            nomeAlimentoTxt = findViewById(R.id.nomeAlimentoCadTxt);
            classeSpin = findViewById(R.id.classeCadSpinner);
            valorKcalTxt = findViewById(R.id.valorKcalTxt);
            salvarBtn = findViewById(R.id.salvarBtn);
            dataTxt = findViewById(R.id.dataCadTxt);

            configuraDataInicial();
            registrarEventos();

        }
        //CONFIGURACOES DATA
        private void configuraDataInicial(){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            dataSelecionada = Calendar.getInstance();
            dataTxt.setText(sf.format(dataSelecionada.getTime()));

        }
        //MAPEANDO REGISTRO DE EVENTOS
        private void registrarEventos(){
            dataTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConfigCalendarUi.configuraCalendarioCadastro(dataTxt, dataSelecionada, com.example.dieta.CadastroActivity.this);
                }
            });
            salvarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cadastraEntrada();
                }
            });
        }

        //CADASTRANDO ENTRADA
        private void cadastraEntrada(){
            String nome = nomeAlimentoTxt.getText().toString();
            double valor = Double.parseDouble(valorKcalTxt.getText().toString());
            LocalDateTime dataCadastro = Instant.ofEpochMilli(dataSelecionada.getTime().getTime()).atZone(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime dataFinal = dataCadastro.withDayOfMonth(dataCadastro.getMonth().length(dataCadastro.toLocalDate().isLeapYear()));
            String classe = (String)classeSpin.getSelectedItem();

            EntradaController controller = new EntradaController(com.example.dieta.CadastroActivity.this);

            if(!controller.insertEntrada(nome, valor,dataCadastro,dataFinal, classe)){
                Toast.makeText(com.example.dieta.CadastroActivity.this,"Erro ao cadastrar usuario",
                        Toast.LENGTH_SHORT).show();
            }
            setResult(RESULT_OK);
            finish();
        }
    }