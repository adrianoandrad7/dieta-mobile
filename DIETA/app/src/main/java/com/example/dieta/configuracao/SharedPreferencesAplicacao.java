package com.example.dieta.configuracao;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class SharedPreferencesAplicacao {

    private Context contextoAtual;

    public SharedPreferencesAplicacao(Context cont){
        this.contextoAtual = cont;
    }

    public void registraData(LocalDateTime dataOperacao){
        SharedPreferences shared = this.contextoAtual.getSharedPreferences("DIETA",0);
        SharedPreferences.Editor ed = shared.edit();
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        ed.putLong("data_atual",dataOperacao.toInstant(ZoneOffset.UTC).toEpochMilli());
        ed.commit();
    }

    public LocalDateTime leituraData(){
        SharedPreferences shared = this.contextoAtual.getSharedPreferences("DIETA",0);
        long tempoMil = shared.getLong("data_atual",-1);

        if(tempoMil == -1) {
            return LocalDateTime.now(ZoneOffset.UTC);
        }else{
            Instant temp = Instant.ofEpochMilli(tempoMil);
            return LocalDateTime.ofInstant(temp, ZoneOffset.UTC);
        }

    }

}
