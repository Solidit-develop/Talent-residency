package com.example.servicesolidit.RegisterFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.servicesolidit.Model.Requests.RegisterRequestDto;
import com.example.servicesolidit.R;
import com.google.android.material.textfield.TextInputLayout;


public class Register extends Fragment{

    private Button btnSiguiente;
    private TextInputLayout etEmail, etName, etLastName, etPhone, etAge;
    // Nombre, apellidos, telefono, edad,


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        btnSiguiente = view.findViewById(R.id.btn_siguiente);

        etEmail = view.findViewById(R.id.txt_email_input);
        etName = view.findViewById(R.id.txt_name_input);
        etLastName = view.findViewById(R.id.txt_lastname_input);
        etPhone = view.findViewById(R.id.txt_phone_number_input);
        etAge = view.findViewById(R.id.txt_age_input);

        btnSiguiente.setOnClickListener(v -> {
            // Se debe crear el objeto a partir de  para mandarlo al presenter
            String mail = etEmail.getEditText().getText().toString();
            String name = etName.getEditText().getText().toString();
            String lastName = etLastName.getEditText().getText().toString();
            String age = etAge.getEditText().getText().toString();
            String phone = etPhone.getEditText().getText().toString();

            RegisterRequestDto userRequest = new RegisterRequestDto();

            userRequest.setName_user(name);
            userRequest.setLastname(lastName);
            userRequest.setAge(Integer.parseInt(age));
            userRequest.setPhoneNumber(phone);
            userRequest.setEmail(mail);

            navigateToSecondRegistry(userRequest);
        });
        return view;
    }

    public void navigateToSecondRegistry(RegisterRequestDto request) {
        Address registerFragment = new Address(request);
        FragmentTransaction transactionTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transactionTransaction.replace(R.id.fragmentLogin, registerFragment);
        transactionTransaction.addToBackStack(null);
        transactionTransaction.commit();
    }
}