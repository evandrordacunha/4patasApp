package com.example.a4patasapp.model;

import android.os.Parcel;
import android.os.Parcelable;
public class Desaparecido implements Parcelable {

    private String codAnuncio;
    private String nome;
    private String email;
    private String telefone;
    private String foto;
    private String descricao;
    private String dataCadastro;

    protected Desaparecido(Parcel in) {
        codAnuncio = in.readString();
        nome = in.readString();
        email = in.readString();
        telefone = in.readString();
        foto = in.readString();
        descricao = in.readString();
        dataCadastro = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codAnuncio);
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(telefone);
        dest.writeString(foto);
        dest.writeString(descricao);
        dest.writeString(dataCadastro);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Desaparecido> CREATOR = new Creator<Desaparecido>() {
        @Override
        public Desaparecido createFromParcel(Parcel in) {
            return new Desaparecido(in);
        }

        @Override
        public Desaparecido[] newArray(int size) {
            return new Desaparecido[size];
        }
    };

    public String getCodAnuncio() {
        return codAnuncio;
    }

    public void setCodAnuncio(String codAnuncio) {
        this.codAnuncio = codAnuncio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }



    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Desaparecido() {
    }

    public Desaparecido(String codAnuncio, String nome, String email, String telefone, String foto, String descricao, String dataCadastro) {
        this.codAnuncio = codAnuncio;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.foto = foto;
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
    }
}