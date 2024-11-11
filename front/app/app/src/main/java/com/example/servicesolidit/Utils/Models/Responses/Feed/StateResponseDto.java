package com.example.servicesolidit.Utils.Models.Responses.Feed;

import com.google.gson.annotations.SerializedName;

public class StateResponseDto {
    @SerializedName("id_state")
    private int idState;

    @SerializedName("name_State")
    private String nameState;

    public StateResponseDto(int idState, String nameState) {
        this.idState = idState;
        this.nameState = nameState;
    }

    public int getIdState() {
        return idState;
    }

    public void setIdState(int idState) {
        this.idState = idState;
    }

    public String getNameState() {
        return nameState;
    }

    public void setNameState(String nameState) {
        this.nameState = nameState;
    }
}
