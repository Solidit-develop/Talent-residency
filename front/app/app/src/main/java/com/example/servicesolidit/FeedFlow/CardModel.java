package com.example.servicesolidit.FeedFlow;

public class CardModel {

    private String imageUrl;
    private String nameBussines;
    private String description;
    private String location;
    private int idProvider;
    private int idProviderAsUser;

    public int getIdProviderAsUser() {
        return idProviderAsUser;
    }

    public void setIdProviderAsUser(int idProviderAsUser) {
        this.idProviderAsUser = idProviderAsUser;
    }

    public void setIdProvider(int idProvider) {
        this.idProvider = idProvider;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setNameBussines(String nameBussines) {
        this.nameBussines = nameBussines;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CardModel(){

    }

    public CardModel(String imageUrl, String nameBussines, String description, String location, int idProvider){
        this.imageUrl = imageUrl;
        this.nameBussines = nameBussines;
        this.description = description;
        this.location = location;
        this.idProvider = idProvider;
    }

    public int getIdProvider() {
        return idProvider;
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
