package com.example.dieta.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dieta.banco_dados.EsquemaBanco;
import com.example.dieta.banco_dados.OperacoesBancoDados;
import com.example.dieta.modelo.Entrada;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Vector;

public class EntradaRepository {

    private Context contexto;
    public EntradaRepository(Context contexto){
        this.contexto = contexto;
    }

    //CADASTRO

    public boolean insertEvento(Entrada novaEntrada){
        try(OperacoesBancoDados conexaoBanco = new OperacoesBancoDados(this.contexto)){
            SQLiteDatabase tran = conexaoBanco.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(EsquemaBanco.Entrada.NOME_COLUNA_NOME_ALIMENTO, novaEntrada.getAlimento());
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_VALOR_KCAL, novaEntrada.getValorKcal());
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_CLASSIFICACAO, novaEntrada.getClassificacao());
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_DATA_INICIAL, novaEntrada.getDataInicial().toInstant(ZoneOffset.UTC).toEpochMilli());
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_DATA_FINAL,
                                            novaEntrada.getDataFinal() == null? -1 :
                                            novaEntrada.getDataFinal().toInstant(ZoneOffset.UTC).toEpochMilli());
            long idGeradoBD = tran.insert(EsquemaBanco.Entrada.NOME_TABELA, null, values);

            if(idGeradoBD > 0){
                novaEntrada.setId(idGeradoBD);
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //SELECT

    public Vector<Entrada> selectEventosPorDatas(long dataInicial, long dataFinal){

        Vector<Entrada> resultado = new Vector<>();
        try(OperacoesBancoDados conexaoBanco = new OperacoesBancoDados(this.contexto)){

            String sql_Busca = "(dataf != -1 And datai >= ?)";
            SQLiteDatabase tran = conexaoBanco.getReadableDatabase();

            Cursor tuplas = tran.query(EsquemaBanco.Entrada.NOME_TABELA,
                                       EsquemaBanco.Entrada.COLUNAS, sql_Busca,
                                       new String[]{dataInicial+""}, null,null, null);


            //AQUI O SELECT SERA SALVO NO VETOR

            while(tuplas.moveToNext()){
                Entrada entradaBd = new Entrada(
                        tuplas.getLong(0),
                        tuplas.getString(1),
                        tuplas.getDouble(2),
                        Instant.ofEpochMilli(tuplas.getLong(3)).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        tuplas.getLong(4) == -1? null : Instant.ofEpochMilli(tuplas.getLong(4))
                                .atZone(ZoneOffset.UTC).toLocalDateTime(),
                        tuplas.getString(5)
                );
                resultado.add(entradaBd);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

}
