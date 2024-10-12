package com.example.servicesolidit.ProfileFlow;

import android.util.Log;

import com.example.servicesolidit.Model.Responses.UserInfoDto;
import com.example.servicesolidit.Model.Responses.UserInfoProfileDto;
import com.example.servicesolidit.Model.Responses.UserInfoProfileResponseDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePresenter {
    private ProfileView view;
    private ApiService service;

    public ProfilePresenter(ProfileView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void information(int idToFind){
        Call<UserInfoProfileResponseDto> call = service.information(idToFind);
        call.enqueue(new Callback<UserInfoProfileResponseDto>() {
            @Override
            public void onResponse(Call<UserInfoProfileResponseDto> call, Response<UserInfoProfileResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserInfoProfileDto result = response.body().getResponse();
                    Log.i("ProfilePresenter", result.getNameUser());
                    view.onLoadProfileSuccess(result);

                } else {
                    String responseError = "Usuario o contraseña inválidos";
                    view.onLoadProfileError(responseError);
                }
            }

            @Override
            public void onFailure(Call<UserInfoProfileResponseDto> call, Throwable t) {
                view.onLoadProfileError(t.getMessage());
            }
        });
    }
}