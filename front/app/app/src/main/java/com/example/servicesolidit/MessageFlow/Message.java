package com.example.servicesolidit.MessageFlow;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicesolidit.ApointmentFlow.Appointment;
import com.example.servicesolidit.Utils.Models.Requests.SendMessageRequest;
import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.Messages.ConversationDto;
import com.example.servicesolidit.Utils.Models.Responses.Messages.MessageDto;
import com.example.servicesolidit.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Message extends Fragment implements MessageView{
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<MessageDto> messageList;
    private Button btnSendMessage;
    private EditText etSendMessage;
    private TextView tvNameRelatedOnConversation;
    private ImageView btnGoToCreateAppointmentFlow;

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
        tvNameRelatedOnConversation = view.findViewById(R.id.tvNameRelatedOnConversation);
        btnGoToCreateAppointmentFlow = view.findViewById(R.id.btnGoToCreateAppointmentFlow);

        btnGoToCreateAppointmentFlow.setOnClickListener(v->{
            navigateToAppointmentFlow(this.idOrigen);
        });

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
        this.presenter.providerInfomration(idDestino);
        Log.i("MessageClass", "End init view");
        return view;
    }

    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS'Z'")
                .withZone(ZoneOffset.UTC);
        return formatter.format(Instant.now());
    }

    public void navigateToAppointmentFlow(int idOrigen){
        Appointment createAppointment = new Appointment(idOrigen);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, createAppointment);
        transaction.addToBackStack(null);
        transaction.commit();
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


        messagesDtoList = ordenarPorFecha(messagesDtoList);

        // Limpia y actualiza la lista sin cambiar la referencia
        this.messageList.clear(); // Limpia la lista actual


        this.messageList.addAll(messagesDtoList); // Añade los nuevos mensajes

        // Notifica al adaptador de los cambios
        adapter.notifyDataSetChanged();

        // Oculta el progreso
        onHideProgress();
    }

    public static List<MessageDto> ordenarPorFecha(List<MessageDto> mensajes) {
        Gson gson = new Gson();
        Log.i("MessageClass", "Lista a ordenar: " + gson.toJson(mensajes));
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Collections.sort(mensajes, new Comparator<MessageDto>() {
                @Override
                public int compare(MessageDto m1, MessageDto m2) {
                    LocalDateTime fecha1 = LocalDateTime.parse(m1.getSenddate(), formatter);
                    LocalDateTime fecha2 = LocalDateTime.parse(m2.getSenddate(), formatter);
                    return fecha1.compareTo(fecha2); // Orden ascendente: el más antiguo al principio
                }
            });
        }catch (Exception e){
            Log.i("MessageClass", "Ocurrió un error al reordenar: " + e.getMessage());
        }

        return mensajes;
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

    @Override
    public void onLoadProviderInfoSuccess(ProviderResponseDto result) {
        this.tvNameRelatedOnConversation.setText(result.getWorkshopName());
        onHideProgress();
    }

    @Override
    public void onLoadProviderInfoError(String message) {
        Toast.makeText(requireContext(), "Ocurrió un error: "+ message, Toast.LENGTH_SHORT).show();
    }
}