package com.example.farmtech_mobile.data.model;

public class Estoque {
    private int pdtId;
    private double quantidade;
    public Estoque(int pdtId, double quantidade) {
        this.pdtId = pdtId;
        this.quantidade = quantidade;
    }
    public int getPdtId() {
        return pdtId;
    }

    public void setPdtId(int pdtId) {
        this.pdtId = pdtId;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }
}
