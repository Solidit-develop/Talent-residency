package com.example.servicesolidit.ProviderInformationFlow;

import android.util.Log;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Dtos.Requests.CreateCommentRequest;
import com.example.servicesolidit.Utils.Dtos.Requests.RelationalImagesRequestDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Comments.EnableToCommentResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Comments.CommentsResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.RelationalImagesResponseDto;
import com.google.gson.Gson;

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

    public void getComments(int idProviderToLoad) {
        view.onShowProgress();
        Call<CommentsResponseDto> call = this.service.getCommentsByProvider(idProviderToLoad);

        call.enqueue(new Callback<CommentsResponseDto>() {
            @Override
            public void onResponse(Call<CommentsResponseDto> call, Response<CommentsResponseDto> response) {
                if(response.isSuccessful() && response.body() != null){
                    CommentsResponseDto result = response.body();
                    view.onSuccessObtainComments(result.getResponse());
                }else{
                    view.onErrorObtainComments("Ocurrió un error al obtener los comentarios");
                }
            }

            @Override
            public void onFailure(Call<CommentsResponseDto> call, Throwable t) {
                view.onErrorObtainComments("Ocurrió un error al obtener los comentarios: " + t.getMessage());
            }
        });
    }

    public void createComment(int idLogged, int idProviderToLoad, CreateCommentRequest request) {
        Gson f = new Gson();
        Log.i("VisitProvider", "Request: " + f.toJson(request));
        Call<AppointmentResponseDto> call = this.service.createComment(idLogged, idProviderToLoad, request);
        call.enqueue(new Callback<AppointmentResponseDto>() {
            @Override
            public void onResponse(Call<AppointmentResponseDto> call, Response<AppointmentResponseDto> response) {
                if(response.isSuccessful() && response.body()!=null){
                    AppointmentResponseDto result = response.body();
                    Log.i("VisitProvider", "Error aqui");
                    view.onCommentCreatedSuccess(result.getMessage());
                }else{
                    Log.i("VisitProvider", "Error acá");
                    view.onCommentCreatedError("Ocurrió un error");
                }
            }

            @Override
            public void onFailure(Call<AppointmentResponseDto> call, Throwable t) {
                Log.i("VisitProvider", "Error hasta acá");
                view.onCommentCreatedError("Ocurrió un error: " + t.getMessage());
            }
        });
    }
}
