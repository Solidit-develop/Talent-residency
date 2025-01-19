package com.example.servicesolidit.ProfileFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;
import com.example.servicesolidit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class PersonalData extends Fragment {
    private TextView txtPrueba;
    private UserInfoProfileDto personalUserInfo;
    private TextInputEditText name;
    private TextInputEditText lastname;
    private TextInputEditText phone;
    private TextInputEditText email;
    private TextInputEditText town;
    private TextInputEditText city;

    public PersonalData(UserInfoProfileDto peronsalUserInfo){
        this.personalUserInfo = peronsalUserInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_data, container, false);

        Gson f = new Gson();
        Log.i("PersonalDataClass", "User info correct: "+ f.toJson(personalUserInfo));
        name = view.findViewById(R.id.tiet_name);
        lastname = view.findViewById(R.id.tiet_lastname);
        phone = view.findViewById(R.id.tiet_phoneNumber);
        email = view.findViewById(R.id.tiet_email);
        city = view.findViewById(R.id.tiet_city);
        setUserData();

        return view;
    }


    private void setUserData() {
        name.setText(personalUserInfo.getNameUser());
        lastname.setText(personalUserInfo.getLastname());
        phone.setText(personalUserInfo.getPhoneNumber());
        email.setText(personalUserInfo.getEmail());
        String location = personalUserInfo.getIdAddress().getLocalidad() + personalUserInfo.getIdAddress().getTown().getNameTown();
        city.setText(location);
    }
}