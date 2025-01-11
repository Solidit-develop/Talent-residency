package com.example.servicesolidit.HomeFlow;

import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;

public interface ActivityView {
    void onShowProgress();
    void onHideProgress();
    void onSuccessSaveImage(String message);
    void onErrorSaveImage(String s);
    void onSuccessUploadImage(String response);
    void onErrorUploadImage(String s);

    void onLoadProfileSuccess(UserInfoProfileDto result);

    void onLoadProfileError(String message);
}
