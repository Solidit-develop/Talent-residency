package com.example.servicesolidit.ProfileFlow;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicesolidit.FeedFlow.House;
import com.example.servicesolidit.ProfileFlow.Presenter.ProfilePresenter;
import com.example.servicesolidit.ProfileFlow.View.ProfileView;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment implements ProfileView {

    private Button btnCustomer;
    private Button btnProvider;
    private MaterialButtonToggleGroup buttonToggleGroup;
    private ProgressBar itemLoad;
    private TextView nameProfileHeader;
    private boolean isProvider;
    private UserInfoProfileDto userProfileLoaded;
    private PersonalData personalData;
    private final BussinesData bussinesData = new BussinesData();
    private final Map<Integer, Runnable> navigationAction = new HashMap<>();

    private ProfilePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        isProvider = false;
        btnCustomer = view.findViewById(R.id.btn_customer);
        btnProvider = view.findViewById(R.id.btn_provider);
        buttonToggleGroup = view.findViewById(R.id.toggleButton);
        itemLoad = view.findViewById(R.id.load_item_profile);
        nameProfileHeader = view.findViewById(R.id.txt_name_profile);
        userProfileLoaded = new UserInfoProfileDto();

        buttonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked){
                Log.i("ProfileClass", "Cuando se llama el isCheckedMai");
                checkButtonData(isChecked, checkedId, this.isProvider);
            }
        });

        presenter = new ProfilePresenter(this);

        /* Load Personal Data */
        presenter.information(getIdLogged());
        showProgress();
        return view;
    }

    public int getIdLogged(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        int userIdLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID, 0);
        Log.i("ProfileClass", "IdLogged: " + userIdLogged);
        return userIdLogged;
    }

    public void initPeronsalData(UserInfoProfileDto user){
        this.userProfileLoaded = user;
        this.personalData = new PersonalData(user);
        this.nameProfileHeader.setText("Bienvenido "+ user.getNameUser());
    }
  
    public void checkButtonData (boolean isChecked,int checkedId, boolean isProvider){
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        if (checkedId == R.id.btn_customer){
            transaction.replace(R.id.fragment_container,personalData);
            transaction.addToBackStack(null);
            transaction.remove(bussinesData);
            transaction.commit();
        } if (checkedId == R.id.btn_provider){
            Log.i("ProfileClass", "IsProviderValue: " + isProvider);
            if (isProvider){
                Log.i("ProfileClass", "Flow to show provider data");
                BussinesData bussinesData = new BussinesData();
                transaction.replace(R.id.fragment_container,bussinesData);
            }else{
                Log.i("ProfileClass", "Flow to convert into provider");
                Gson gson = new Gson();
                Log.i("ProfileClass", "Se obtiene: " + gson.toJson(gson));
                RegisterBussines registerBussines = new RegisterBussines(this.userProfileLoaded);
                transaction.replace(R.id.fragment_container,registerBussines);
            }
            transaction.addToBackStack(null);
            transaction.remove(personalData);
            transaction.commit();

        }
    }

    @Override
    public void showProgress() {
        itemLoad.setVisibility(View.VISIBLE);
        Log.i("ProfileClass", "ShowProgress");
    }

    @Override
    public void hideProgress() {
        itemLoad.setVisibility(View.GONE);
        Log.i("ProfileClass", "HideProgress");
    }

    @Override
    public void onLoadProfileSuccess(UserInfoProfileDto message) {
        hideProgress();
        if(message == null){
            Toast.makeText(requireContext(), "Hubo un problema al recuperar la información", Toast.LENGTH_SHORT).show();
            House houseFragment = new House();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container,houseFragment);
            transaction.addToBackStack(null);
            transaction.remove(bussinesData);
            transaction.commit();
        }else{
            this.isProvider = message.getTypes().isValue();
            Log.i("ProfileClass", "Value obtained: " + isProvider);
            initPeronsalData(message);
            Log.i("ProfileClass", "Aqui validamos que pintar de los botones de profile");
            Log.i("ProfileClass", "Información: " + message.getIdUser());
            // TODO: Como validar mi tipo para pintar la vista segun user
            if (isProvider){
                btnProvider.setText("DATOS DE NEGOCIO");
            }else{
                btnProvider.setText("SER VENDENDOR");
            }

        }
        checkButtonData(true, R.id.btn_customer, isProvider);

    }

    @Override
    public void onLoadProfileError(String message) {
        hideProgress();
        Log.i("ProfileClass", "ErrorOnLoadProfileError: " + message);
        Toast.makeText(requireContext(), "Hubo un problema al recuperar la información", Toast.LENGTH_SHORT).show();
        House houseFragment = new House();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,houseFragment);
        transaction.addToBackStack(null);
        transaction.remove(bussinesData);
        transaction.commit();
    }
}