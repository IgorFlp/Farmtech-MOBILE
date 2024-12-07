package com.example.farmtech_mobile.data.model;

import java.util.Date;
import java.util.List;

public class Producao {
    private int id;
    private String dataProd;

    public Producao(String dataProd) {
        this.dataProd = dataProd;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDataProd() {
        return dataProd;
    }

    public void setDataProd(String dataProd) {
        this.dataProd = dataProd;
    }
}
