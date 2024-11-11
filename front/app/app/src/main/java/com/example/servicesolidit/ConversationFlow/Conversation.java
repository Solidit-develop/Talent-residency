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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicesolidit.MessageFlow.Message;
import com.example.servicesolidit.Utils.Models.Responses.Conversatoins.ConversationDto;
import com.example.servicesolidit.Utils.Models.Responses.Conversatoins.ConversationResponseDto;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class Conversation extends Fragment implements AdapterConversation.OnConversationClickListener, ConversationView{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noConversationView;
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
        progressBar = view.findViewById(R.id.conversationProgressBar);
        noConversationView = view.findViewById(R.id.noConversationView);
        conversations = new ArrayList<>();
        adapter = new AdapterConversation(conversations, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        presenter = new ConversationPresenter(this);
        this.showProgress();
        this.idLogged = getLoggedId();
        this.presenter.getConversations(idLogged);
        return view;
    }

    public int getLoggedId(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        int userIdLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID, 0);
        Log.i("ConversationClass", "IdLogged: " + userIdLogged);
        return  userIdLogged;
    }


    @Override
    public void onConversationClick(String relatedId) {
        Message messageView = new Message(this.idLogged, Integer.parseInt(relatedId));
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, messageView);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onConversationSucess(List<ConversationResponseDto> response) {
        if(!response.isEmpty()) {
            Log.i("ConversationClass", "Response: " + response.get(0).getMessages().get(0).getContent());
            noConversationView.setVisibility(View.GONE);
            List<ConversationDto> conversationList = new ArrayList<>();
            for (int i = 0; i < response.size(); i++) {
                ConversationResponseDto conver = response.get(i);
                ConversationDto converResult = new ConversationDto(
                        String.valueOf(conver.getInteract().getDestinationId()),
                        conver.getInteract().getName(),
                        "profileImageUrl",
                        conver.getMessages().get(0).getContent(),
                        conver.getMessages().get(0).getSendDate(),
                        String.valueOf(conver.getIdRelated())
                );
                conversationList.add(converResult);
            }
            this.conversations.clear();
            this.conversations.addAll(conversationList);

            adapter.notifyDataSetChanged();
        }else{
            //Load empty conversations view
            noConversationView.setVisibility(View.VISIBLE);
            Log.i("ConversationClass", "Load not conversation view");
        }

        this.hideProgress();
    }

    @Override
    public void onConversationFail(String error) {
        hideProgress();
        Toast.makeText(requireContext(), "Ocurrió un error al cargar la conversación", Toast.LENGTH_SHORT).show();
        Log.i("ConversationClass", "Error: " + error);
    }
}