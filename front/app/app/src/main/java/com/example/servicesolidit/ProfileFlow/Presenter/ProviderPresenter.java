package com.example.servicesolidit.ProfileFlow.Presenter;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.ProfileFlow.View.ProviderView;
import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProviderProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderPresenter {
    private ProviderView view;
    private ApiService service;

    public ProviderPresenter(ProviderView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }
    public void providerInfomration(int id) {
        Call<UserInfoProviderProfileResponse> call = service.informationProviderByUserId(id);

        call.enqueue(new Callback<UserInfoProviderProfileResponse>() {
            @Override
            public void onResponse(Call<UserInfoProviderProfileResponse> call, Response<UserInfoProviderProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        ProviderResponseDto result = response.body().getResponse();
                        view.onLoadProviderInfoSuccess(result);
                    } catch (Exception e) {
                        view.onLoadProviderInfoError(e.getMessage());
                    }
                } else {
                    view.onLoadProviderInfoError("Ocurrió un error al obtener la información de tu negocio");
                }
            }

            @Override
            public void onFailure(Call<UserInfoProviderProfileResponse> call, Throwable t) {
                view.onLoadProviderInfoError(t.getMessage());
            }
        });

    }
}
