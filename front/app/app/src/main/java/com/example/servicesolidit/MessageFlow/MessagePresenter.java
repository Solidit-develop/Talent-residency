package com.example.servicesolidit.MessageFlow;

import android.util.Log;
import android.widget.Toast;

import com.example.servicesolidit.Model.Responses.Messages.ConversationDto;
import com.example.servicesolidit.Model.Responses.Messages.MessageDto;
import com.example.servicesolidit.Model.Responses.Messages.MessagesResponseDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.google.gson.JsonSerializer;

import java.util.ArrayList;
import java.util.List;

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
                Log.d("MessagePresenter", "Response: " + call);
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
}
