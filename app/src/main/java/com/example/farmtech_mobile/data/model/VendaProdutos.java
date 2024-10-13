package com.example.farmtech_mobile.data.model;

import java.math.BigDecimal;

public class VendaProdutos {
    private int vendaId;
    private int pdtId;
    private BigDecimal quant;
    public VendaProdutos(int vendaId, int pdtId, BigDecimal quant) {
        this.vendaId = vendaId;
        this.pdtId = pdtId;
        this.quant = quant;
    }

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
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
