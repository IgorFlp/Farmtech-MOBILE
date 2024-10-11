package com.example.farmtech_mobile.data.model;

public class Produto {
    private int id;
    private String nome;
    private String unMedida;
    private double precoUn;

    public Produto(String nome, String unMedida, double precoUn){
        this.nome = nome;
        this.unMedida = unMedida;
        this.precoUn = precoUn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnMedida() {
        return unMedida;
    }

    public void setUnMedida(String unMedida) {
        this.unMedida = unMedida;
    }

    public double getPrecoUn() {
        return precoUn;
    }

    public void setPrecoUn(double precoUn) {
        this.precoUn = precoUn;
    }
}
