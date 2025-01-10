package com.example.servicesolidit.Utils.Dtos.Requests;

import com.google.gson.annotations.SerializedName;

public class CreateCommentRequest {
    @SerializedName("calificacion")
    private String calification;
    private String comment;
    private String urlLocation;


    public String getCalification() {
        return calification;
    }

    public void setCalification(String calification) {
        this.calification = calification;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUrlLocation() {
        return urlLocation;
    }

    public void setUrlLocation(String urlLocation) {
        this.urlLocation = urlLocation;
    }
}
