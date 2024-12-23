package com.example.servicesolidit.Utils.Models.Responses.User;

import com.example.servicesolidit.Utils.Models.Responses.ResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.ResponseNeastedDto;
import com.google.gson.annotations.SerializedName;

public class UserInfoProfileResponseDto extends ResponseDto {
    private ResponseNeastedDto response;

    public UserInfoProfileResponseDto(int code, boolean success, ResponseNeastedDto response){
        super();
        this.code = code;
        this.success = success;
        this.response = response;
    }

    public ResponseNeastedDto getResponse() {
        return response;
    }

    public void setResponse(ResponseNeastedDto response) {
        this.response = response;
    }

    @SerializedName("idUsedOn")
    private String idUsedOn;

    public String getIdUsedOn() {
        return idUsedOn;
    }

    public void setIdUsedOn(String idUsedOn) {
        this.idUsedOn = idUsedOn;
    }
}
