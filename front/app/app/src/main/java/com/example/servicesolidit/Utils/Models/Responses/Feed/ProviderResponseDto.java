package com.example.servicesolidit.Utils.Models.Responses.Feed;

import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoDto;
import com.google.gson.annotations.SerializedName;

public class ProviderResponseDto {
    @SerializedName("id_provider")
    private int idProvider;

    @SerializedName("experienceYears")
    private String experienceYears;

    @SerializedName("workshopName")
    private String workshopName;

    @SerializedName("workshopPhoneNumber")
    private String workshopPhoneNumber;

    @SerializedName("address")
    private AddressResponseDto address;

    @SerializedName("user")
    private UserInfoDto userInfoRelated;

    public UserInfoDto getUserInfoRelated() {
        return userInfoRelated;
    }

    public void setUserInfoRelated(UserInfoDto userInfoRelated) {
        this.userInfoRelated = userInfoRelated;
    }

    public ProviderResponseDto(){}

    public ProviderResponseDto(int idProvider, String experienceYears, String workshopName, String workshopPhoneNumber, AddressResponseDto address) {
        this.idProvider = idProvider;
        this.experienceYears = experienceYears;
        this.workshopName = workshopName;
        this.workshopPhoneNumber = workshopPhoneNumber;
        this.address = address;
    }

    public int getIdProvider() {
        return idProvider;
    }

    public void setIdProvider(int idProvider) {
        this.idProvider = idProvider;
    }

    public String getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(String experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getWorkshopPhoneNumber() {
        return workshopPhoneNumber;
    }

    public void setWorkshopPhoneNumber(String workshopPhoneNumber) {
        this.workshopPhoneNumber = workshopPhoneNumber;
    }

    public AddressResponseDto getAddress() {
        return address;
    }

    public void setAddress(AddressResponseDto address) {
        this.address = address;
    }
}
