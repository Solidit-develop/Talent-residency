package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentListResponse;

public interface ObtainAppointmentView {
    void onShowProgress();
    void onHideProgress();
    void onSuccessObtainResponse(AppointmentListResponse result);
    void onErrorObtainResponse(String s);
}
