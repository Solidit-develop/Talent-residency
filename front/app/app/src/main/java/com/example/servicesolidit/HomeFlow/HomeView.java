package com.example.servicesolidit.HomeFlow;

import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;

import java.util.ArrayList;

public interface HomeView {
    void showProgres();
    void hideProgess();
    void onFeedSuccess(ArrayList<ProviderResponseDto> feedResponse);
    void onFeedError(String error);
}
