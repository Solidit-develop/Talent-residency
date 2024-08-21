package com.example.servicesolidit.LoginFlow;

import android.util.Log;

import com.example.servicesolidit.Model.LoginResponse;
import com.example.servicesolidit.Model.User;
import com.example.servicesolidit.Network.LoginService;
import com.example.servicesolidit.Network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {
    private LoginView view;
    private LoginService service;

    public LoginPresenter(LoginView view) {
        this.view = view;
        this.service = RetrofitClient.getClient().create(LoginService.class);
    }

    public void login(String username, String password) {
        view.showProgress();
        Call<LoginResponse> call = service.login(new User(username, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    view.onLoginSuccess(response.body().getMessage());
                } else {
                    view.onLoginError("Login failed");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                System.out.println("Error on failure: " + t.getMessage());
                view.hideProgress();
                view.onLoginError(t.getMessage());
            }
        });
    }
}
