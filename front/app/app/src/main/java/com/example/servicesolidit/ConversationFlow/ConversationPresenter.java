package com.example.servicesolidit.ConversationFlow;

import android.util.Log;

import com.example.servicesolidit.Utils.Dtos.Responses.Conversatoins.ConversationResponse;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.google.gson.Gson;

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
        Call<ConversationResponse> call = service.getConversations(idLogged);
        call.enqueue(new Callback<ConversationResponse>() {
            @Override
            public void onResponse(Call<ConversationResponse> call, Response<ConversationResponse> response) {
                ConversationResponse result = response.body();
                Gson gson = new Gson();
                Log.i("ConversationPresenter", "Conversation loaded: " + gson.toJson(result));
                view.onConversationSucess(result.getResponse());
            }

            @Override
            public void onFailure(Call<ConversationResponse> call, Throwable t) {
                view.onConversationFail(t.getMessage());
            }
        });
    }
}
