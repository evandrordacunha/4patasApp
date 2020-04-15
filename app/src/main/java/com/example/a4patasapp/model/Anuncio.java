package com.example.a4patasapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Anuncio implements Parcelable {

    private String codAnuncio;
    private String dataCriacao;
    private String titulo;
    private String descricao;
    private String telefone;
    private String email;
    private String sexo;
    private String tipoAnimal;
    private String porte;
    private String idade;
    private String latitude;
    private String longitude;
    private String imagem;
    private String endAprox;
    private String cidade;
    private String estado;

    public Anuncio(String codAnuncio, String dataCriacao, String titulo, String descricao, String telefone, String email, String sexo, String tipoAnimal, String porte, String idade, String latitude, String longitude, String imagem, String endAprox, String cidade, String estado) {
        this.codAnuncio = codAnuncio;
        this.dataCriacao = dataCriacao;
        this.titulo = titulo;
        this.descricao = descricao;
        this.telefone = telefone;
        this.email = email;
        this.sexo = sexo;
        this.tipoAnimal = tipoAnimal;
        this.porte = porte;
        this.idade = idade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagem = imagem;
        this.endAprox = endAprox;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Anuncio(){}

    protected Anuncio(Parcel in) {
        codAnuncio = in.readString();
        dataCriacao = in.readString();
        titulo = in.readString();
        descricao = in.readString();
        telefone = in.readString();
        email = in.readString();
        sexo = in.readString();
        tipoAnimal = in.readString();
        porte = in.readString();
        idade = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        imagem = in.readString();
        endAprox = in.readString();
        cidade = in.readString();
        estado = in.readString();
    }

    public static final Creator<Anuncio> CREATOR = new Creator<Anuncio>() {
        @Override
        public Anuncio createFromParcel(Parcel in) {
            return new Anuncio(in);
        }

        @Override
        public Anuncio[] newArray(int size) {
            return new Anuncio[size];
        }
    };

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEndAprox() {
        return endAprox;
    }

    public void setEndAprox(String endAprox) {
        this.endAprox = endAprox;
    }

    public String getCodAnuncio() {
        return codAnuncio;
    }

    public void setCodAnuncio(String codAnuncio) {
        this.codAnuncio = codAnuncio;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(String tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codAnuncio);
        dest.writeString(dataCriacao);
        dest.writeString(titulo);
        dest.writeString(descricao);
        dest.writeString(telefone);
        dest.writeString(email);
        dest.writeString(sexo);
        dest.writeString(tipoAnimal);
        dest.writeString(porte);
        dest.writeString(idade);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(imagem);
        dest.writeString(endAprox);
        dest.writeString(cidade);
        dest.writeString(estado);
    }
}



