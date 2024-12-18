package com.recife.observatorio_economico.model;

public class IndicadorEconomico {
    private String nome;     // Ex: "PIB", "Taxa de Desemprego"
    private double valor;    // Ex: 1200.50, 10.5 (em porcentagem)
    private String data;     // Ex: "2024-12-18"
    private String fonte;    // Ex: "Instituto Brasileiro de Geografia e Estatística (IBGE)"

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }
}
