package com.example.servicesolidit.MessageFlow;

import android.util.Log;

import com.example.servicesolidit.Utils.Models.Requests.SendMessageRequest;
import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.Messages.MessagesResponseDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Models.Responses.Messages.SendMessageResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProviderProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagePresenter {
    private MessageView view;
    private ApiService service;

    public MessagePresenter(MessageView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }
    public void loadConversation(int idOrigen, int idDestino){
        Call<MessagesResponseDto> call = service.getMessages(idOrigen,idDestino);
        call.enqueue(new Callback<MessagesResponseDto>() {
            @Override
            public void onResponse(Call<MessagesResponseDto> call, Response<MessagesResponseDto> response) {
                Log.d("MessagePresenter", "Response: " + response);
                if (response.isSuccessful() && response.body() != null){
                    Log.d("MessagePresenter", "ResponseBody: " + response.body().getResultados());
                    view.onConversationLoaded(response.body().getResultados());
                }else {
                    //manejo del error
                    Log.i("MessagePresenter", "Error on Response: " + response.body());
                    view.onErrorConversationLoaded("Error al cargar la conversación");
                }
            }

            @Override
            public void onFailure(Call<MessagesResponseDto> call, Throwable t) {
                Log.i("MessagePresenter", "Error on Response: " + t.getMessage());
                view.onErrorConversationLoaded("Error al cargar la conversación");
            }
        });
    }

    public void sendMessage(int idOrigen, int idDestino, SendMessageRequest message){
        Call<SendMessageResponseDto> call = service.sendMessages(idOrigen, idDestino, message);

        call.enqueue(new Callback<SendMessageResponseDto>() {
            @Override
            public void onResponse(Call<SendMessageResponseDto> call, Response<SendMessageResponseDto> response) {
                Log.d("MessagePresenter", "Response: "+response);
                if(response.isSuccessful() && response.body()!=null){
                    SendMessageResponseDto result = response.body();
                    Log.i("MessagePresenter", result.getResponse());
                    view.onMessageSended(result.getResponse());
                }else{
                    view.onErrorSendMessage("Ocurrió un error al enviar tu mensaje");
                }
            }

            @Override
            public void onFailure(Call<SendMessageResponseDto> call, Throwable t) {
                view.onErrorSendMessage("Ocurrió un error al enviar tu mensaje: " + t.getMessage());
            }
        });
    }

    public void providerInfomration(int id) {
        Log.i("MessagePresenter", "Se buscará al provider tomando información como user con id: " + id);
        Call<UserInfoProviderProfileResponse> call = service.informationProviderByUserId(id);
        call.enqueue(new Callback<UserInfoProviderProfileResponse>() {
            @Override
            public void onResponse(Call<UserInfoProviderProfileResponse> call, Response<UserInfoProviderProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        ProviderResponseDto result = response.body().getResponse();
                        Log.i("MessagePresenter", "result: " + result.getUserInfoRelated());
                        view.onLoadProviderInfoSuccess(result);
                    } catch (Exception e) {
                        view.onLoadProviderInfoError(e.getMessage());
                    }
                } else {
                    view.onLoadProviderInfoError("Ocurrió un error al obtener la información de tu negocio");
                }
            }

            @Override
            public void onFailure(Call<UserInfoProviderProfileResponse> call, Throwable t) {
                view.onLoadProviderInfoError(t.getMessage());
            }
        });

    }
}
