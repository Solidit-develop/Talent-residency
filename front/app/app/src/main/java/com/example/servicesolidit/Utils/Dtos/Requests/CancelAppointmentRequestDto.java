package com.example.servicesolidit.Utils.Dtos.Requests;

import com.google.gson.annotations.SerializedName;

public class CancelAppointmentRequestDto {
    @SerializedName("id_customer")
    private String idCustomer;

    @SerializedName("id_appointment")
    private String idAppointment;

    public CancelAppointmentRequestDto(String idCustomer, String idAppointment) {
        this.idCustomer = idCustomer;
        this.idAppointment = idAppointment;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(String idAppointment) {
        this.idAppointment = idAppointment;
    }
}
