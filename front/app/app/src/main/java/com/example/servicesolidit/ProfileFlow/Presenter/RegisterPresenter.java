package com.example.servicesolidit.ProfileFlow.Presenter;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.ProfileFlow.RegisterBussines;
import com.example.servicesolidit.ProfileFlow.View.ProfileView;
import com.example.servicesolidit.ProfileFlow.View.RegisterBussinesView;
import com.example.servicesolidit.Utils.Models.Requests.UpdateToProviderRequestDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UpdateUserToProviderResponseDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenter {
    private RegisterBussinesView view;
    private ApiService service;

    public RegisterPresenter(RegisterBussinesView registerBussines) {
        this.view = registerBussines;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void convertUserToProvider(UpdateToProviderRequestDto request){
        Call<UpdateUserToProviderResponseDto> call = service.updateToUserProvider(request);

        call.enqueue(new Callback<UpdateUserToProviderResponseDto>() {
            @Override
            public void onResponse(Call<UpdateUserToProviderResponseDto> call, Response<UpdateUserToProviderResponseDto> response) {
                if (response.isSuccessful() && response.body() != null){
                    UpdateUserToProviderResponseDto responseMessage = response.body();
                    if (responseMessage.isSucces()){
                        view.onRegisterSuccess(responseMessage);
                    }else{
                        view.onRegisterFails("Ocurrió un error al tratar de registrarte");
                    }
                }else{
                    view.onRegisterFails("Ocurrió un error al tratar de registrarte");
                }
            }

            @Override
            public void onFailure(Call<UpdateUserToProviderResponseDto> call, Throwable t) {
                view.onRegisterFails(t.getMessage());
            }
        });

    }
}
