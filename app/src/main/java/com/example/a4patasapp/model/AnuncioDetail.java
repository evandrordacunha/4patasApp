package com.example.a4patasapp.model;

import java.util.List;

public class AnuncioDetail {

    private final Anuncio anuncio;
    private final List<Anuncio> anunciosSimilares;

    public AnuncioDetail(Anuncio anuncio, List<Anuncio> anunciosSimilares) {
        this.anuncio = anuncio;
        this.anunciosSimilares = anunciosSimilares;
    }

    public Anuncio getMovie() {
        return anuncio;
    }

    public List<Anuncio> getMoviesSimilar() {
        return anunciosSimilares;
    }
}

