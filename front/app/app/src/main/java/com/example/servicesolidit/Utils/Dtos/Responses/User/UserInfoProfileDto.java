package com.example.servicesolidit.Utils.Dtos.Responses.User;

import com.example.servicesolidit.Utils.Dtos.Responses.Feed.AddressResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.ImagesInformationResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.RelationalImagesResponseDto;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserInfoProfileDto extends UserInfoDto {
    @SerializedName("adress")
    private AddressResponseDto address;

    @SerializedName("userType")
    private UserInfoTypeResponseDto types;

    @SerializedName("relacionImagen")
    private ArrayList<ImagesInformationResponseDto> imageName;

    public ArrayList<ImagesInformationResponseDto> getImageName() {
        return imageName;
    }

    public void setImageName(ArrayList<ImagesInformationResponseDto> imageName) {
        this.imageName = imageName;
    }

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
