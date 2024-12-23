package com.example.servicesolidit.RegisterFlow;

import android.util.Log;

import com.example.servicesolidit.Utils.Dtos.Requests.RegisterRequestDto;
import com.example.servicesolidit.Utils.Dtos.Responses.RegisterResponseDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenter {
    private ApiService service;
    private RegisterView view;


    public RegisterPresenter(RegisterView view) {
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void register(RegisterRequestDto userRequest) {
        Call<RegisterResponseDto> call = service.register(userRequest);
        call.enqueue(new Callback<RegisterResponseDto>() {
            @Override
            public void onResponse(Call<RegisterResponseDto> call, Response<RegisterResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("RegisterPresenter", "Success register");
                    view.onRegisterSuccess("Se ha enviado un correo para confirmar su identidad");
                } else {
                    Log.i("RegisterPresenter", "Success error");
                    view.onRegisterError("Register failed");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseDto> call, Throwable t) {
                Log.i("RegisterPresenter", "Failure error");
                view.onRegisterError(t.getMessage());
            }
        });
    }

}
