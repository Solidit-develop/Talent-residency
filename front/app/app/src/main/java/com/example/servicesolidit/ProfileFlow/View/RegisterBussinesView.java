package com.example.servicesolidit.ProfileFlow.View;

import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;

public interface RegisterBussinesView {
    void showProgress();
    void hideProgress();
    void onRegisterSuccess(String responseMessage);
    void onRegisterFails(String s);

    void onImageUploadSuccess(String result);
    void onImageUploadError(String s);

    void onSuccessGetInformationAsProvider(ProviderResponseDto result);
    void onErrorGetInformationAsProvider(String message);

    void onSuccessRelationalInformationGenerated(String message);
    void onErrorRelationalInformationGenerated(String s);
}
