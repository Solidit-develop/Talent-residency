package com.example.servicesolidit.ApointmentFlow.ViewAgreements;

import android.util.Log;

import com.example.servicesolidit.ApointmentFlow.ViewAppointments.ObtainAppointmentPresenter;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Dtos.Responses.Agreements.AgreementResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProviderProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObtainAgreementsPresenter {
    private ObtainAgreementsView view;
    private ApiService service;

    public ObtainAgreementsPresenter(ObtainAgreementsView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void information(int idToFind){
        Call<UserInfoProfileResponseDto> call = service.information(idToFind);
        call.enqueue(new Callback<UserInfoProfileResponseDto>() {
            @Override
            public void onResponse(Call<UserInfoProfileResponseDto> call, Response<UserInfoProfileResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try{
                        UserInfoProfileDto result = response.body().getResponse().getResponse();
                        Log.i("ProfilePresenterSucces", result.getNameUser());
                        view.onLoadProfileSuccess(result);
                    }catch (Exception e){
                        view.onLoadProfileError(e.getMessage());
                    }

                } else {
                    String responseError = "Usuario o contraseña inválidos";
                    Log.i("ProfilePresenterElse", responseError);
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

    public void getProviderInformationFromUserId(int id) {
        Log.i("MessagePresenter", "Se buscará al provider tomando información como user con id: " + id);
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

    public void getAgreements(int id, String asProvider) {
        Log.i("ObtainAgreementPresenter", "Buscará acuerdos del id " + id + " como " + asProvider);
        switch (asProvider){
            case "asProvider":
                Call<AgreementResponseDto> call = this.service.obtainAgreements(id);
                call.enqueue(new Callback<AgreementResponseDto>() {
                    @Override
                    public void onResponse(Call<AgreementResponseDto> call, Response<AgreementResponseDto> response) {
                      if(response.isSuccessful() && response.body()!= null) {
                          AgreementResponseDto result = response.body();
                          view.onSuccessObtainAgreements(result.getCitas());
                      }else{
                          view.onErrorObtainAgreements("Ocurrió un error al consultar los acuerdos");
                      }
                    }

                    @Override
                    public void onFailure(Call<AgreementResponseDto> call, Throwable t) {
                        view.onErrorObtainAgreements("Ocurrió un error al consultar los acuerdos");
                    }
                });
                break;
        }
    }
}
