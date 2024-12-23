package com.example.servicesolidit.Utils.Dtos.Responses.Conversatoins;

import com.google.gson.annotations.SerializedName;

public class MessageDto {
    @SerializedName("id_messages")
    private int messageId;

    @SerializedName("contect")
    private String content;

    @SerializedName("senddate")
    private String sendDate;

    // Constructor, getters y setters
    public MessageDto(int messageId, String content, String sendDate) {
        this.messageId = messageId;
        this.content = content;
        this.sendDate = sendDate;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
