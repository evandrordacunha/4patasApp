package com.example.a4patasapp.model;

import java.util.ArrayList;
import java.util.List;

public class Query {

    private String raio;
    private String sexo;
    private String idade;
    private String tipo;
    private String porte;

    public Query(String raio, String sexo, String idade, String tipo, String porte) {
        this.raio = raio;
        this.sexo = sexo;
        this.idade = idade;
        this.tipo = tipo;
        this.porte = porte;
    }

    public String getRaio() {
        return raio;
    }

    public void setRaio(String raio) {
        this.raio = raio;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    /*CASE TIPO= CACHORRO | SEXO= MACHO | PORTE= PEQUENO | IDADE= FILHOTE*/

    public List<Anuncio> getFiltro_1 (String raio, String tipo, String sexo, String porte,String idade){
        List<Anuncio> listaAnuncios = new ArrayList<>();



        return listaAnuncios;

    }


}
