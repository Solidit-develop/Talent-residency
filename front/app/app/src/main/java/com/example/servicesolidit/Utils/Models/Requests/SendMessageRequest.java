package com.example.servicesolidit.Utils.Models.Requests;

import com.google.gson.annotations.SerializedName;

public class SendMessageRequest {
    @SerializedName("contect")
    private String contentMessage;

    @SerializedName("sendDate")
    private String sendDate;

    @SerializedName("createdate")
    private String createDate;

    public SendMessageRequest(){
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
