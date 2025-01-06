package com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider;

import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.ImagesInformationResponseDto;

import java.util.List;

public class SearchProviderDto {
    private ProviderResponseDto provedor;
    private List<ImagesInformationResponseDto> relacionImagen;

    public ProviderResponseDto getProvedor() {
        return provedor;
    }

    public void setProvedor(ProviderResponseDto provedor) {
        this.provedor = provedor;
    }

    public List<ImagesInformationResponseDto> getRelacionImagen() {
        return relacionImagen;
    }

    public void setRelacionImagen(List<ImagesInformationResponseDto> relacionImagen) {
        this.relacionImagen = relacionImagen;
    }
}
