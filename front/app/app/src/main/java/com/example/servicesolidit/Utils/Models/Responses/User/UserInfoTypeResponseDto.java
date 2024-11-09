package com.example.servicesolidit.Utils.Models.Responses.User;

public class UserInfoTypeResponseDto {
    private boolean value;
    private String description;

    public UserInfoTypeResponseDto(boolean value, String description) {
        this.value = value;
        this.description = description;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}