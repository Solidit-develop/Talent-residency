package com.example.servicesolidit.ApointmentFlow.ViewAgreements;

import com.example.servicesolidit.Utils.Dtos.Responses.Agreements.AgreementAppointmentResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;

import java.util.ArrayList;

public interface ObtainAgreementsView {

    void onLoadProfileSuccess(UserInfoProfileDto result);

    void onLoadProfileError(String message);
    void onShowProgress();
    void onHideProgress();

    void onSuccessGetInformationAsProvider(ProviderResponseDto result);

    void onErrorGetInformationAsProvider(String message);

    void onErrorObtainAgreements(String s);

    void onSuccessObtainAgreements(ArrayList<AgreementAppointmentResponseDto> citas);
}
