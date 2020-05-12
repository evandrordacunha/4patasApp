package com.example.a4patasapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Duvida implements Parcelable {

    private String pergunta;
    private String resposta;

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Creator<Duvida> getCREATOR() {
        return CREATOR;
    }

    private String id;

    public Duvida(String pergunta, String resposta, String id) {
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.id = id;
    }

    public Duvida() {
    }

    protected Duvida(Parcel in) {
        pergunta = in.readString();
        resposta = in.readString();
        id = in.readString();
    }

    public static final Creator<Duvida> CREATOR = new Creator<Duvida>() {
        @Override
        public Duvida createFromParcel(Parcel in) {
            return new Duvida(in);
        }

        @Override
        public Duvida[] newArray(int size) {
            return new Duvida[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pergunta);
        dest.writeString(resposta);
        dest.writeString(id);
    }
}
