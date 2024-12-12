package com.example.servicesolidit.MessageFlow;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProfileDto;
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

    private int idProviderAsProvider;
    private int idDestino;
    private int idOrigen;

    private MessagePresenter presenter;

    // Fix: from ID DESTINO find id as provider in a new call from here and delete from constructor
    public Message(int origen, int idProviderAsUser){
        this.idOrigen = origen;
        this.idDestino=idProviderAsUser;
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
            navigateToAppointmentFlow(this.idOrigen, this.idProviderAsProvider);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showViewToCreateAppointment(this.idOrigen);

    }

    private void showViewToCreateAppointment(int idOrigen) {
        Log.i("Message", "Sevalidará mostrar el button para el id: " + idOrigen);
        this.presenter.validateIfImProvider(idOrigen);
        onShowProgress();
    }

    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS'Z'")
                .withZone(ZoneOffset.UTC);
        return formatter.format(Instant.now());
    }

    /**
     * Inicio el flow para generar un appointment
     * @param idOrigen used from idLogged
     * @param idDestino used from provider selected
     */
    public void navigateToAppointmentFlow(int idOrigen,int idDestino){
        Appointment createAppointment = new Appointment(idOrigen, idDestino);
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
        Gson fs = new Gson();
        Log.i("MessageClass", "OnLoadProviderInfo" + fs.toJson(result));
        if(result.getUserInfoRelated().getIdUser()==0){
            // load information as user
            this.presenter.loadCustomerInformation(this.idOrigen);
        }else{
            this.tvNameRelatedOnConversation.setText(result.getWorkshopName());
            this.idProviderAsProvider = result.getIdProvidersss();
        }
        onHideProgress();
    }

    @Override
    public void onLoadProviderInfoError(String message) {
        Log.i("MessageClass", "Ocurrió un error al load providerInformation: " + message);
        Toast.makeText(requireContext(), "Ocurrió un error: "+ message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessShowViewToCreateAppointment(ProviderResponseDto result) {
        onHideProgress();
        Gson gso = new Gson();
        Log.i("Message", gso.toJson(result));
        if(result.getUserInfoRelated().getIdUser() == 0){
            btnGoToCreateAppointmentFlow.setVisibility(TextView.GONE);
        }else {
            btnGoToCreateAppointmentFlow.setVisibility(TextView.VISIBLE);
        }
    }

    @Override
    public void onErrorShowViewToCreateAppointment(String message) {
        onHideProgress();
    }

    @Override
    public void onLoadInfoCustomerSuccess(UserInfoProfileDto result) {
        this.tvNameRelatedOnConversation.setText(result.getNameUser());
        this.idProviderAsProvider = 0;
    }

    @Override
    public void onLoadInfoCustomerError(String message) {
        Log.i("MessageClass", "Error: " + message);
    }
}