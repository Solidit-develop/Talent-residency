package com.example.servicesolidit.Utils.Models.Responses.Messages;

import java.util.List;

public class MessagesResponseDto {

    private List<ConversationDto> resultados;

    public MessagesResponseDto(List<ConversationDto> resultados){
        this.resultados = resultados;
    }

    public List<ConversationDto> getResultados() {
        return resultados;
    }

    public void setResultados(List<ConversationDto> resultados) {
        this.resultados = resultados;
    }

}
