package com.example.servicesolidit.LoginFlow;

import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoDto;

public interface LoginView {
    void showProgress();
    void hideProgress();
    void onLoginSuccess(UserInfoDto message);
    void onLoginError(String message);
}