package com.example.servicesolidit.MessageFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.servicesolidit.HouseFlow.CardAdapter;
import com.example.servicesolidit.Model.Responses.Messages.ConversationDto;
import com.example.servicesolidit.Model.Responses.Messages.MessageDto;
import com.example.servicesolidit.Model.Responses.Messages.MessagesResponseDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends Fragment {
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<MessageDto> messageList = new ArrayList<>();
    private CardAdapter cardAdapter;

    private CardAdapter adapterCard;
    private int idDestino;
    private int idOrigen;

    public MessageActivity(int origen, int destino){
        this.idOrigen = origen;
        this.idDestino=destino;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_activity, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //parametros que mando para la url
        int idOrigen = this.idOrigen; //usuario logueado
        int idDestino = this.idDestino; //usuario destino


        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<MessagesResponseDto> call = apiService.getMessages(idOrigen,idDestino);

        call.enqueue(new Callback<MessagesResponseDto>() {
            @Override
            public void onResponse(Call<MessagesResponseDto> call, Response<MessagesResponseDto> response) {
                if (response.isSuccessful() && response.body() != null){
                    for (ConversationDto resultado : response.body().getResultados()) {
                        messageList.addAll(resultado.getMessage());
                        Log.d("TAG", "PRUEBA: " + call);
                    }

                    adapter = new MessageAdapter(messageList, idOrigen);
                    recyclerView.setAdapter(adapter);

                }else {
                    //manejo del error
                    Toast.makeText(getContext(), "EXCEPTION: " + response.body().getResultados(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessagesResponseDto> call, Throwable t) {
                //manejo de errores
                Toast.makeText(getContext(), "EXCEPTION: " + t, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}