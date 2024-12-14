package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;

public interface ObtainAppointmentView {
    void onShowProgress();
    void onHideProgress();
    void onSuccessObtainResponse(AppointmentListResponse result);
    void onErrorObtainResponse(String s);

    void onSuccessGetInformationAsProvider(ProviderResponseDto result);

    void onErrorGetInformationAsProvider(String message);
}
