package com.example.servicesolidit.HomeFlow;

import android.util.Log;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Dtos.Requests.UploadRelationalImageDto;
import com.example.servicesolidit.Utils.Dtos.Responses.GenerarMessageResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.ProviderImageLoadedResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.UploadImageResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileResponseDto;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPresenter {
    private ActivityView view;
    private ApiService service;

    public ActivityPresenter(ActivityView view) {
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }


    public void loadImageToProvider(MultipartBody.Part imagePart) {
        Log.i("ActivityPresenter", "Should send image petition");
        Call<UploadImageResponseDto> call = this.service.uploadImage(imagePart);
        call.enqueue(new Callback<UploadImageResponseDto>() {
            @Override
            public void onResponse(Call<UploadImageResponseDto> call, Response<UploadImageResponseDto> response) {
                Log.i("ActivityPresenter - Response", response.message());
                if(response.isSuccessful() && response.body()!= null){
                    UploadImageResponseDto result = response.body();
                    Log.i("ActivityPresenter - Result", result.getResponse());
                    view.onSuccessUploadImage(result.getResponse());
                }else{
                    view.onErrorUploadImage("Ocurrió un error al cargar tu imagen, vuelve a intentarlo más tarde");
                }
            }

            @Override
            public void onFailure(Call<UploadImageResponseDto> call, Throwable t) {
                view.onErrorUploadImage("Ocurrió un error: " + t.getMessage());
            }
        });
        view.onHideProgress();
    }

    public void saveImageInformation(int loggedId, UploadRelationalImageDto request) {
        Call<GenerarMessageResponseDto> call = this.service.loadImageRelationalInformationUserProfile(loggedId, request);
        call.enqueue(new Callback<GenerarMessageResponseDto>() {
            @Override
            public void onResponse(Call<GenerarMessageResponseDto> call, Response<GenerarMessageResponseDto> response) {
                if(response.isSuccessful() && response.body() != null) {
                    GenerarMessageResponseDto result = response.body();
                    view.onSuccessSaveImage(result.getMessage());
                }else{
                    view.onErrorSaveImage("Ocurrió un error al generar la información de la relación entre la imagen y el proveedor");
                }
            }

            @Override
            public void onFailure(Call<GenerarMessageResponseDto> call, Throwable t) {
                view.onErrorUploadImage("Ocurrió un error: " + t.getMessage());
            }
        });
    }

    public void getImageInformation(int loggedId) {
        Call<UserInfoProfileResponseDto> call = service.information(loggedId);
            call.enqueue(new Callback<UserInfoProfileResponseDto>() {
                @Override
                public void onResponse(Call<UserInfoProfileResponseDto> call, Response<UserInfoProfileResponseDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try{
                            UserInfoProfileDto result = response.body().getResponse().getResponse();
                            Log.i("ActivityPresenter", result.getNameUser());
                            view.onLoadProfileSuccess(result);
                        }catch (Exception e){
                            view.onLoadProfileError(e.getMessage());
                        }

                    } else {
                        String responseError = "No info found";
                        Log.i("ActivityPresenter", responseError);
                        view.onLoadProfileError(responseError);
                    }
                }

                @Override
                public void onFailure(Call<UserInfoProfileResponseDto> call, Throwable t) {
                    Log.i("ProfilePresenterFailure", t.getMessage());
                    view.onLoadProfileError(t.getMessage());
                }
            });
    }
}
