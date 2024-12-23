package com.example.servicesolidit.Utils.Dtos.Responses.Agreements;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AgreementResponseDto {
    @SerializedName("citas")
    private ArrayList<AgreementAppointmentResponseDto> citas;

    public ArrayList<AgreementAppointmentResponseDto> getCitas() {
        return citas;
    }

    public void setCitas(ArrayList<AgreementAppointmentResponseDto> citas) {
        this.citas = citas;
    }
}
