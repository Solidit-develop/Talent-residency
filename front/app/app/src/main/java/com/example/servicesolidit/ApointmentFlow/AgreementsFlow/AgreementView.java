package com.example.servicesolidit.ApointmentFlow.AgreementsFlow;

public interface AgreementView {
    void onCreateAgreementSuccess(String message);
    void onCreateAgreementError(String s);
    void onShowProgress();
    void onHideProgress();
}
