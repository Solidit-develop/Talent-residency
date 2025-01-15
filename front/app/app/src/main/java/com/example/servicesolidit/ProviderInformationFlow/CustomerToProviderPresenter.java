package com.example.servicesolidit.ProviderInformationFlow;

import android.util.Log;

import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Messages.MessagesResponseDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Dtos.Responses.User.ProviderProfileInformationDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProviderProfileResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerToProviderPresenter {
    private CustomerToProviderView view;
    private ApiService service;

    public CustomerToProviderPresenter(CustomerToProviderView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void drawViewToStartConversation(int idOrigen, int idDestino){
        Call<MessagesResponseDto> call = service.getMessages(idOrigen, idDestino);
        call.enqueue(new Callback<MessagesResponseDto>() {
            @Override
            public void onResponse(Call<MessagesResponseDto> call, Response<MessagesResponseDto> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.i("CTPP", "Origen, Destino: " + idOrigen + " " + idDestino);
                    MessagesResponseDto result = response.body();
                    Log.i("CTPP", "Success: " + result.getResultados().size());
                    Log.i("CTPP", "Encontró conversación: " + result.getResultados().size() );
                    view.onPrintStartConversation(result.getResultados().isEmpty(), result.getResultados());
                }else{
                    Log.i("CTPP", "Error no conversación");
                    view.onPrintStartConversationError("Error al consultar las conversaciones");
                }
            }

            @Override
            public void onFailure(Call<MessagesResponseDto> call, Throwable t) {
                    Log.i("CTPP", "Error no conversación: " + t.getMessage());
                    view.onPrintStartConversationError("Error al consultar las conversaciones");
            }
        });
    }

    public void loadProviderInformation(int idProviderToLoad){
        Log.i("CTP", "Trata de buscar id de provider: " + idProviderToLoad);
        Call< ProviderProfileInformationDto> call = service.informationProviderByProviderId(idProviderToLoad);
        call.enqueue(new Callback<ProviderProfileInformationDto>() {
            @Override
            public void onResponse(Call<ProviderProfileInformationDto> call, Response<ProviderProfileInformationDto> response) {
                Gson g = new Gson();
                if(response.isSuccessful() && response.body() != null){
                    Log.i("CTP", "response: " + g.toJson(response.body()));
                    ProviderProfileInformationDto result = response.body();
                    Gson respuesta = new Gson();

                    Log.i("CTP", "response: " + result.getResponse().getUserInfoRelated().getEmail());
                    Log.i("CTP", "aqui existe el user id del que quiere ser el provider: " + respuesta.toJson(result));
                    view.onInforProviderLoaded(result.getResponse());
                }else{
                    view.onInfoProviderError("Ocurrió un error al cargar la información del proveedor");
                }
            }

            @Override
            public void onFailure(Call<ProviderProfileInformationDto> call, Throwable t) {
                view.onInfoProviderError("Ocurrió un error: " + t.getMessage());
            }
        });
    }
}
