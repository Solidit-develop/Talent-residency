package com.example.servicesolidit.RegisterFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.servicesolidit.Model.Requests.RegisterRequestDto;
import com.example.servicesolidit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class Register extends Fragment{

    private TextInputLayout etName, etLastName, etEmail, etPhone, etAge;
    private TextInputEditText edtName, edtLastname, edtEmail, edtAge, edtPhoneNumber;

    private Button btnSiguiente;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        btnSiguiente = view.findViewById(R.id.btn_siguiente);


        etName = view.findViewById(R.id.txt_name_input_layout);
        edtName = view.findViewById(R.id.edtxt_name);
        etLastName = view.findViewById(R.id.txt_lastname_input_layout);
        edtLastname = view.findViewById(R.id.edtxt_lastname);
        etEmail = view.findViewById(R.id.txt_email_input_layout);
        edtEmail = view.findViewById(R.id.edtxt_email);
        etPhone = view.findViewById(R.id.txt_phone_number_layout);
        edtPhoneNumber = view.findViewById(R.id.edtxt_phone_number);
        etAge = view.findViewById(R.id.txt_age_input_layout);
        edtAge = view.findViewById(R.id.edtxt_age);

        edtPhoneNumber.addTextChangedListener(new TextWatcher() {

            private static final String PHONE_PATTERN = "### ### ####";
            private boolean isUpdating = false;
            private final StringBuilder currentText = new StringBuilder();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    return;
                }

                isUpdating = true;

                // quita cualquier espacio o caracter no numerico;
                String cleanText = s.toString().replaceAll("[^\\d]", "");

                if (cleanText.length() > 10) {
                    cleanText = cleanText.substring(0, 10);
                }

                // Aplicar el fomato ### ### ####
                StringBuilder formattedText = new StringBuilder();
                int length = cleanText.length();
                for (int i = 0; i < length; i++) {
                    formattedText.append(cleanText.charAt(i));
                    if ((i == 2 || i == 5) && i < length - 1) {
                        formattedText.append(" ");
                    }
                }

                currentText.replace(0, currentText.length(), formattedText.toString());
                edtPhoneNumber.setText(currentText.toString());
                edtPhoneNumber.setSelection(currentText.length());  // mueve el cursos al final

                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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