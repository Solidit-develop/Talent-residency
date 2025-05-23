package com.example.servicesolidit.ProviderInformationFlow;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.servicesolidit.MessageFlow.Message;
import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Messages.ConversationDto;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.List;

public class CustomerToProvider extends Fragment implements CustomerToProviderView {

    private int idProviderToLoad;
    private int idUserOfProviderSelected;
    private int idUserLogged;
    private CustomerToProviderPresenter presenter;
    private Button btnGoToMessages;
    private Button btnStartNewConversation;
    private TextInputLayout tilEmailProvider;
    private TextInputLayout tilUbication;

    public CustomerToProvider(int idProviderToLoad) {
        Log.i("CTP", "Recibe en constructor el provider id: " + idProviderToLoad);
        this.idProviderToLoad = idProviderToLoad;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_to_provider_view, container, false);
        // Draw las conversation if exist
        // Draw item to start a conversations if not exist
        btnStartNewConversation = view.findViewById(R.id.btnStartNewMessage);
        btnGoToMessages = view.findViewById(R.id.btnGoToConversation);
        tilEmailProvider = view.findViewById(R.id.tilEmailProvider);
        tilUbication = view.findViewById(R.id.tilLocationInfoProvider);

        this.idUserLogged = getIdUserLogged();
        this.presenter = new CustomerToProviderPresenter(this);

        btnStartNewConversation.setOnClickListener(v->{
            GoToMessage(idUserLogged, this.idUserOfProviderSelected);
        });
        btnGoToMessages.setOnClickListener(v->{
            GoToMessage(idUserLogged, this.idUserOfProviderSelected);
        });

        this.showProgres();
        this.presenter.loadProviderInformation(this.idProviderToLoad);
        return view;
    }

    public int getIdUserLogged(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        int idUserLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID, 0);
        return idUserLogged;
    }

    @Override
    public void showProgres() {
        Log.i("CTP", "ShowProgress");
    }

    @Override
    public void hideProgess() {
        Log.i("CTP", "HideProgress");

    }

    @Override
    public void onPrintStartConversation(boolean isNewConversation, List<ConversationDto> resultados) {
        hideProgess();
        Log.i("CTP", "Deberia pintar el item de iniciar nueva conversación: " + isNewConversation);
        if(isNewConversation){
            btnGoToMessages.setVisibility(View.GONE);
            btnStartNewConversation.setVisibility(View.VISIBLE);
        }else{
            btnGoToMessages.setVisibility(View.VISIBLE);
            btnStartNewConversation.setVisibility(View.GONE);
        }

        if(this.idUserOfProviderSelected == this.idUserLogged){
            this.btnGoToMessages.setEnabled(false);
            this.btnStartNewConversation.setEnabled(false);
        }
    }

    public void GoToMessage (int origen, int destino){
        Message message = new Message(origen, destino);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, message);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onPrintStartConversationError(String error) {
        Log.i("CTP", "Error: " + error);
    }

    @Override
    public void onInforProviderLoaded(ProviderResponseDto response) {
        if(response.getUserInfoRelated().getEmail() != null){
            Gson g = new Gson();
            Log.i("CTP", g.toJson(response));
            this.tilEmailProvider.getEditText().setText(response.getUserInfoRelated().getEmail());
            this.idUserOfProviderSelected = response.getUserInfoRelated().getIdUser();
            String location = response.getAddress().getTown().getNameTown() + " " + response.getAddress().getLocalidad();
            this.tilUbication.getEditText().setText(location);
            Log.i("CTP", "id Provider: " + this.idProviderToLoad);
            Log.i("CTP", "id user of Provider: " + this.idUserOfProviderSelected);
            Log.i("CTP", "id user logged: " + this.idUserLogged);

            this.presenter.drawViewToStartConversation(idUserLogged, this.idUserOfProviderSelected);

        }else{
            Log.i("CTP", "OCurrió un error");
        }
    }

    @Override
    public void onInfoProviderError(String s) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
    }
}

