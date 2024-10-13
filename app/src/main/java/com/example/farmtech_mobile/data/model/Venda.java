package com.example.farmtech_mobile.data.model;

import java.math.BigDecimal;

public class Venda {
    private int id;
    private BigDecimal subtotal;
    private BigDecimal frete;
    private BigDecimal desconto;
    private BigDecimal total;
    private String cupom;
    private String mtdPagto;
    private String cl_cpf;
    private String dtVenda;
    private String entrega;
    private int user_id;

    public Venda(BigDecimal subtotal, BigDecimal frete, BigDecimal desconto, BigDecimal total, String cupom, String mtdPagto, String cl_cpf, String dtVenda, String entrega, int user_id) {
        this.subtotal = subtotal;
        this.frete = frete;
        this.desconto = desconto;
        this.total = total;
        this.cupom = cupom;
        this.mtdPagto = mtdPagto;
        this.cl_cpf = cl_cpf;
        this.dtVenda = dtVenda;
        this.entrega = entrega;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getFrete() {
        return frete;
    }

    public void setFrete(BigDecimal frete) {
        this.frete = frete;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCupom() {
        return cupom;
    }

    public void setCupom(String cupom) {
        this.cupom = cupom;
    }

    public String getMtdPagto() {
        return mtdPagto;
    }

    public void setMtdPagto(String mtdPagto) {
        this.mtdPagto = mtdPagto;
    }

    public String getCl_cpf() {
        return cl_cpf;
    }

    public void setCl_cpf(String cl_cpf) {
        this.cl_cpf = cl_cpf;
    }

    public String getDtVenda() {
        return dtVenda;
    }

    public void setDtVenda(String dtVenda) {
        this.dtVenda = dtVenda;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}





