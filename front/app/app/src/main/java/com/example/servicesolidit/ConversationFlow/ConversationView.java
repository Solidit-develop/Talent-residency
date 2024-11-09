package com.example.servicesolidit.ConversationFlow;

import com.example.servicesolidit.Utils.Models.Responses.Conversatoins.ConversationResponseDto;

import java.util.List;

public interface ConversationView {
    void showProgress();
    void hideProgress();
    void onConversationSucess(List<ConversationResponseDto> response);
    void onConversationFail(String error);
}
