package com.example.dieta.controller;

import android.content.Context;

import com.example.dieta.modelo.Entrada;
import com.example.dieta.repository.EntradaRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Vector;

public class EntradaController {

    private Context contexto;

    public EntradaController(Context contexto){

        this.contexto = contexto;
    }

    //BUSCANDO ENTRADAS A PARTIR DAS DATAS

    public Vector<Entrada> buscaEntradasPorData(LocalDateTime data1, LocalDateTime data2) throws Exception {

        if(data1 != null && data2 != null){

            long dataLong1 = data1.toInstant(ZoneOffset.UTC).toEpochMilli();
            long dataLong2 = data2.toInstant(ZoneOffset.UTC).toEpochMilli();

            if(dataLong1 < dataLong2){
                EntradaRepository repos = new EntradaRepository(contexto);
                return repos.selectEventosPorDatas(dataLong1, dataLong2);
            }else{
                throw new Exception("A data 1 precisa ser menor que a data 2");
            }

        }else{
            throw  new Exception("As datas não podem ser nulas");
        }
    }

    //INSERIR ENTRADAS A PARTIR DE UMA NOVA ENTRADA

    public boolean insertEntrada(Entrada novaEntrada){

        if(novaEntrada != null){
            EntradaRepository repository = new EntradaRepository(contexto);
            return repository.insertEvento(novaEntrada);
        }
        return false;
    }

    public boolean insertEntrada(String alimento, double valorKcal, LocalDateTime dataI,
                                 LocalDateTime dataF, String classe){

        if(alimento != null && !alimento.isEmpty() && valorKcal > 0 && dataI != null && classe != null && !classe.isEmpty()){
            Entrada novaEntrada = new Entrada(alimento, valorKcal, dataI, dataF, classe);
            return insertEntrada(novaEntrada);

        }else{
            return false;
        }
    }
}