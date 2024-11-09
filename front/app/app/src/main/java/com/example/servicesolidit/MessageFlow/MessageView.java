package com.example.servicesolidit.MessageFlow;

import com.example.servicesolidit.Utils.Models.Responses.Messages.ConversationDto;

import java.util.List;

public interface MessageView {
    void onConversationLoaded(List<ConversationDto> messages);
    void onErrorConversationLoaded(String messages);
    void onShowProgress();
    void onHideProgress();
}
