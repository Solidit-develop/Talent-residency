package com.example.servicesolidit.ProfileFlow;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.servicesolidit.ProfileFlow.Presenter.ProviderPresenter;
import com.example.servicesolidit.ProfileFlow.View.ProviderView;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;


public class BussinesData extends Fragment implements ProviderView {

    private TextInputLayout tieBussinesName, tieBussinesPhone, tieBussinesExperience, tieBussinesUbication;
    private ProviderPresenter presenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bussines_data, container, false);
        tieBussinesExperience = view.findViewById(R.id.tieBussinesExperience);
        tieBussinesPhone = view.findViewById(R.id.tieBussinesPhone);
        tieBussinesName = view.findViewById(R.id.tieBussinesName);
        tieBussinesUbication = view.findViewById(R.id.tieBussinesUbication);

        this.presenter = new ProviderPresenter(this);
        int idLogged = getLoggedId();
        this.presenter.providerInfomration(idLogged);
        return view;
    }

    public int getLoggedId(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        int userIdLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID, 0);
        Log.i("BussinesDataClass", "IdLogged: " + userIdLogged);
        return  userIdLogged;
    }

    @Override
    public void onLoadProviderInfoSuccess(ProviderResponseDto response) {
        //Full form data
        Gson gson = new Gson();
        Log.i("BussinesDataClass",gson.toJson(response) );
        this.tieBussinesName.getEditText().setText(response.getWorkshopName());
        this.tieBussinesPhone.getEditText().setText(response.getWorkshopPhoneNumber());
        this.tieBussinesExperience.getEditText().setText(response.getExperienceYears());
        this.tieBussinesUbication.getEditText().setText(response.getAddress().getLocalidad());
    }

    @Override
    public void onLoadProviderInfoError(String error) {
        Log.i("BussinesDataClass", error);
    }
}