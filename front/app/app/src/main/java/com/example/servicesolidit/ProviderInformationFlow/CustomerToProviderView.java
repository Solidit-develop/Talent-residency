package com.example.servicesolidit.ProviderInformationFlow;

import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.Messages.ConversationDto;

import java.util.List;

public interface CustomerToProviderView {
    void showProgres();
    void hideProgess();
    void onPrintStartConversation(boolean isNewConversation, List<ConversationDto> resultados);
    void onPrintStartConversationError(String error);

    void onInforProviderLoaded(ProviderResponseDto response);

    void onInfoProviderError(String s);
}
