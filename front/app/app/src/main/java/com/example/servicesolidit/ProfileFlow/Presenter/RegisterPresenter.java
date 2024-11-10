package com.example.servicesolidit.ProfileFlow.Presenter;

import android.util.Log;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.ProfileFlow.RegisterBussines;
import com.example.servicesolidit.ProfileFlow.View.ProfileView;
import com.example.servicesolidit.ProfileFlow.View.RegisterBussinesView;
import com.example.servicesolidit.Utils.Models.Requests.UpdateToProviderRequestDto;
import com.example.servicesolidit.Utils.Models.Responses.RegisterResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UpdateUserToProviderResponseDto;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenter {
    private RegisterBussinesView view;
    private ApiService service;
    Gson gson = new Gson();

    public RegisterPresenter(RegisterBussinesView registerBussines) {
        this.view = registerBussines;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void convertUserToProvider(UpdateToProviderRequestDto request){
        Call<RegisterResponseDto> call = service.updateToUserProvider(request);
        call.enqueue(new Callback<RegisterResponseDto>() {
            @Override
            public void onResponse(Call<RegisterResponseDto> call, Response<RegisterResponseDto> response) {
                Log.i("RegisterPresenter", "Response: " + gson.toJson(response));
                Log.i("RegisterPresenter", "Response: " + gson.toJson(response));

                if (response.isSuccessful() && response.body() != null){
                    RegisterResponseDto responseMessage = response.body();
                    view.onRegisterSuccess(responseMessage.getResponse());
                }else{
                    view.onRegisterFails("Ocurrió un error al tratar de registrarte");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseDto> call, Throwable t) {
                Log.i("RegisterPresenter", "Response: " + t.getMessage());

                view.onRegisterFails(t.getMessage());
            }
        });

    }
}
