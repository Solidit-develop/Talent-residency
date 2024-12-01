package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentListResponse;

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
}
