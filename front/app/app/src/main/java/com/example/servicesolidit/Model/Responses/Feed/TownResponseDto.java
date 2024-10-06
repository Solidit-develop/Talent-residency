package com.example.servicesolidit.Model.Responses.Feed;

import com.google.gson.annotations.SerializedName;

public class TownResponseDto {
    @SerializedName("id_town")
    private int idTown;

    @SerializedName("name_Town")
    private String nameTown;

    @SerializedName("zipCode")
    private String zipCode;

    @SerializedName("state")
    private StateResponseDto state;

    public TownResponseDto(int idTown, String nameTown, String zipCode, StateResponseDto state) {
        this.idTown = idTown;
        this.nameTown = nameTown;
        this.zipCode = zipCode;
        this.state = state;
    }

    public int getIdTown() {
        return idTown;
    }

    public void setIdTown(int idTown) {
        this.idTown = idTown;
    }

    public String getNameTown() {
        return nameTown;
    }

    public void setNameTown(String nameTown) {
        this.nameTown = nameTown;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public StateResponseDto getState() {
        return state;
    }

    public void setState(StateResponseDto state) {
        this.state = state;
    }
}
