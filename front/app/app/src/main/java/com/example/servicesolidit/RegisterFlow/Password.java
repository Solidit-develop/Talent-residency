package com.example.servicesolidit.RegisterFlow;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.servicesolidit.HomeFlow.Home;
import com.example.servicesolidit.LoginFlow.Login;
import com.example.servicesolidit.Model.Requests.RegisterRequestDto;
import com.example.servicesolidit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Password extends Fragment implements RegisterView {

    private Button btnConfirm;
    private TextInputLayout etPassword;
    private TextInputLayout etConfirmPassword;
    private RegisterRequestDto request;
    private RegisterPresenter presenter;

    public Password(RegisterRequestDto requestDto){
        this.request = requestDto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        etPassword = view.findViewById(R.id.txt_password_input);
        etConfirmPassword = view.findViewById(R.id.txt_confirm_password_input);
        presenter = new RegisterPresenter(this);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getEditText().getText().toString();
                String confirmPassword = etConfirmPassword.getEditText().getText().toString();

                try {
                    isValidaPassword(password, confirmPassword);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                request.setPassword(password);

                showProgress();
                presenter.register(request);

            }
        });

        return view;


    }

    private void isValidaPassword(String password, String confirmPassword) throws Exception {
        if(!password.equals(confirmPassword)){
            // TODO: Manejar el flujo por not match password
            Log.i("PasswordClass", "Alerta por error de contraseñas diferentes");
            throw new Exception("Error por match de password field");
        }
    }

    @Override
    public void showProgress() {
        Log.i("PasswordClass", "Should show progress bar");
    }

    @Override
    public void hideProgress() {
        Log.i("PasswordClass", "Should hide progress bar");
    }

    @Override
    public void onRegisterSuccess(String message) {
        hideProgress();
        Log.i("PasswordClass", "Should show alert with message: " + message);

        Login loginFragment = new Login();
        FragmentTransaction transactionRegister = getParentFragmentManager().beginTransaction();
        transactionRegister.replace(R.id.fragmentLogin, loginFragment);
        transactionRegister.addToBackStack(null);
        transactionRegister.commit();
    }

    @Override
    public void onRegisterError(String message) {
        Log.i("PasswordClass", "Should show alert with error: " + message);
    }
}