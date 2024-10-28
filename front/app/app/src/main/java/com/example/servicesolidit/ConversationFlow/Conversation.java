package com.example.servicesolidit.ConversationFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.servicesolidit.MessageFlow.Message;
import com.example.servicesolidit.Model.Responses.Conversatoins.ConversationDto;
import com.example.servicesolidit.R;

import java.util.ArrayList;
import java.util.List;

public class Conversation extends Fragment implements AdapterConversation.OnConversationClickListener{

    private RecyclerView recyclerView;
    private List<ConversationDto> conversations;

    public Conversation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewConversations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lista de conversaciones de ejemplo
        conversations = new ArrayList<>();
        conversations.add(new ConversationDto("1", "Usuario 1", "https://example.com/image1.jpg", "Último mensaje 1", "12:30 PM"));
        conversations.add(new ConversationDto("2", "Usuario 2", "https://example.com/image2.jpg", "Último mensaje 2", "12:45 PM"));
        // Añade más conversaciones...

        AdapterConversation adapter = new AdapterConversation(conversations, this);
        recyclerView.setAdapter(adapter);

        return view;
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
}