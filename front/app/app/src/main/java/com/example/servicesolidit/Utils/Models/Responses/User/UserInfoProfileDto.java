package com.example.servicesolidit.Utils.Models.Responses.User;

import com.example.servicesolidit.Utils.Models.Responses.Feed.AddressResponseDto;
import com.google.gson.annotations.SerializedName;

public class UserInfoProfileDto extends UserInfoDto {
    @SerializedName("adress")
    private AddressResponseDto address;

    @SerializedName("userType")
    private UserInfoTypeResponseDto types;

    public UserInfoProfileDto() {}

    public UserInfoTypeResponseDto getTypes() {
        return types;
    }

    public AddressResponseDto getIdAddress() {
        return address;
    }

    public void setIdAddress(AddressResponseDto address) {
        this.address = address;
    }
}
