package com.example.servicesolidit.MessageFlow;

import com.example.servicesolidit.Model.Responses.Messages.ConversationDto;
import com.example.servicesolidit.Model.Responses.Messages.MessageDto;

import java.util.List;

public interface MessageView {
    void onConversationLoaded(List<ConversationDto> messages);
    void onErrorConversationLoaded(String messages);
    void onShowProgress();
    void onHideProgress();
}
