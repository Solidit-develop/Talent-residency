package com.example.servicesolidit.Search;

import com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider.SearchProviderResponseDto;

import java.util.List;

public interface SearchProviderView {
    void onResultFound(List<SearchProviderResponseDto> response);
    void onErrorResult(String error);
    void onShowProgress();
    void onHideProgress();
}
