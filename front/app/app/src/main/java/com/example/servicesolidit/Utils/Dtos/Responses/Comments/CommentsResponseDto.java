package com.example.servicesolidit.Utils.Dtos.Responses.Comments;

import java.util.ArrayList;

public class CommentsResponseDto{
    private ArrayList<CommentsDto> response;

    public CommentsResponseDto(ArrayList<CommentsDto> response) {
        this.response = response;
    }

    public ArrayList<CommentsDto> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<CommentsDto> response) {
        this.response = response;
    }
}
