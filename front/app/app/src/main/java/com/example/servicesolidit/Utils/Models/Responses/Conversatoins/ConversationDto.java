package com.example.servicesolidit.Utils.Models.Responses.Conversatoins;

public class ConversationDto {
    private String id;
    private String name;
    private String imageUrl;
    private String lastMessage;
    private String timeLastMessage;

    public ConversationDto(String id, String name, String imageUrl, String lastMessage, String timeLastMessage) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.lastMessage = lastMessage;
        this.timeLastMessage = timeLastMessage;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getLastMessage() { return lastMessage; }
    public String getTimeLastMessage() { return timeLastMessage; }
}
