package com.example.servicesolidit.Utils.Dtos.Responses.Messages;

import java.util.List;

public class ConversationDto {
    private int id_conversation;
    private InteractuanDto interactuan;
    private List<MessageDto> message;

    public int getIdConversation(){
        return id_conversation;
    }

    public InteractuanDto getInteractuan(){
        return  interactuan;
    }

    public List<MessageDto> getMessage(){
        return message;
    }

}
