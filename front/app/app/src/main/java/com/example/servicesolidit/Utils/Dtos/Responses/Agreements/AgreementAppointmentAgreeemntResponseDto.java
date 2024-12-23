package com.example.servicesolidit.Utils.Dtos.Responses.Agreements;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AgreementAppointmentAgreeemntResponseDto {
    @SerializedName("idAgreeemnt")
    private int idAgreeemnt;

    @SerializedName("description")
    private String description;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("agreementService")
    private ArrayList<AgreementServiceResponseDto> agreementService;

    public int getIdAgreeemnt() {
        return idAgreeemnt;
    }

    public void setIdAgreeemnt(int idAgreeemnt) {
        this.idAgreeemnt = idAgreeemnt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public ArrayList<AgreementServiceResponseDto> getAgreementService() {
        return agreementService;
    }

    public void setAgreementService(ArrayList<AgreementServiceResponseDto> agreementService) {
        this.agreementService = agreementService;
    }
}
