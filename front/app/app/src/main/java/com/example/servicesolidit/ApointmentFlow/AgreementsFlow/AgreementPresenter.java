package com.example.servicesolidit.ApointmentFlow.AgreementsFlow;

import android.util.Log;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Dtos.Requests.CreateAgreementRequest;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentResponseDto;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgreementPresenter {
    private AgreementView view;
    private ApiService service;

    public AgreementPresenter(AgreementView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }


    public void createAgreement(CreateAgreementRequest request, int idAppointment, int idProvider) {
        Gson g = new Gson();
        Log.i("AgreementPresenter", "Se intenta generar acuerdo de appointment: " + idAppointment + " de provider " + idProvider + " y request: " + g.toJson(request));
        Call<AppointmentResponseDto> call = this.service.createAgreement(request, idAppointment, idProvider);
        call.enqueue(new Callback<AppointmentResponseDto>() {
            @Override
            public void onResponse(Call<AppointmentResponseDto> call, Response<AppointmentResponseDto> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.i("AgreementPresenter", "Success on response: ");
                    view.onCreateAgreementSuccess("Acuerdo generado correctamente");
                }else{
                    Log.i("AgreementPresenter", "Error on response ");
                    view.onCreateAgreementError("Ocurrió un error al tratar de crear el acuerdo");
                }
            }

            @Override
            public void onFailure(Call<AppointmentResponseDto> call, Throwable t) {
                Log.i("AgreementPresenter", "Error on failure: " + t.getMessage());
                view.onCreateAgreementError("Ocurrió un error al tratar de crear el acuerdo: " + t.getMessage());
            }
        });
    }
}
