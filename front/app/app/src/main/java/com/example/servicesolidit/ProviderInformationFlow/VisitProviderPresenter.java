package com.example.servicesolidit.ProviderInformationFlow;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Dtos.Requests.RelationalImagesRequestDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Dtos.Responses.Comments.EnableToCommentResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.RelationalImagesResponseDto;

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

    public void enableCommentsSection(int idLogged, int idProviderToLoad) {
        Call<EnableToCommentResponseDto> call = this.service.enableToComentSection(idLogged, idProviderToLoad);
        call.enqueue(new Callback<EnableToCommentResponseDto>() {
            @Override
            public void onResponse(Call<EnableToCommentResponseDto> call, Response<EnableToCommentResponseDto> response) {
                if(response.isSuccessful() && response.body() != null){
                  EnableToCommentResponseDto result = response.body();
                  view.enableCommentsSection(result.isEnableToComment());
                }else{
                    view.onErrorEnableCommentsSection("Ocurrió un error");
                }
            }

            @Override
            public void onFailure(Call<EnableToCommentResponseDto> call, Throwable t) {
                view.onErrorEnableCommentsSection("Ocurrió un error: " + t.getMessage());
            }
        });
    }
}
