package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

import android.util.Log;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Dtos.Requests.CancelAppointmentRequestDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentItemResponse;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProviderProfileResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

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

    public void getAppointments(int idToFind, String howToFind){
        Log.i("OAP", "Consultar los appointments de " + idToFind + " y buscarlo como " + howToFind);
        switch (howToFind){
            case "asProvider": {
                Log.i("OAP", "Start find as Provider");
                Call<AppointmentListResponse> call = service.obtenerAppointmntsListAsProvider(idToFind);
                call.enqueue(new Callback<AppointmentListResponse>() {
                    @Override
                    public void onResponse(Call<AppointmentListResponse> call, Response<AppointmentListResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AppointmentListResponse result = response.body();
                            view.onSuccessObtainResponse(result.regreso);
                        } else {
                            view.onErrorObtainResponse("Ocurrió un error al consultar el servicio");
                        }
                    }

                    @Override
                    public void onFailure(Call<AppointmentListResponse> call, Throwable t) {
                        view.onErrorObtainResponse("Ocurrió un error al consultar el servicio: " + t.getMessage());
                    }
                });
            }
            break;
            case "asCustomer":{
                Log.i("OAP", "Start find as Customer");

                Call<ArrayList<AppointmentItemResponse>> call = service.obtenerAppointmntsListAsCustomer(idToFind);
                call.enqueue(new Callback<ArrayList<AppointmentItemResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<AppointmentItemResponse>> call, Response<ArrayList<AppointmentItemResponse>> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            view.onSuccessObtainResponse(response.body());
                        }else{
                            view.onErrorObtainResponse("Ocurrió un error al consultar el servicio");
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<AppointmentItemResponse>> call, Throwable t) {
                        view.onErrorObtainResponse("Ocurrió un error al consultar el servicio: " + t.getMessage());
                    }
                });
            }
            break;
        }
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

    public void updateAppointment(int appointmentId, String newStatus, int idProvider, int idCustomer) {
        Log.i("OAP", "Trying to update the appointment id " + appointmentId + " to " + newStatus + " provider: " + idProvider + " customer " + idCustomer);
        switch (newStatus) {
            case "Cancelar":
                Call<AppointmentResponseDto> call = this.service.cancelAppointment(
                        new CancelAppointmentRequestDto(String.valueOf(idCustomer), String.valueOf(appointmentId)),
                        idProvider);

                call.enqueue(new Callback<AppointmentResponseDto>() {
                    @Override
                    public void onResponse(Call<AppointmentResponseDto> call, Response<AppointmentResponseDto> response) {
                        Log.i("OAP", "OnResposne");
                        Gson g = new Gson();
                        if(response.isSuccessful() && response.body()!=null) {
                            view.onAppointmentUpdated("Actualizado correctamente");
                        }else{
                            Log.i("OAP", "Error On Response Body");

                            view.onAppointmentUpdatedError("Ocurrió un error al intentar actualizar la cita");
                        }
                    }

                    @Override
                    public void onFailure(Call<AppointmentResponseDto> call, Throwable t) {
                        Log.i("OAP", "OnFailure: " + t.getMessage());

                        view.onAppointmentUpdatedError("Ocurrió un error al intentar actualizar la cita: " + t.getMessage());
                    }
                });
            default:
                view.onAppointmentUpdatedError("Error general al actualizar la cita...");
        }
    }
}
