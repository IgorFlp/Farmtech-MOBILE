package com.example.farmtech_mobile.data.model;

public class ClienteEndereco{

    private String cl_cpf;

    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public ClienteEndereco(String cl_cpf, String rua,String bairro, String cidade, String estado, String cep) {
        this.cl_cpf = cl_cpf;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }
    public String getCl_cpf() { return cl_cpf; }
    public void setCl_cpf(String cl_cpf ) { this.cl_cpf = cl_cpf; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
}
