package com.example.servicesolidit.ProviderInformationFlow;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Models.Requests.RelationalImagesRequestDto;
import com.example.servicesolidit.Utils.Models.Responses.ImagesRelational.RelationalImagesResponseDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitProviderPresenter {
    private VisitProviderView view;
    private ApiService service;

    public VisitProviderPresenter(VisitProviderView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getProviderImagesByComments(RelationalImagesRequestDto requestDto){
        Call<RelationalImagesResponseDto> call = this.service.getRelationalImages(requestDto.getTable(), requestDto.getIdUsedOn(), requestDto.getFuncionalida());
        call.enqueue(new Callback<RelationalImagesResponseDto>() {
            @Override
            public void onResponse(Call<RelationalImagesResponseDto> call, Response<RelationalImagesResponseDto> response) {
                if(response.isSuccessful() && response.body()!=null){
                    RelationalImagesResponseDto result = response.body();
                    view.onSuccessGetImageInformation(result);
                }else{
                    view.onErrorGetImageInformation("Ocurrió un error al obtener la imagen");
                }
            }

            @Override
            public void onFailure(Call<RelationalImagesResponseDto> call, Throwable t) {
                view.onErrorGetImageInformation("Ocurrió un error al obtener la imagen: " + t.getMessage());
            }
        });
    }
}
