package com.example.servicesolidit.LoginFlow;

import com.example.servicesolidit.Model.Responses.UserInfoDto;

public interface LoginView {
    void showProgress();
    void hideProgress();
    void onLoginSuccess(UserInfoDto message);
    void onLoginError(String message);
}