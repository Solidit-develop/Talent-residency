package com.example.servicesolidit.HouseFlow;

public class CardModel {

    private String imageUrl;
    private String nameBussines;
    private String description;
    private String location;

    public CardModel(String imageUrl, String nameBussines, String description, String location){
        this.imageUrl = imageUrl;
        this.nameBussines = nameBussines;
        this.description = description;
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNameBussines() {
        return nameBussines;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

}
