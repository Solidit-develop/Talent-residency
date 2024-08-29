package com.example.servicesolidit.Model.Responses;

public class RegisterResponseDto extends ResponseDto {
    public RegisterResponseDto(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
}
