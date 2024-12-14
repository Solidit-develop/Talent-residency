package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

import android.util.Log;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProviderProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObtainAppointmentPresenter {
    private ObtainAppointmentView view;
    private ApiService service;

    public ObtainAppointmentPresenter(ObtainAppointmentView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getAppointments(int idLogged){
        Call<AppointmentListResponse> call = service.obtenerAppointmntsList(idLogged);
        call.enqueue(new Callback<AppointmentListResponse>() {
            @Override
            public void onResponse(Call<AppointmentListResponse> call, Response<AppointmentListResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    AppointmentListResponse result = response.body();
                    view.onSuccessObtainResponse(result);
                }else{
                    view.onErrorObtainResponse("Ocurrió un error al consultar el servicio");
                }
            }

            @Override
            public void onFailure(Call<AppointmentListResponse> call, Throwable t) {
                view.onErrorObtainResponse("Ocurrió un error al consultar el servicio: " + t.getMessage());
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
}
