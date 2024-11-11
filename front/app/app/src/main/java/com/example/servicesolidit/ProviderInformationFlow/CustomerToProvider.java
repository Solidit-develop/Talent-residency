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
import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.Messages.ConversationDto;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class CustomerToProvider extends Fragment implements CustomerToProviderView {

    private int idProviderToLoad;
    private int idUserLogged;
    private CustomerToProviderPresenter presenter;
    private Button btnGoToMessages;
    private Button btnStartNewConversation;
    private TextInputLayout tilEmailProvider;

    public CustomerToProvider(int idProviderToLoad) {
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

        this.idUserLogged = getIdUserLogged();
        this.presenter = new CustomerToProviderPresenter(this);

        btnStartNewConversation.setOnClickListener(v->{
            GoToMessage(idUserLogged, idProviderToLoad);
        });
        btnGoToMessages.setOnClickListener(v->{
            GoToMessage(idUserLogged, idProviderToLoad);
        });

        this.showProgres();
        this.presenter.drawViewToStartConversation(idUserLogged, idProviderToLoad);
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
            this.tilEmailProvider.getEditText().setText(response.getUserInfoRelated().getEmail());
        }else{
            Log.i("CTP", "OCurrió un error");
        }
    }

    @Override
    public void onInfoProviderError(String s) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
    }
}

