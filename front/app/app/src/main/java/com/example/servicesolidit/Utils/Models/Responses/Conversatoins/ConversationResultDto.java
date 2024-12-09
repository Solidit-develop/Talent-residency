package com.example.servicesolidit.Utils.Models.Responses.Conversatoins;

public class ConversationResultDto {
    private String contenido;
    private int idMessage;
    private String date;
    private boolean isSent;
    private String related;
    private String nameUser;

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public ConversationResultDto(){}

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }
}


/*
{
            "contenido": "Holaaaaa1",
            "idMessage": 20,
            "date": "2024-11-12T03:25:44.395Z",
            "isSent": true,
            "related": 13
        }

 */