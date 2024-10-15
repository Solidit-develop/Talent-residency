package com.example.servicesolidit.ProviderInformationFlow;

import com.example.servicesolidit.Model.Responses.Messages.ConversationDto;

import java.util.List;

public interface CustomerToProviderView {
    void showProgres();
    void hideProgess();
    void onPrintStartConversation(boolean isNewConversation, List<ConversationDto> resultados);
    void onPrintStartConversationError(String error);
}
