package com.example.dieta.banco_dados;

//CLASSE DESENVOLVIDA EM SALA PARA ESQUEMATIZAR BANCO DE DADOS

public class EsquemaBanco {

    public static final String NOME_BANCO_DADOS = "gordurinhas";
    public static final String SQL_CREATE_TABLES = Entrada.SQL_CREATE;
    public static final String SQL_DROP_TABLES = Entrada.SQL_DROP;
    public static final int VERSAO = 1;

    public static class Entrada{
        public static final String NOME_TABELA = "entrada";
        public static final String NOME_COLUNA_ID = "id";
        public static final String NOME_COLUNA_NOME_ALIMENTO = "alimento";
        public static final String NOME_COLUNA_DATA_INICIAL = "datai";
        public static final String NOME_COLUNA_DATA_FINAL = "dataf";
        public static final String NOME_COLUNA_VALOR_KCAL = "valor_kcal";
        public static final String NOME_COLUNA_CLASSIFICACAO = "classificao";

        public static final String COLUNAS[] = {NOME_COLUNA_ID, NOME_COLUNA_NOME_ALIMENTO,
                                                NOME_COLUNA_VALOR_KCAL, NOME_COLUNA_DATA_INICIAL,
                                                NOME_COLUNA_DATA_FINAL, NOME_COLUNA_CLASSIFICACAO};

        public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " +
                "entrada(" +
                NOME_COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NOME_COLUNA_NOME_ALIMENTO + " TEXT," +
                NOME_COLUNA_DATA_INICIAL + " INTEGER,"+
                NOME_COLUNA_DATA_FINAL + " INTEGER,"+
                NOME_COLUNA_VALOR_KCAL + " REAL,"+
                NOME_COLUNA_CLASSIFICACAO + " TEXT"+
                ");";

        public static final String SQL_DROP = " DROP TABLE IF EXISTS entrada ";

    }
}
