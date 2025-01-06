package com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider;

import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ResponseDto;

import java.util.List;

public class SearchProviderResponseDto extends ResponseDto {
    private List<SearchProviderDto> response;

    public List<SearchProviderDto> getResponse() {
        return response;
    }

    public void setResponse(List<SearchProviderDto> response) {
        this.response = response;
    }
}
