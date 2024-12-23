package com.example.servicesolidit.Utils.Dtos.Responses.Agreements;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AgreementAppointmentResponseDto {
    @SerializedName("idAppointment")
    private int idAppointment;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("appointmentDate")
    private String appointmentDate;

    @SerializedName("appointmentLocation")
    private String appointmentLocation;

    @SerializedName("statusAppointment")
    private String statusAppointment;

    @SerializedName("user")
    private AgreementAppointmentUserResponseDto user;

    @SerializedName("agreements")
    private ArrayList<AgreementAppointmentAgreeemntResponseDto> agreements;

    public int getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(int idAppointment) {
        this.idAppointment = idAppointment;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public String getStatusAppointment() {
        return statusAppointment;
    }

    public void setStatusAppointment(String statusAppointment) {
        this.statusAppointment = statusAppointment;
    }

    public AgreementAppointmentUserResponseDto getUser() {
        return user;
    }

    public void setUser(AgreementAppointmentUserResponseDto user) {
        this.user = user;
    }

    public ArrayList<AgreementAppointmentAgreeemntResponseDto> getAgreements() {
        return agreements;
    }

    public void setAgreements(ArrayList<AgreementAppointmentAgreeemntResponseDto> agreements) {
        this.agreements = agreements;
    }
}
