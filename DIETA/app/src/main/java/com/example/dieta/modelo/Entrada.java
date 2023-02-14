package com.example.dieta.modelo;

import java.time.LocalDateTime;

public class Entrada {

    private long id;
    private String alimento;
    private double valorKcal;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private String classificacao;

    public Entrada(String alimento, double valorKcal, LocalDateTime dataI, LocalDateTime dataF, String classificacao) {
        this.alimento = alimento;
        this.valorKcal = valorKcal;
        this.dataInicial = dataI;
        this.dataFinal = dataF;
        this.classificacao = classificacao;
    }

    public Entrada(long id, String alimento, double valorKcal, LocalDateTime dataI, LocalDateTime dataF,String classificacao) {
        this.id = id;
        this.alimento = alimento;
        this.valorKcal = valorKcal;
        this.dataInicial = dataI;
        this.dataFinal = dataF;
        this.classificacao = classificacao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlimento() {
        return alimento;
    }

    public void setAlimento(String alimento) {
        this.alimento = alimento;
    }

    public double getValorKcal() {
        return valorKcal;
    }

    public void setValorKcal(double valorKcal) {
        this.valorKcal = valorKcal;
    }

    public LocalDateTime getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDateTime dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDateTime getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDateTime dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

}
