package com.example.servicesolidit.MessageFlow;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.servicesolidit.HouseFlow.CardAdapter;
import com.example.servicesolidit.Model.Responses.Messages.ConversationDto;
import com.example.servicesolidit.Model.Responses.Messages.MessageDto;
import com.example.servicesolidit.R;

import java.util.ArrayList;
import java.util.List;

public class Message extends Fragment implements MessageView{
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<MessageDto> messageList;

    private int idDestino;
    private int idOrigen;

    private MessagePresenter presenter;
    public Message(int origen, int destino){
        this.idOrigen = origen;
        this.idDestino=destino;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_activity, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_messages);

        // Inicializa la lista y el adaptador
        this.messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList, idOrigen);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        this.presenter = new MessagePresenter(this);

        //parametros que mando para la url
        int idOrigen = this.idOrigen; //usuario logueado
        int idDestino = this.idDestino; //usuario destino

        this.onShowProgress();
        this.presenter.loadConversation(idOrigen, idDestino);


        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onConversationLoaded(List<ConversationDto> messages) {
        Log.i("MessageClass", "Conversation loaded: " + messages.size());

        List <MessageDto> messagesDtoList = new ArrayList<>();
        for (int i=0 ; i < messages.size(); i++) {
            List<MessageDto> messagesReadyToDraw = messages.get(i).getMessage();
            for(MessageDto toDraw: messagesReadyToDraw){
               toDraw.setSent(messages.get(i).getInteractuan().getIdDest() == this.idOrigen);
               Log.d("MessageClass", "Content: " + (toDraw.getContect()) + " Es enviado: " + toDraw.isSent());
            }
            messagesDtoList.addAll(messages.get(i).getMessage());
        }


        // Limpia y actualiza la lista sin cambiar la referencia
        this.messageList.clear(); // Limpia la lista actual
        this.messageList.addAll(messagesDtoList); // Añade los nuevos mensajes

        // Notifica al adaptador de los cambios
        adapter.notifyDataSetChanged();

        // Oculta el progreso
        onHideProgress();
    }

    @Override
    public void onErrorConversationLoaded(String messages) {
        Log.i("MessageClass", "Error: " + messages);
    }

    @Override
    public void onShowProgress() {
        Log.i("MessageClass", "Show progress");
    }

    @Override
    public void onHideProgress() {
        Log.i("MessageClass", "Hide progress");
    }
}