package com.example.servicesolidit.Utils.Models.Responses.Feed;

import com.example.servicesolidit.Utils.Models.Responses.ResponseDto;

import java.util.ArrayList;

public class FeedResponseDto extends ResponseDto {
    private ArrayList<ProviderImageFeedResponseDto> response;
    public FeedResponseDto(int code, boolean success, ArrayList<ProviderImageFeedResponseDto> response){
        this.response = response;
    }
    public ArrayList<ProviderImageFeedResponseDto> getResponse(){
        return this.response;
    }

    public String getResponseMessage(){
        return this.response.toString();
    }

    public int getCode(){
        return this.code;
    }
}
