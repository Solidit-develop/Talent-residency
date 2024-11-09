package com.example.servicesolidit.ProfileFlow;

import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;

public interface ProviderView {
    void onLoadProviderInfoSuccess(ProviderResponseDto response);
    void onLoadProviderInfoError(String error);
}
