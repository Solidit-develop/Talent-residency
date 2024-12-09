package com.example.servicesolidit.Utils.Models.Responses.Conversatoins;

import com.google.gson.annotations.SerializedName;

public class InteractDto {
    @SerializedName("nombre")
    private String name;

    @SerializedName("id_dest")
    private int destinationId;

    // Constructor, getters y setters
    public InteractDto(String name, int destinationId) {
        this.name = name;
        this.destinationId = destinationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }
}