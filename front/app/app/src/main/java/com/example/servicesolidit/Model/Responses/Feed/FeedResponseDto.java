package com.example.servicesolidit.Model.Responses.Feed;

import com.example.servicesolidit.Model.Responses.ResponseDto;

import java.util.ArrayList;

public class FeedResponseDto extends ResponseDto {
    private ArrayList<ProviderResponseDto> response;
    public FeedResponseDto(int code, boolean success, ArrayList<ProviderResponseDto> response){
        this.response = response;
    }
    public ArrayList<ProviderResponseDto> getResponse(){
        return this.response;
    }

    public String getResponseMessage(){
        return this.response.toString();
    }

    public int getCode(){
        return this.code;
    }
}
