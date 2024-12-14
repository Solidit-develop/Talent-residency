package com.example.servicesolidit.Utils.Models.Responses.Appointment;

import com.google.gson.annotations.SerializedName;

public class AppointmentItemResponse{
    @SerializedName("id_appintment")
    private int idAppointment;

    @SerializedName("AppointmentLocation")
    private String appointmentLocation;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("apointmentDate")
    private String appointmentDate;

    @SerializedName("id_provider")
    private int idProvider;

    @SerializedName("id_user")
    private int idUser;

    @SerializedName("name_User")
    private String nameUser;

    @SerializedName("lasname")
    private String lastName;

    @SerializedName("update")
    private String statusUpdate;

    @SerializedName("workshopName")
    private String workshopName;

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    // Getters and Setters
    public int getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(int idAppointment) {
        this.idAppointment = idAppointment;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
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

    public int getIdProvider() {
        return idProvider;
    }

    public void setIdProvider(int idProvider) {
        this.idProvider = idProvider;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(String statusUpdate) {
        this.statusUpdate = statusUpdate;
    }
}
