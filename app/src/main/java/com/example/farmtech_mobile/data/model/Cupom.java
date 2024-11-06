package com.example.farmtech_mobile.data.model;

import java.util.Date;

public class Cupom {
    private String nome;
    private double valor;
    private Date dtValid;

    public Cupom(String nome, double valor, Date dtValid) {
        this.nome = nome;
        this.valor = valor;
        this.dtValid = dtValid;
    }
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

    public Date getDtValid() {
        return dtValid;
    }

    public void setDtValid(Date dtValid) {
        this.dtValid = dtValid;
    }
}
