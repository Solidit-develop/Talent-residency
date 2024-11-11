package com.example.servicesolidit.LoginFlow;

import android.util.Log;

import com.example.servicesolidit.Utils.Models.Requests.LoginRequestDto;
import com.example.servicesolidit.Utils.Models.Responses.LoginResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {
    private LoginView view;
    private ApiService service;

    public LoginPresenter(LoginView view) {
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void login(String username, String password) {
        Call<LoginResponseDto> call = service.login(new LoginRequestDto(username, password));
        call.enqueue(new Callback<LoginResponseDto>() {
            @Override
            public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {

                if (response.isSuccessful() && response.body() != null) {
                    UserInfoDto loginResponse = response.body().getResponse();
                    Log.i("LoginPresenter", loginResponse.getNameUser());
                    view.onLoginSuccess(loginResponse);

                } else {
                    String responseError = "Usuario o contraseña inválidos";
                    view.onLoginError(responseError);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                view.onLoginError(t.getMessage());
            }
        });
    }
}
