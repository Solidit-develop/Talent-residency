package com.example.servicesolidit.Utils.Models.Responses.Messages;

import com.google.gson.annotations.SerializedName;

public class SendMessageResponseDto {
    @SerializedName("message")
    private String response;

    public SendMessageResponseDto(){}

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
