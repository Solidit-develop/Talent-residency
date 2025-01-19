package com.example.servicesolidit.Utils.Dtos.Responses.Feed;

import com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider.Skills;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoDto;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

    @SerializedName("experiencias")
    private String experienceYears;

    public String getExperienceYearsToFeed() {
        return experienceYearsToFeed;
    }

    public void setExperienceYearsToFeed(String experienceYearsToFeed) {
        this.experienceYearsToFeed = experienceYearsToFeed;
    }

    @SerializedName("experienceYears")
    private String experienceYearsToFeed;

    @SerializedName("workshopName")
    private String workshopName;

    @SerializedName("workshopPhoneNumber")
    private String workshopPhoneNumber;

    public ArrayList<Skills> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skills> skills) {
        this.skills = skills;
    }

    @SerializedName("skills")
    private ArrayList<Skills> skills;

    @SerializedName("adress")
    private AddressResponseDto address;

    @SerializedName("address")
    private AddressResponseDto addressToFeed;

    public AddressResponseDto getAddressToFeed() {
        return addressToFeed;
    }

    public void setAddressToFeed(AddressResponseDto addressToFeed) {
        this.addressToFeed = addressToFeed;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

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
