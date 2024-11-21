package com.example.servicesolidit.ApointmentFlow;


import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentResponseDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentPresenter {
    private AppointmentView view;
    private ApiService service;

    public AppointmentPresenter(AppointmentView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void createAppointment(String request){
        Call<AppointmentResponseDto> call = service.createAppointmnt();
        call.enqueue(new Callback<AppointmentResponseDto>() {
            @Override
            public void onResponse(Call<AppointmentResponseDto> call, Response<AppointmentResponseDto> response) {
                view.onSuccessAppintmentCreated(response.body().toString());
            }

            @Override
            public void onFailure(Call<AppointmentResponseDto> call, Throwable t) {
                view.onErrorAppointmentCreated(t.getMessage());
            }
        });
    }
}
