package com.example.servicesolidit.ProfileFlow.View;

import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;

public interface ProfileView {
    void showProgress();
    void hideProgress();
    void onLoadProfileSuccess(UserInfoProfileDto message);
    void onLoadProfileError(String message);
}
