package com.jefmelo.autenticationforsms.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.jefmelo.autenticationforsms.config.ConfigFirebase;

public class Usuario {

    private String id;
    private String nome;
    private String telefone;
    private String senha;

    public Usuario() {
    }

    public void salvarUsuario() {
        DatabaseReference databaseReference = ConfigFirebase.getDatabaseReference();
        databaseReference
                .child("users")
                .child(getId())
                .setValue(this);
    }

    public Usuario(String id, String nome, String senha, String telefone) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.telefone = telefone;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
