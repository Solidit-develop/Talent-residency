package com.example.servicesolidit.RegisterFlow;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.servicesolidit.Model.Requests.RegisterRequestDto;
import com.example.servicesolidit.R;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        btnSiguiente = view.findViewById(R.id.btn_siguiente_address);

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