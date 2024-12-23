package com.example.servicesolidit.ProfileFlow.View;

public interface RegisterBussinesView {
    void showProgress();
    void hideProgress();
    void onRegisterSuccess(String responseMessage);
    void onRegisterFails(String s);
}
