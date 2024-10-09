package com.example.servicesolidit.ProfileFlow;

import com.example.servicesolidit.Model.Responses.UserInfoProfileDto;

public interface ProfileView {
    void showProgress();
    void hideProgress();
    void onLoadProfileSuccess(UserInfoProfileDto message);
    void onLoadProfileError(String message);
}
