package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentItemResponse;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProfileDto;

import java.util.ArrayList;

public interface ObtainAppointmentView {
    void onShowProgress();
    void onHideProgress();
    void onSuccessObtainResponse(ArrayList<AppointmentItemResponse> result);
    void onErrorObtainResponse(String s);

    void onSuccessGetInformationAsProvider(ProviderResponseDto result);

    void onErrorGetInformationAsProvider(String message);

    void onLoadProfileSuccess(UserInfoProfileDto result);

    void onLoadProfileError(String message);
}
