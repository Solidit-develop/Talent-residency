package com.example.servicesolidit.RegisterFlow;

import com.example.servicesolidit.Model.Dtos.UserRegisterModel;

import java.util.List;

public interface RegisterView {
    void showProgress();
    void hideProgress();
    void onRegisterSuccess(String message);
    void onRegisterError(String message);

    void onGetAllClientsSuccess(List<UserRegisterModel> clients);
}
