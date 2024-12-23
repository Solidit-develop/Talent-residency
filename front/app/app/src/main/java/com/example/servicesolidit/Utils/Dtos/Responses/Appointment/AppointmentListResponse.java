package com.example.servicesolidit.Utils.Dtos.Responses.Appointment;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListResponse {

    @SerializedName("appProvider")
    public ArrayList<AppointmentItemResponse> regreso;

    public AppointmentListResponse(){}

    public List<AppointmentItemResponse> getRegreso() {
        return regreso;
    }

    public void setRegreso(ArrayList<AppointmentItemResponse> regreso) {
        this.regreso = regreso;
    }
}

