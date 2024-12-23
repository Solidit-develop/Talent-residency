package com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RelationalImagesResponseDto {
    private String table;
    private String idUsedOn;
    private String funcionality;
    private String message;
    @SerializedName("relacionImagen")
    private ArrayList<ImagesInformationResponseDto> imageName;

    public RelationalImagesResponseDto(){}
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getIdUsedOn() {
        return idUsedOn;
    }

    public void setIdUsedOn(String idUsedOn) {
        this.idUsedOn = idUsedOn;
    }

    public String getFuncionality() {
        return funcionality;
    }

    public void setFuncionality(String funcionality) {
        this.funcionality = funcionality;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ImagesInformationResponseDto> getImageName() {
        return imageName;
    }

    public void setImageName(ArrayList<ImagesInformationResponseDto> imageName) {
        this.imageName = imageName;
    }
}
