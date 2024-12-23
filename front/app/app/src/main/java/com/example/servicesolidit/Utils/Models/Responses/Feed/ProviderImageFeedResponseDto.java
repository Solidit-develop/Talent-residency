package com.example.servicesolidit.Utils.Models.Responses.Feed;

import com.example.servicesolidit.Utils.Models.Responses.ImagesRelational.RelationalImagesResponseDto;

public class ProviderImageFeedResponseDto {
    private ProviderResponseDto provedor;
    private RelationalImagesResponseDto imagen;

    public ProviderImageFeedResponseDto() {}

    public ProviderResponseDto getProvedor() {
        return provedor;
    }

    public void setProvedor(ProviderResponseDto provedor) {
        this.provedor = provedor;
    }

    public RelationalImagesResponseDto getImagen() {
        return imagen;
    }

    public void setImagen(RelationalImagesResponseDto imagen) {
        this.imagen = imagen;
    }
}
