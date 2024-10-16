package com.example.farmtech_mobile.data.model;

public class Usuario {
    private int id;
    private String login;
    private String senha;
    private String cargo;
    private String nome;

    public Usuario(int id, String login, String senha,String cargo, String nome){
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.cargo = cargo;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
