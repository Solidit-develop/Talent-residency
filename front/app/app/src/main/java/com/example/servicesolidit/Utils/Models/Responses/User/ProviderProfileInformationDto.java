package com.example.servicesolidit.Utils.Models.Responses.User;

import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.ResponseDto;

public class ProviderProfileInformationDto extends ResponseDto {
    private ProviderResponseDto response;

    public ProviderProfileInformationDto(int code, boolean succes, ProviderResponseDto response){
        this.code = code;
        this.success = succes;
        this.response = response;
    }

    public ProviderResponseDto getResponse() {
        return response;
    }

    public void setResponse(ProviderResponseDto response) {
        this.response = response;
    }
}
