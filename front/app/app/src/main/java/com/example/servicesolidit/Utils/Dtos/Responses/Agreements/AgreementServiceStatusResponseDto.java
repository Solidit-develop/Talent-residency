package com.example.servicesolidit.Utils.Dtos.Responses.Agreements;

import com.google.gson.annotations.SerializedName;

public class AgreementServiceStatusResponseDto {
    @SerializedName("idServiceStatus")
    private String idServiceStatus;

    @SerializedName("description")
    private String description;

    @SerializedName("value")
    private String value;

    public String getIdServiceStatus() {
        return idServiceStatus;
    }

    public void setIdServiceStatus(String idServiceStatus) {
        this.idServiceStatus = idServiceStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
