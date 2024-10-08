package com.example.servicesolidit.RegisterFlow;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.servicesolidit.Model.Requests.RegisterRequestDto;
import com.example.servicesolidit.R;
import com.google.android.material.textfield.TextInputEditText;

public class Address extends Fragment {

    private RegisterRequestDto requestFromRegistry;
    public Address(RegisterRequestDto requestDto){
        this.requestFromRegistry = requestDto;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private Button btnSiguiente;
    private TextInputEditText edtxtZipCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        btnSiguiente = view.findViewById(R.id.btn_siguiente_address);
        edtxtZipCode = view.findViewById(R.id.edtxt_zip_code);

        edtxtZipCode.addTextChangedListener(new TextWatcher() {

            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    return;
                }

                isUpdating = true;

                // Remover cualquier caracter que no sea un número
                String cleanText = s.toString().replaceAll("[^\\d]", "");

                // Limitar la entrada a 5 dígitos
                if (cleanText.length() > 5) {
                    cleanText = cleanText.substring(0, 5);
                }

                edtxtZipCode.setText(cleanText);
                edtxtZipCode.setSelection(cleanText.length());  // Colocar el cursor al final

                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Log.i("AddressClass", "Inicializado: " + requestFromRegistry.getLastname());

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // zipCode
                // state
                // municipio
                // st1
                // st2
                // localidad
                // TODO: Llenar información desde los editText como en registryOne
                requestFromRegistry.setZipcode("43480");
                requestFromRegistry.setName_state("Hidalgo");
                requestFromRegistry.setName_Town("Tenango de Doria");
                requestFromRegistry.setStreet_1("Valle San Miguel Villa");
                requestFromRegistry.setStreet_2("Lago Baykal");
                requestFromRegistry.setLocalidad("Urbi");

                navigateToThirdRegistry(requestFromRegistry);
            }
        });

        return view;
    }

    public void navigateToThirdRegistry(RegisterRequestDto request){
        Password fragmentPassword = new Password(request);
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLogin, fragmentPassword);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}