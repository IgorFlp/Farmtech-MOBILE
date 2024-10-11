package com.example.farmtech_mobile.data.model;

import java.util.Date;

public class Cliente {
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String dataNasc;
    private char genero;

    public Cliente(String nome, String cpf, String email, String telefone,String dataNasc,char genero) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.dataNasc = dataNasc;
        this.genero = genero;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getDataNasc() { return dataNasc; }
    public void setDataNasc(String dataNasc) { this.dataNasc = dataNasc; }

    public char getGenero() { return genero; }
    public void setGenero(char genero) { this.genero = genero; }
}
