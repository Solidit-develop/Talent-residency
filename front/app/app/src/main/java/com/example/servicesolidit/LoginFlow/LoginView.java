package com.example.servicesolidit.LoginFlow;

public interface LoginView {
    void showProgress();
    void hideProgress();
    void onLoginSuccess(String message);
    void onLoginError(String message);
}