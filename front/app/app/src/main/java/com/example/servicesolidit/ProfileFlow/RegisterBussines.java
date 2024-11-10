package com.example.servicesolidit.ProfileFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.servicesolidit.ProfileFlow.Presenter.RegisterPresenter;
import com.example.servicesolidit.ProfileFlow.View.RegisterBussinesView;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Models.Requests.UpdateToProviderRequestDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UpdateUserToProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProfileDto;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class RegisterBussines extends Fragment implements RegisterBussinesView {

    private TextInputLayout tilBussinesName, tilSkills, tilPhoneNumber, tilExperiencia, tilEmailUser;
    private Button btnRegister;
    private UserInfoProfileDto user;
    private RegisterPresenter presenter;
    private ProgressBar progressBar;

    public RegisterBussines(UserInfoProfileDto userLogged){
        this.user = userLogged;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_register_bussines, container, false);
        Objects.requireNonNull(tilEmailUser.getEditText()).setText(this.user.getEmail());
        tilBussinesName = view.findViewById(R.id.tilBussinesNameRegister);
        tilSkills = view.findViewById(R.id.tilBussinesSkillsRegister);
        tilPhoneNumber = view.findViewById(R.id.tilBussinesPhoneRegister);
        tilExperiencia = view.findViewById(R.id.tilBussinesExperienceRegister);
        tilEmailUser = view.findViewById(R.id.tilBussinesEmailRegister);
        progressBar = view.findViewById(R.id.progressItemOnRegisterBussines);
        btnRegister = view.findViewById(R.id.btnRegisterBussines);

        this.presenter = new RegisterPresenter(this);

        btnRegister.setOnClickListener(v->{
            Toast.makeText(requireContext(), "Completa el registro", Toast.LENGTH_SHORT).show();
            showProgress();
            fullFormToUpdateProfile();
        });
        return view;
    }


    public void fullFormToUpdateProfile(){
        if (validateInput()){
            String nameBussines = tilBussinesName.getEditText().getText().toString().trim();
            String skills = tilSkills.getEditText().getText().toString().trim();
            String phoneNumber = tilPhoneNumber.getEditText().getText().toString().trim();
            String experiencia = tilExperiencia.getEditText().getText().toString().trim();
            String emailUserLogged = this.user.getEmail();

            UpdateToProviderRequestDto requestDto = new UpdateToProviderRequestDto();

            requestDto.setWorkshopName(nameBussines);
            requestDto.setSkills(this.getSkills(skills));
            requestDto.setWorkshopPhone(phoneNumber);
            requestDto.setExperience(experiencia);
            requestDto.setEmail(emailUserLogged);
            requestDto.setLocalidad("Localidad");
            requestDto.setNameState("Localidad");
            requestDto.setNameTown("Localidad");
            requestDto.setStr1("Localidad");
            requestDto.setStr2("Localidad");
            requestDto.setZipCode("Localidad");

            this.showProgress();
            this.presenter.convertUserToProvider(requestDto);
        }
    }

    private ArrayList<String> getSkills(String skill) {
        ArrayList<String> skills = new ArrayList<>();
        skills.add(skill);
        return skills;
    }

    private boolean validateInput() {
        boolean isValid = true;
        isValid &= validarCampoVacio(this.tilBussinesName, "Campo requerido");
        isValid &= validarCampoVacio(this.tilSkills, "Campo requerido");
        isValid &= validarCampoVacio(this.tilPhoneNumber, "Campo requerido");
        isValid &= validarCampoVacio(this.tilExperiencia, "Campo requerido");
        isValid &= validarCampoVacio(this.tilEmailUser, "Campo requerido");
        return isValid;
    }

    /**
     * Método auxiliar para validar si un campo está vacío
     *
     * @param inputLayout El TextInputLayout que contiene el campo a validar
     * @param mensajeError El mensaje a mostrar si el campo está vacío
     * @return boolean - true si el campo no está vacío, false si está vacío
     */
    private boolean validarCampoVacio(TextInputLayout inputLayout, String mensajeError) {
        String texto = inputLayout.getEditText().getText().toString().trim();
        Log.i("RegisterClass", "Validar text: " + texto );
        if (texto.isEmpty()) {
            inputLayout.setError(mensajeError);
            Log.i("RegisterClass", mensajeError);
            return false;
        } else {
            inputLayout.setError(null);
            Log.i("RegisterClass", "True");
            return true;
        }
    }

    @Override
    public void showProgress() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRegisterSuccess(UpdateUserToProviderResponseDto responseMessage) {
        Toast.makeText(requireContext(), responseMessage.getResponse(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterFails(String s) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
    }
}