package com.example.servicesolidit.MessageFlow;

import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.Messages.ConversationDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProfileDto;

import java.util.List;

public interface MessageView {
    void onConversationLoaded(List<ConversationDto> messages);
    void onErrorConversationLoaded(String messages);
    void onShowProgress();
    void onHideProgress();
    void onMessageSended(String response);
    void onErrorSendMessage(String s);

    void onLoadProviderInfoSuccess(ProviderResponseDto result);

    void onLoadProviderInfoError(String message);

    void onSuccessShowViewToCreateAppointment(ProviderResponseDto result);

    void onErrorShowViewToCreateAppointment(String message);

    void onLoadInfoCustomerSuccess(UserInfoProfileDto result);

    void onLoadInfoCustomerError(String message);
}
