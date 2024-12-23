package com.example.servicesolidit.ConversationFlow;

import com.example.servicesolidit.Utils.Dtos.Responses.Conversatoins.ConversationResultDto;

import java.util.List;

public interface ConversationView {
    void showProgress();
    void hideProgress();
    void onConversationSucess(List<ConversationResultDto> response);
    void onConversationFail(String error);
}
