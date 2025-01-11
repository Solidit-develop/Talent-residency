package com.example.servicesolidit.ResetPassword;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Dtos.Requests.ResetPasswordRequest;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentItemResponse;
import com.example.servicesolidit.Utils.Dtos.Responses.GenerarMessageResponseDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordPresenter {
    private ResetPasswordView view;
    private ApiService service;

    public ResetPasswordPresenter(ResetPasswordView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void resetPassword(String emailTxt) {
        Call<GenerarMessageResponseDto> call = this.service.resetPassword(new ResetPasswordRequest(emailTxt));
        call.enqueue(new Callback<GenerarMessageResponseDto>() {
            @Override
            public void onResponse(Call<GenerarMessageResponseDto> call, Response<GenerarMessageResponseDto> response) {
                if(response.isSuccessful() && response.body()!=null){
                    GenerarMessageResponseDto result = response.body();
                    view.onSuccessReset(result.getMessage());
                }else{
                    view.onErrorReset("Ocurrió un error al reestablecer su contraseña");
                }
            }

            @Override
            public void onFailure(Call<GenerarMessageResponseDto> call, Throwable t) {
                view.onErrorReset("Ocurrió un error: " + t.getMessage());
            }
        });
    }
}
