package com.example.a4patasapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {

    private String userID;
    private String nome;
    private String email;
    private String senha;
    private String dataCriacao;
    private String telefone;
    private String latitude;
    private String longitude;

    public Usuario(){

    }

    public Usuario(String userID, String nome, String email, String senha, String dataCriacao, String telefone) {
        this.userID = userID;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataCriacao = dataCriacao;
        this.telefone = telefone;
    }

    public Usuario(String userID, String nome, String email, String senha, String dataCriacao, String telefone, String latitude,String longitude) {
        this.userID = userID;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataCriacao = dataCriacao;
        this.telefone = telefone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Usuario(Parcel in) {
        userID = in.readString();
        nome = in.readString();
        email = in.readString();
        senha = in.readString();
        dataCriacao = in.readString();
        telefone = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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
        dest.writeString(userID);
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(senha);
        dest.writeString(dataCriacao);
        dest.writeString(telefone);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
