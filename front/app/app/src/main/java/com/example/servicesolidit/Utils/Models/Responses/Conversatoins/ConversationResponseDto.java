package com.example.servicesolidit.Utils.Models.Responses.Conversatoins;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConversationResponseDto {

    @SerializedName("interactuan")
    private InteractDto interact;

    @SerializedName("message")
    private List<MessageDto> messages;

    @SerializedName("related")
    private int idRelated;

    public int getIdRelated() {
        return idRelated;
    }

    public void setIdRelated(int idRelated) {
        this.idRelated = idRelated;
    }

    // Constructor, getters y setters
    public ConversationResponseDto(InteractDto interact, List<MessageDto> messages) {
        this.interact = interact;
        this.messages = messages;
    }

    public InteractDto getInteract() {
        return interact;
    }

    public void setInteract(InteractDto interact) {
        this.interact = interact;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }
}
