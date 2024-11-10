package com.example.servicesolidit.ProfileFlow.View;

import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;

public interface ProviderView {
    void onLoadProviderInfoSuccess(ProviderResponseDto response);
    void onLoadProviderInfoError(String error);
}
