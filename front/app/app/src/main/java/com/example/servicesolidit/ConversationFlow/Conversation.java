package com.example.servicesolidit.ConversationFlow;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.servicesolidit.MessageFlow.Message;
import com.example.servicesolidit.Model.Responses.Conversatoins.ConversationDto;
import com.example.servicesolidit.Model.Responses.Conversatoins.ConversationResponseDto;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class Conversation extends Fragment implements AdapterConversation.OnConversationClickListener, ConversationView{

    private RecyclerView recyclerView;
    private List<ConversationDto> conversations;
    private ConversationPresenter presenter;
    private AdapterConversation adapter;
    private int idLogged;
    public Conversation() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewConversations);

        // Lista de conversaciones de ejemplo
        conversations = new ArrayList<>();
        adapter = new AdapterConversation(conversations, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Añade más conversaciones...
        presenter = new ConversationPresenter(this);
        this.showProgress();
        this.idLogged = getLoggedId();
        this.presenter.getConversations(idLogged);
        return view;
    }

    public int getLoggedId(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        int userIdLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID, 0);
        Log.i("ConversationClass", "IdLogged: " + userIdLogged);
        return  userIdLogged;
    }


    @Override
    public void onConversationClick(String conversationId) {
        // Cambia al fragmento de conversación usando el id
        Toast.makeText(requireContext(), "Funcionalidad pendiente", Toast.LENGTH_SHORT).show();
        /*int idOrigen = 1;
        int idDestino = 2;
        Message messageView = new Message(idOrigen, idDestino);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content_visit_provider, messageView);
        transaction.addToBackStack(null);
        transaction.commit();*/
    }

    @Override
    public void showProgress() {
        Log.i("ConversationClass", "ShowProgress");
    }

    @Override
    public void hideProgress() {
        Log.i("ConversationClass", "HideProgress");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onConversationSucess(List<ConversationResponseDto> response) {
        Log.i("ConversationClass", "Response: " + response.get(0).getMessages().get(0).getContent());
        List<ConversationDto> conversationList = new ArrayList<>();
        for (int i=0; i<response.size(); i++)  {
            ConversationResponseDto conver = response.get(i);
            ConversationDto converResult = new ConversationDto(
                    String.valueOf(conver.getInteract().getDestinationId()),
                    conver.getInteract().getName(),
                    "profileImageUrl",
                    conver.getMessages().get(0).getContent(),
                    conver.getMessages().get(0).getSendDate()
                    );
            conversationList.add(converResult);
        }
        this.conversations.clear();
        this.conversations.addAll(conversationList);

        adapter.notifyDataSetChanged();

        this.hideProgress();
    }

    @Override
    public void onConversationFail(String error) {
        Log.i("ConversationClass", "Error: " + error);
    }
}