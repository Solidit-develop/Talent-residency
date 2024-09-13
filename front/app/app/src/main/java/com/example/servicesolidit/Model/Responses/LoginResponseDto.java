package com.example.servicesolidit.Model.Responses;

public class LoginResponseDto extends ResponseDto {

    public LoginResponseDto(int code){
        super();
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}
