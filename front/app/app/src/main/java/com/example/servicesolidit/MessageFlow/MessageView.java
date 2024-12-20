package com.example.servicesolidit.MessageFlow;

import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.Messages.ConversationDto;

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
}
