package com.example.servicesolidit.Utils.Dtos.Responses.Conversatoins;

import com.example.servicesolidit.Utils.Dtos.Responses.ResponseDto;

import java.util.List;

public class ConversationResponse extends ResponseDto {
    private List<ConversationResultDto> response;
    public ConversationResponse(int code, boolean success, List<ConversationResultDto> response){
        super();
        this.code = code;
        this.success = success;
        this.response = response;
    }

    public List<ConversationResultDto> getResponse() {
        return response;
    }

    public void setResponse(List<ConversationResultDto> response) {
        this.response = response;
    }
}
