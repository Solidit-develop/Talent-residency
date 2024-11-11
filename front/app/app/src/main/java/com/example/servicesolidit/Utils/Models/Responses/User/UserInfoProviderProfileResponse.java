package com.example.servicesolidit.Utils.Models.Responses.User;

import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.ResponseDto;

public class UserInfoProviderProfileResponse extends ResponseDto {
    private ProviderResponseDto response;
    public UserInfoProviderProfileResponse(int code, boolean success, ProviderResponseDto response){
        this.response = response;
    }
    public ProviderResponseDto getResponse(){
        return this.response;
    }

    public String getResponseMessage(){
        return this.response.toString();
    }

    public int getCode(){
        return this.code;
    }
}
