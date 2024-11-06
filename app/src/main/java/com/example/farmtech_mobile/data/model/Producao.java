package com.example.farmtech_mobile.data.model;

import java.util.Date;
import java.util.List;

public class Producao {
    private int id;
    private Date dataProd;

    public Producao(Date dataProd) {
        this.dataProd = dataProd;
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
}
