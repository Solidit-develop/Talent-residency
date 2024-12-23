package com.example.servicesolidit.Utils.Dtos.Responses.User;

import com.example.servicesolidit.Utils.Dtos.Responses.ResponseDto;

public class UpdateUserToProviderResponseDto extends ResponseDto {

    private int code;
    private boolean succes;
    private String response;

    public UpdateUserToProviderResponseDto(int code, boolean success, String response){
        super();
        this.code = code;
        this.success = success;
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSucces() {
        return succes;
    }

    public void setSucces(boolean succes) {
        this.succes = succes;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
