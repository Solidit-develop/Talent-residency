package com.example.servicesolidit.ProviderInformationFlow;

import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Models.Responses.ImagesRelational.RelationalImagesResponseDto;

public interface VisitProviderView {
    void onSuccessGetImageInformation(RelationalImagesResponseDto response);
    void onErrorGetImageInformation(String error);
    void onShowProgress();
    void onHideProgress();
    void onSuccessObtainResponse(AppointmentListResponse result);
    void onErrorObtainResponse(String s);
}
