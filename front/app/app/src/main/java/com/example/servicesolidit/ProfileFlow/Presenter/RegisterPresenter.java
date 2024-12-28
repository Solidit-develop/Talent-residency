package com.example.servicesolidit.ProfileFlow.Presenter;

import android.util.Log;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.ProfileFlow.View.RegisterBussinesView;
import com.example.servicesolidit.Utils.Dtos.Requests.UpdateToProviderRequestDto;
import com.example.servicesolidit.Utils.Dtos.Requests.UploadRelationalImageDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.ProviderImageLoadedResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.UploadImageResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.RegisterResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProviderProfileResponse;
import com.google.gson.Gson;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenter {
    private RegisterBussinesView view;
    private ApiService service;
    Gson gson = new Gson();

    public RegisterPresenter(RegisterBussinesView registerBussines) {
        this.view = registerBussines;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void convertUserToProvider(UpdateToProviderRequestDto request){
        Call<RegisterResponseDto> call = service.updateToUserProvider(request);
        call.enqueue(new Callback<RegisterResponseDto>() {
            @Override
            public void onResponse(Call<RegisterResponseDto> call, Response<RegisterResponseDto> response) {
                if (response.isSuccessful() && response.body() != null){
                    RegisterResponseDto responseMessage = response.body();
                    view.onRegisterSuccess(responseMessage.getResponse());
                }else{
                    view.onRegisterFails("Ocurrió un error al tratar de registrarte");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseDto> call, Throwable t) {
                view.onRegisterFails(t.getMessage());
            }
        });

    }

    public void loadImageToProvider(MultipartBody.Part imagePart) {
        Log.i("RegisterPresenter", "Should send image petition");
        Call<UploadImageResponseDto> call = this.service.uploadImage(imagePart);
        call.enqueue(new Callback<UploadImageResponseDto>() {
            @Override
            public void onResponse(Call<UploadImageResponseDto> call, Response<UploadImageResponseDto> response) {
                Log.i("RegisterPresenter - Response", response.message());
                if(response.isSuccessful() && response.body()!= null){
                    UploadImageResponseDto result = response.body();
                    Log.i("RegisterPresenter - Result", result.getResponse());
                    view.onImageUploadSuccess(result.getResponse());
                }else{
                    view.onImageUploadError("Ocurrió un error al cargar tu imagen, vuelve a intentarlo más tarde");
                }
            }

            @Override
            public void onFailure(Call<UploadImageResponseDto> call, Throwable t) {
                view.onImageUploadError("Ocurrió un error: " + t.getMessage());
            }
        });
        view.hideProgress();
    }

    public void getProviderInformation(int id) {
        Log.i("RegisterPresenter", "Se buscará al provider tomando información como user con id: " + id);
        Call<UserInfoProviderProfileResponse> call = service.informationProviderByUserId(id);
        call.enqueue(new Callback<UserInfoProviderProfileResponse>() {
            @Override
            public void onResponse(Call<UserInfoProviderProfileResponse> call, Response<UserInfoProviderProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        ProviderResponseDto result = response.body().getResponse();
                        Log.i("OAPPresenter", "result: " + result.getIdProvidersss());
                        view.onSuccessGetInformationAsProvider(result);
                    } catch (Exception e) {
                        view.onErrorGetInformationAsProvider(e.getMessage());
                    }
                } else {
                    view.onErrorGetInformationAsProvider("Ocurrió un error al obtener la información de tu negocio");
                }
            }

            @Override
            public void onFailure(Call<UserInfoProviderProfileResponse> call, Throwable t) {
                view.onErrorGetInformationAsProvider(t.getMessage());
            }
        });
    }

    public void generateRelationalInformation(UploadRelationalImageDto request, int idProviderGenerated) {
        Call<ProviderImageLoadedResponseDto> call = this.service.loadImageRelationalInformationProvider(idProviderGenerated, request);
        call.enqueue(new Callback<ProviderImageLoadedResponseDto>() {
            @Override
            public void onResponse(Call<ProviderImageLoadedResponseDto> call, Response<ProviderImageLoadedResponseDto> response) {
                if(response.isSuccessful() && response.body() != null) {
                    ProviderImageLoadedResponseDto result = response.body();
                    view.onSuccessRelationalInformationGenerated(result.getMessage());
                }else{
                    view.onErrorRelationalInformationGenerated("Ocurrió un error al generar la información de la relación entre la imagen y el proveedor");
                }
            }

            @Override
            public void onFailure(Call<ProviderImageLoadedResponseDto> call, Throwable t) {
                view.onErrorRelationalInformationGenerated("Ocurrió un error: " + t.getMessage());
            }
        });
    }
}
