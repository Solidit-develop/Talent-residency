package com.example.servicesolidit.Utils.Dtos.Responses.Agreements;

import com.google.gson.annotations.SerializedName;

public class AgreementServiceResponseDto {
    @SerializedName("idAgreement")
    private String idAgreement;

    @SerializedName("description")
    private String description;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("serviceStatus")
    private AgreementServiceStatusResponseDto serviceStatus;

    public String getIdAgreement() {
        return idAgreement;
    }

    public void setIdAgreement(String idAgreement) {
        this.idAgreement = idAgreement;
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

    public AgreementServiceStatusResponseDto getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(AgreementServiceStatusResponseDto serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
}
