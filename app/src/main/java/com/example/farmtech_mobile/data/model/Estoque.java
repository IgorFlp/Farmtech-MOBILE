package com.example.farmtech_mobile.data.model;

import java.math.BigDecimal;

public class Estoque {
    private int pdtId;
    private BigDecimal quantidade;
    public Estoque(int pdtId, BigDecimal quantidade) {
        this.pdtId = pdtId;
        this.quantidade = quantidade;
    }
    public int getPdtId() {
        return pdtId;
    }

    public void setPdtId(int pdtId) {
        this.pdtId = pdtId;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }
}
