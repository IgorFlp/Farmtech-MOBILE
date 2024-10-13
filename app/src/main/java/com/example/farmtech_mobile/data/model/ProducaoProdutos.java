package com.example.farmtech_mobile.data.model;

import java.math.BigDecimal;
import java.util.List;

public class ProducaoProdutos {
    private int pdc_id;
    private int pdt_id;
    private BigDecimal quant;

    public ProducaoProdutos(int pdc_id, int pdt_id, BigDecimal quant) {
        this.pdc_id = pdc_id;
        this.pdt_id = pdt_id;
        this.quant = quant;
    }

    public int getPdc_id() {
        return pdc_id;
    }

    public void setPdc_id(int pdc_id) {
        this.pdc_id = pdc_id;
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
