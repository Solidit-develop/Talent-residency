package com.example.servicesolidit.Utils.Models.Responses.Messages;

public class MessageDto {
    private int id_message;
    private String contect;
    private boolean isSent;// Indica si el mensaje fue enviado o recibido
    private String senddate;
    private int idDest;


    // Constructor
    public MessageDto(String contect, boolean isSent) {
        this.contect = contect;
        this.isSent = isSent;
    }


    public int getId_message() {
        return id_message;
    }

    public void setId_message(int id_message) {
        this.id_message = id_message;
    }

    public String getContect() {
        return contect;
    }

    public void setContent(String content) {
        this.contect = content;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getSenddate() {
        return senddate;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate;
    }

    public int getIdDest() {
        return idDest;
    }

    public void setIdDest(int idDest) {
        this.idDest = idDest;
    }
}
