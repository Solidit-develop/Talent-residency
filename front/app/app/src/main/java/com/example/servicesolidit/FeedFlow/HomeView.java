package com.example.servicesolidit.FeedFlow;

import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderImageFeedResponseDto;

import java.util.ArrayList;

public interface HomeView {
    void showProgres();
    void hideProgess();
    void onFeedSuccess(ArrayList<ProviderImageFeedResponseDto> feedResponse);
    void onFeedError(String error);
}
