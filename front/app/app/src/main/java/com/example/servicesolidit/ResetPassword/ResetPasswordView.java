package com.example.servicesolidit.ResetPassword;

public interface ResetPasswordView {
    void onShowProgress();
    void onHideProgress();
    void onSuccessReset(String message);
    void onErrorReset(String errror);
}
