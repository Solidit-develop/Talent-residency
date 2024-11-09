package com.example.servicesolidit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class RegisterBussines extends Fragment {

    private Button btnRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_register_bussines, container, false);
        btnRegister = view.findViewById(R.id.btnRegisterBussines);

        btnRegister.setOnClickListener(v->{
            Toast.makeText(requireContext(), "Completa el registro", Toast.LENGTH_SHORT).show();
        });
        return view;
    }
}