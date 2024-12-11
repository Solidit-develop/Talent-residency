package com.example.servicesolidit.ApointmentFlow;

public interface AppointmentView {
    void onSuccessAppintmentCreated(String appointmentResponse);
    void onErrorAppointmentCreated(String errorMessage);
    void onShowProgress();
    void onHideProgress();
}
