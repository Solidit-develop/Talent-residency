package com.example.servicesolidit.Utils.Dtos.Responses;

public class RegisterResponseDto extends ResponseDto {
    public String response;
    public RegisterResponseDto(int code, boolean success, String response){
        super();
        this.code = code;
        this.success = success;
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
