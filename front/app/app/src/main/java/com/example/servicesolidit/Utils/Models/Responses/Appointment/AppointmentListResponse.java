package com.example.servicesolidit.Utils.Models.Responses.Appointment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppointmentListResponse {

    @SerializedName("appProvider")
    public List<AppointmentItemResponse> regreso;

    public AppointmentListResponse(){}

    public List<AppointmentItemResponse> getRegreso() {
        return regreso;
    }

    public void setRegreso(List<AppointmentItemResponse> regreso) {
        this.regreso = regreso;
    }
}

