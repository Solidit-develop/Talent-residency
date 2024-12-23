package com.example.servicesolidit.Utils.Models.Responses.Feed;

import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoDto;
import com.google.gson.annotations.SerializedName;

public class ProviderResponseDto {
    /**
     * Used to get id on feed service.
     */
    @SerializedName("id_provider")
    private int idProvider;

    /**
     * Used to get id on GetInfoProviderFromUserID
     */
    @SerializedName("id_provedores")
    private int idProvidersss;

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

    @SerializedName("photoProvider")
    private String photoProvider;

    @SerializedName("descripcion")
    private String descripcion;



    public String getPhotoProvider() {
        return photoProvider;
    }

    public void setPhotoProvider(String photoProvider) {
        this.photoProvider = photoProvider;
    }

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

    public int getIdProvidersss() {
        return idProvidersss;
    }

    public void setIdProvidersss(int idProvidersss) {
        this.idProvidersss = idProvidersss;
    }
}
