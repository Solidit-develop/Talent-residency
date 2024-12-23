package com.example.servicesolidit.Utils.Dtos.Responses.Appointment;

import com.google.gson.annotations.SerializedName;

public class AppointmentItemResponseAsCustomer {
    @SerializedName("id_appintment")
    private int idAppointment;

    @SerializedName("AppointmentLocation")
    private String appointmentLocation;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("apointmentDate")
    private String appointmentDate;

    @SerializedName("id_providers")
    private int idProvider;

    @SerializedName("id_user")
    private int idUser;

    @SerializedName("name_user")
    private String nameUser;

    @SerializedName("lastname")
    private String lastName;

    @SerializedName("update")
    private String statusUpdate;

    @SerializedName("workshopName")
    private String workshopName;
}
