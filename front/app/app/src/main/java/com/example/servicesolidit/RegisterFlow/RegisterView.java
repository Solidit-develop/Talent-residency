package com.example.servicesolidit.RegisterFlow;

public interface RegisterView {
    void showProgress();
    void hideProgress();
    void onRegisterSuccess(String message);
    void onRegisterError(String message);
}
