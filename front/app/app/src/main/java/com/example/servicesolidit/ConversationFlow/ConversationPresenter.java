package com.example.servicesolidit.ConversationFlow;

import android.util.Log;

import com.example.servicesolidit.Utils.Models.Responses.Conversatoins.ConversationResponseDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationPresenter {
    private ConversationView view;
    private ApiService service;

    public ConversationPresenter(ConversationView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getConversations(int idLogged){
        Log.i("ConversationPresenter", "Trata de consultar las conversaciones de " + idLogged);
        Call<List<ConversationResponseDto>> call = service.getConversations(idLogged);
        call.enqueue(new Callback<List<ConversationResponseDto>>() {
            @Override
            public void onResponse(Call<List<ConversationResponseDto>> call, Response<List<ConversationResponseDto>> response) {
                List<ConversationResponseDto> result = response.body();
                Gson gson = new Gson();
                Log.i("ConversationPresenter", "Conversation loaded: " + gson.toJson(result));
                view.onConversationSucess(result);
            }

            @Override
            public void onFailure(Call<List<ConversationResponseDto>> call, Throwable t) {
                view.onConversationFail(t.getMessage());
            }
        });
    }
}
