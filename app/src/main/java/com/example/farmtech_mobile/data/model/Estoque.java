package com.example.farmtech_mobile.data.model;

import java.math.BigDecimal;

public class Estoque {
    private int pdtId;
    private BigDecimal quant;
    public Estoque(int pdtId, BigDecimal quant) {
        this.pdtId = pdtId;
        this.quant = quant;
    }
    public int getPdtId() {
        return pdtId;
    }

    public void setPdtId(int pdtId) {
        this.pdtId = pdtId;
    }

    public BigDecimal getQuant() {
        return quant;
    }

    public void setQuant(BigDecimal quant) {
        this.quant = quant;
    }
}
