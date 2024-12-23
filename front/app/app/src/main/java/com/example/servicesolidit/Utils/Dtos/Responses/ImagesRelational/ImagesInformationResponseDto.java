package com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ImagesInformationResponseDto {
    @SerializedName("id_images")
    private String idImagen;
    private String urlLocation;
    private String funcionality;
    @SerializedName("imagesRelation")
    private ArrayList<ImagesRelation> imagesRelation;

    public ImagesInformationResponseDto() {    }

    public String getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(String idImagen) {
        this.idImagen = idImagen;
    }

    public String getUrlLocation() {
        return urlLocation;
    }

    public void setUrlLocation(String urlLocation) {
        this.urlLocation = urlLocation;
    }

    public String getFuncionality() {
        return funcionality;
    }

    public void setFuncionality(String funcionality) {
        this.funcionality = funcionality;
    }

    public ArrayList<ImagesRelation> getImagesRelation() {
        return imagesRelation;
    }

    public void setImagesRelation(ArrayList<ImagesRelation> imagesRelation) {
        this.imagesRelation = imagesRelation;
    }
}

class ImagesRelation{
    @SerializedName("id_imagesRelation")
    private String idImagesRelation;
    private String idUsedOn;
    private String tableToRelation;

    public ImagesRelation(){}

    public String getIdImagesRelation() {
        return idImagesRelation;
    }

    public void setIdImagesRelation(String idImagesRelation) {
        this.idImagesRelation = idImagesRelation;
    }

    public String getIdUsedOn() {
        return idUsedOn;
    }

    public void setIdUsedOn(String idUsedOn) {
        this.idUsedOn = idUsedOn;
    }

    public String getTableToRelation() {
        return tableToRelation;
    }

    public void setTableToRelation(String tableToRelation) {
        this.tableToRelation = tableToRelation;
    }
}
