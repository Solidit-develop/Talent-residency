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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.servicesolidit.Utils.Models.Requests.SendMessageRequest;
import com.example.servicesolidit.Utils.Models.Responses.Messages.ConversationDto;
import com.example.servicesolidit.Utils.Models.Responses.Messages.MessageDto;
import com.example.servicesolidit.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Message extends Fragment implements MessageView{
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<MessageDto> messageList;
    private Button btnSendMessage;
    private EditText etSendMessage;

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
        Log.i("MessageClass", "Init view");
        View view = inflater.inflate(R.layout.fragment_message_activity, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_messages);
        btnSendMessage = view.findViewById(R.id.btnSendMessage);
        etSendMessage = view.findViewById(R.id.etMessageContent);


        btnSendMessage.setOnClickListener(v->{
            String messageContent = etSendMessage.getText().toString();
            String fechaEspanol = getCurrentDateTime();
            SendMessageRequest request = new SendMessageRequest();
            request.setContentMessage(messageContent);
            request.setCreateDate(String.valueOf(fechaEspanol));
            request.setSendDate(fechaEspanol.toString());
            if(!messageContent.isEmpty()){
                Gson gson = new Gson();
                Log.i("MessageClass", "Send json: " + gson.toJson(request));
                this.presenter.sendMessage(this.idOrigen, this.idDestino, request);
                onShowProgress();
                etSendMessage.setText("");
            }
        });

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
        Log.i("MessageClass", "End init view");
        return view;
    }

    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS'Z'")
                .withZone(ZoneOffset.UTC);
        return formatter.format(Instant.now());
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

    @Override
    public void onMessageSended(String response) {
        this.presenter.loadConversation(this.idOrigen, this.idDestino);
    }

    @Override
    public void onErrorSendMessage(String s) {
        Log.i("MessageClass", "Error al enviar el mensaje: " + s);
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        onHideProgress();
    }
}