package com.example.servicesolidit.ProfileFlow;

import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProfileDto;

public interface ProfileView {
    void showProgress();
    void hideProgress();
    void onLoadProfileSuccess(UserInfoProfileDto message);
    void onLoadProfileError(String message);
}
