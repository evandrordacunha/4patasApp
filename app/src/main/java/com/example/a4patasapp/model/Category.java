package com.example.a4patasapp.model;

import java.util.List;

public class Category {
    private String nome;
    private List<Anuncio> anuncios;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Anuncio> getMovies() {
        return anuncios;
    }

    public void setMovies(List<Anuncio> anuncios) {
        this.anuncios = anuncios;
    }
}
