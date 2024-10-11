package com.example.farmtech_mobile.data.model;

import java.util.Date;
import java.util.List;

public class Producao {
    private int id;
    private Date dataProd;
    private List<Estoque> estoques;
    public Producao(Date dataProd, List<Estoque> estoques) {
        this.dataProd = dataProd;
        this.estoques = estoques;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Date getDataProd() {
        return dataProd;
    }

    public void setDataProd(Date dataProd) {
        this.dataProd = dataProd;
    }

    public List<Estoque> getEstoques() {
        return estoques;
    }

    public void setEstoques(List<Estoque> estoques) {
        this.estoques = estoques;
    }
}
