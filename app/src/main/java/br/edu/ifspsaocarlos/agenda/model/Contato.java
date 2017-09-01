package br.edu.ifspsaocarlos.agenda.model;

import java.io.Serializable;

public class Contato implements Serializable{
    private static final long serialVersionUID = 1L;
    private long id;
    private String nome;
    private String apelido;
    private long msg;
    private long id_origem;

    public Contato()
    {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_origem() {
        return id_origem;
    }

    public void setId_origem(long id_origem) {
        this.id_origem = id_origem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public long getMsg() {
        return msg;
    }

    public void setMsg(long msg) {
        this.msg = msg;
    }

}

