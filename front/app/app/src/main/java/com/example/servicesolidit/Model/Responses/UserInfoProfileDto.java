package com.example.servicesolidit.Model.Responses;

import com.example.servicesolidit.Model.Responses.Feed.AddressResponseDto;
import com.google.gson.annotations.SerializedName;

public class UserInfoProfileDto extends UserInfoDto {
    @SerializedName("adress")
    private AddressResponseDto address;

    public UserInfoProfileDto() {}

    public AddressResponseDto getIdAddress() {
        return address;
    }

    public void setIdAddress(AddressResponseDto address) {
        this.address = address;
    }
}
