package com.example.servicesolidit.Utils.Models.Responses.Appointment;

import java.util.List;

public class AppointmentListResponse {
    public List<AppointmentItemResponse> regreso;

    public AppointmentListResponse(){}

    public List<AppointmentItemResponse> getRegreso() {
        return regreso;
    }

    public void setRegreso(List<AppointmentItemResponse> regreso) {
        this.regreso = regreso;
    }
}

