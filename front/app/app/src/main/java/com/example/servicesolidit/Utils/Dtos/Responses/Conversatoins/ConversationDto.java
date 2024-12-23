package com.example.servicesolidit.Utils.Dtos.Responses.Conversatoins;

public class ConversationDto {
    private String id;
    private String name;
    private String imageUrl;
    private String lastMessage;
    private String timeLastMessage;
    private String idRelated;


    public ConversationDto(String id, String name, String imageUrl, String lastMessage, String timeLastMessage, String idRelated) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.lastMessage = lastMessage;
        this.timeLastMessage = timeLastMessage;
        this.idRelated = idRelated;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getLastMessage() { return lastMessage; }
    public String getTimeLastMessage() { return timeLastMessage; }
    public String getIdRelated() { return idRelated; }
}
