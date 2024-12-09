package com.example.servicesolidit.RegisterFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.servicesolidit.Utils.Models.Requests.RegisterRequestDto;
import com.example.servicesolidit.R;
import com.google.android.material.textfield.TextInputEditText;


public class Register extends Fragment{

    private TextInputEditText etName, etLastName, etEmail, etAge;
    private TextInputEditText edtPhoneNumber;

    private Button btnSiguiente;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        btnSiguiente = view.findViewById(R.id.btn_siguiente);
        etName = view.findViewById(R.id.edtxt_name);
        etLastName = view.findViewById(R.id.edtxt_lastname);
        etEmail = view.findViewById(R.id.edtxt_email);
        edtPhoneNumber = view.findViewById(R.id.edtxt_phone_number);
        etAge = view.findViewById(R.id.edtxt_age);

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
            Log.i("RegisterClass", "Entran al dal click el siguiente");
            if (validarCampos()) {
                Log.i("RegisterClass", "Campos validos");
                String mail = etEmail.getText().toString();
                String name = etName.getText().toString();
                String lastName = etLastName.getText().toString();
                String age = etAge.getText().toString();
                String phone = edtPhoneNumber.getText().toString();

                Log.i("RegisterClass", "Campos campos asignados");
                RegisterRequestDto userRequest = new RegisterRequestDto();

                userRequest.setName_user(name);
                userRequest.setLastname(lastName);
                userRequest.setAge(Integer.parseInt(age));
                userRequest.setPhoneNumber(phone);
                userRequest.setEmail(mail);

                navigateToSecondRegistry(userRequest);
            }
        });
        return view;
    }

    /**
     * Método para validar los campos del formulario de forma más compacta
     *
     * @return boolean - true si todos los campos son válidos, false si hay algún error.
     */
    private boolean validarCampos() {
        boolean esValido = true;

        // Validación general de campos vacíos
        esValido &= validarCampoVacio(etName, "El nombre es obligatorio");
        esValido &= validarCampoVacio(etLastName, "El apellido es obligatorio");
        esValido &= validarCampoVacio(edtPhoneNumber, "El número de teléfono es obligatorio");
        esValido &= validarEmail(etEmail, "El correo es obligatorio", "Formato de correo inválido");
        esValido &= validarEdad(etAge, "La edad es obligatoria", "La edad debe ser un número válido");

        return esValido;
    }

    /**
     * Método auxiliar para validar si un campo está vacío
     *
     * @param inputLayout El TextInputLayout que contiene el campo a validar
     * @param mensajeError El mensaje a mostrar si el campo está vacío
     * @return boolean - true si el campo no está vacío, false si está vacío
     */
    private boolean validarCampoVacio(TextInputEditText inputLayout, String mensajeError) {
        String texto = inputLayout.getText().toString().trim();
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

    /**
     * Método auxiliar para validar el campo de correo
     *
     * @param inputLayout El TextInputLayout que contiene el campo a validar
     * @param mensajeErrorVacio El mensaje a mostrar si el campo está vacío
     * @param mensajeErrorFormato El mensaje a mostrar si el correo tiene un formato inválido
     * @return boolean - true si el correo es válido, false en caso contrario
     */
    private boolean validarEmail(TextInputEditText inputLayout, String mensajeErrorVacio, String mensajeErrorFormato) {
        String email = inputLayout.getText().toString().trim();
        if (email.isEmpty()) {
            inputLayout.setError(mensajeErrorVacio);
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputLayout.setError(mensajeErrorFormato);
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }

    /**
     * Método auxiliar para validar el campo de edad
     *
     * @param inputLayout El TextInputLayout que contiene el campo a validar
     * @param mensajeErrorVacio El mensaje a mostrar si el campo está vacío
     * @param mensajeErrorFormato El mensaje a mostrar si la edad no es un número válido
     * @return boolean - true si la edad es válida, false en caso contrario
     */
    private boolean validarEdad(TextInputEditText inputLayout, String mensajeErrorVacio, String mensajeErrorFormato) {
        String edadStr = inputLayout.getText().toString().trim();
        if (edadStr.isEmpty()) {
            inputLayout.setError(mensajeErrorVacio);
            return false;
        } else {
            try {
                int edad = Integer.parseInt(edadStr);
                if (edad <= 0) {
                    inputLayout.setError(mensajeErrorFormato);
                    return false;
                }
                inputLayout.setError(null);
                return true;
            } catch (NumberFormatException e) {
                inputLayout.setError(mensajeErrorFormato);
                return false;
            }
        }
    }

    /**
     * Method to navigate to the secondary register view
     * TODO: Refactor to extract method to utils
     */
    public void navigateToSecondRegistry(RegisterRequestDto request) {
        Log.i("RegisterClass", "Si va a salir");
        Address registerFragment = new Address(request);
        FragmentTransaction transactionTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transactionTransaction.replace(R.id.fragmentLogin, registerFragment);
        transactionTransaction.addToBackStack(null);
        transactionTransaction.commit();
    }
}