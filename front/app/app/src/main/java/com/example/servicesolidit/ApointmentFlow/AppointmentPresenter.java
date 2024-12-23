package com.example.servicesolidit.ApointmentFlow;


import android.util.Log;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Models.Requests.CreateAppointmentRequestDto;
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

    public void createAppointment(CreateAppointmentRequestDto request, int idProvider, int idCustomer){
        Log.i("AppointmentPresenter", "Se intenta generar cita con provider " + idProvider + " y con customer " + idCustomer);
        Call<AppointmentResponseDto> call = service.createAppointmnt(request, idProvider, idCustomer);
        call.enqueue(new Callback<AppointmentResponseDto>() {
            @Override
            public void onResponse(Call<AppointmentResponseDto> call, Response<AppointmentResponseDto> response) {

                if(response.isSuccessful() && response.body() != null){
                    view.onSuccessAppintmentCreated(response.body().toString());
                }else {
                    view.onErrorAppointmentCreated("Ocurrió un error al generar la cita");
                }
            }

            @Override
            public void onFailure(Call<AppointmentResponseDto> call, Throwable t) {
                view.onErrorAppointmentCreated(t.getMessage());
            }
        });
    }
}
