package com.example.servicesolidit.HomeFlow;

import android.util.Log;

import com.example.servicesolidit.Utils.Models.Responses.Feed.FeedResponseDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter {
    private HomeView view;
    private ApiService service;

    public HomePresenter(HomeView view) {
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void feed(String limit, String offset) {
        // Call<FeedResponseDto> call = service.feed(); <- should read limit and offset to charge by lotes.
        Call<FeedResponseDto> call = service.feed();
        call.enqueue(new Callback<FeedResponseDto>() {
            @Override
            public void onResponse(Call<FeedResponseDto> call, Response<FeedResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FeedResponseDto feedResponseDto = response.body();
                    Log.i("HomePresenter", "Id Slected: " +feedResponseDto.getResponse());
                    view.onFeedSuccess(feedResponseDto.getResponse());

                } else {
                    String responseError = "Error al cargar el feed...";
                    view.onFeedError(responseError);
                }
            }

            @Override
            public void onFailure(Call<FeedResponseDto> call, Throwable t) {
                view.onFeedError(t.getMessage());
            }
        });
    }
}
