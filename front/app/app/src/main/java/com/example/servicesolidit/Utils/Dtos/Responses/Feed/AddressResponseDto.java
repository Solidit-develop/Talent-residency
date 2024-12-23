package com.example.servicesolidit.Utils.Dtos.Responses.Feed;

import com.google.gson.annotations.SerializedName;

public class AddressResponseDto {
    @SerializedName("id_address")
    private int idAddress;

    @SerializedName("street_1")
    private String street1;

    @SerializedName("street_2")
    private String street2;

    @SerializedName("localidad")
    private String localidad;

    @SerializedName("town")
    private TownResponseDto town;

    public AddressResponseDto(int idAddress, String street1, String street2, String localidad, TownResponseDto town) {
        this.idAddress = idAddress;
        this.street1 = street1;
        this.street2 = street2;
        this.localidad = localidad;
        this.town = town;
    }

    public int getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(int idAddress) {
        this.idAddress = idAddress;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public TownResponseDto getTown() {
        return town;
    }

    public void setTown(TownResponseDto town) {
        this.town = town;
    }
}
