package com.example.farmtech_mobile.data.model;

import java.math.BigDecimal;

public class VendaProdutos {
    private int ven_id;
    private int pdt_id;
    private BigDecimal quant;
    public VendaProdutos(int ven_id, int pdt_id, BigDecimal quant) {
        this.ven_id  = ven_id;
        this.pdt_id = pdt_id;
        this.quant = quant;
    }

    public int getVen_id() {
        return ven_id;
    }

    public void setVen_id(int Ven_id) {
        this.ven_id = Ven_id;
    }

    public int getPdt_id() {
        return pdt_id;
    }

    public void setPdt_id(int pdt_id) {
        this.pdt_id = pdt_id;
    }

    public BigDecimal getQuant() {
        return quant;
    }

    public void setQuant(BigDecimal quant) {
        this.quant = quant;
    }
}
