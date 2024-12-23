package com.example.servicesolidit.ProfileFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.servicesolidit.FeedFlow.House;
import com.example.servicesolidit.ProfileFlow.Presenter.RegisterPresenter;
import com.example.servicesolidit.ProfileFlow.View.RegisterBussinesView;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Models.Requests.UpdateToProviderRequestDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProfileDto;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class RegisterBussines extends Fragment implements RegisterBussinesView {

    private TextInputLayout tilBussinesName, tilSkills, tilPhoneNumber, tilExperiencia, tilEmailUser;
    private Button btnRegister;
    private UserInfoProfileDto user;
    private RegisterPresenter presenter;
    private ProgressBar progressBar;

    public RegisterBussines(UserInfoProfileDto userLogged){
        this.user = userLogged;
        Log.i("RegisterBussines", "UserLogged: " + userLogged.getNameUser());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_register_bussines, container, false);
        tilBussinesName = view.findViewById(R.id.tilBussinesNameRegister);
        tilSkills = view.findViewById(R.id.tilBussinesSkillsRegister);
        tilPhoneNumber = view.findViewById(R.id.tilBussinesPhoneRegister);
        tilExperiencia = view.findViewById(R.id.tilBussinesExperienceRegister);
        tilEmailUser = view.findViewById(R.id.tilBussinesEmailRegister);
        progressBar = view.findViewById(R.id.progressItemOnRegisterBussines);
        btnRegister = view.findViewById(R.id.btnRegisterBussines);

        this.presenter = new RegisterPresenter(this);
        tilEmailUser.getEditText().setText(this.user.getEmail());

        btnRegister.setOnClickListener(v->{
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
            requestDto.setDescription("DescriptionField");
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
        if (texto.isEmpty()) {
            inputLayout.setError(mensajeError);
            return false;
        } else {
            inputLayout.setError(null);
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
    public void onRegisterSuccess(String responseMessage) {
        Toast.makeText(requireContext(), responseMessage, Toast.LENGTH_SHORT).show();
        hideProgress();
        House houseFragment = new House();
        navigateTo(houseFragment);
    }

    @Override
    public void onRegisterFails(String s) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to load a new fragment from the slide menu.
     * @param fragmentDestiny is the fragment to navigate.
     */
    public void navigateTo(Fragment fragmentDestiny) {
        Log.i("HomeClass", "Start slide transaction fragment");
        FragmentTransaction transactionTransaction = this.requireActivity().getSupportFragmentManager().beginTransaction();
        transactionTransaction.replace(R.id.frame_container, fragmentDestiny);
        transactionTransaction.addToBackStack(null);
        transactionTransaction.commit();
    }
}