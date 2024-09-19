package com.example.servicesolidit.RegisterFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.servicesolidit.Address;
import com.example.servicesolidit.Model.Dtos.UserRegisterModel;
import com.example.servicesolidit.Model.Requests.RegisterRequestDto;
import com.example.servicesolidit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class Register extends Fragment implements RegisterView{

    private Button btnSiguiente;
    private RegisterPresenter presenter;
    private TextInputLayout etEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        btnSiguiente = view.findViewById(R.id.btn_siguiente);
        etEmail = view.findViewById(R.id.txt_email_input);
        presenter = new RegisterPresenter(this);
        btnSiguiente.setOnClickListener(v -> {
            // Se debe crear el objeto a partir de  para mandarlo al presenter
            String mail = etEmail.getEditText().getText().toString();
            RegisterRequestDto userRequest = new RegisterRequestDto(
                    "Jach",
                    "Sanches Altamirano",
                    mail,
                    "Hola1234",
                    24,
                    "7712276783",
                    "calle 3",
                    "calle 4",
                    "San Pedro Cholula",
                    "Zavaleta",
                    "Puebla",
                    "739283"
            );
            presenter.register(userRequest);
            showProgress();
        });
        return view;
    }

    @Override
    public void showProgress() {
        Toast.makeText(this.getContext(), "ShowProgress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        Toast.makeText(this.getContext(), "HideProgress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterSuccess(String message) {
        hideProgress();
        Toast.makeText(this.getContext(), "RegisterSuccess: "+ message, Toast.LENGTH_SHORT).show();

        Address registerFragment = new Address();
        FragmentTransaction transactionTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transactionTransaction.replace(R.id.fragmentLogin, registerFragment);
        transactionTransaction.addToBackStack(null);
        transactionTransaction.commit();
    }

    @Override
    public void onRegisterError(String message) {
        Toast.makeText(this.getContext(), "Error message: "+message, Toast.LENGTH_SHORT).show();
    }
}