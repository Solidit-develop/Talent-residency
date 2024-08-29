package com.example.servicesolidit.LoginFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.servicesolidit.R;
import com.example.servicesolidit.RegisterFlow.Register;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends Fragment implements LoginView {

    private Button btnRegister;
    private TextInputEditText etUser;
    private TextInputEditText etPassword;
    private LoginPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        btnRegister = view.findViewById(R.id.btn_register);
        etUser = view.findViewById(R.id.etUser);
        etPassword = view.findViewById(R.id.etPassword);
        presenter = new LoginPresenter(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUser.getText().toString();
                String password = etPassword.getText().toString();
                Toast.makeText(v.getContext(), "Login called with: "+username+" y "+password, Toast.LENGTH_SHORT).show();
                presenter.login(username, password);
            }
        });
        return view;
    }

    @Override
    public void showProgress() {
        Toast.makeText(this.getContext(), "ShowProgressCall", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        Toast.makeText(this.getContext(), "HideProgressCall", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoginSuccess(String message) {
        Toast.makeText(this.getContext(), "OnLoginSuccessCall: " + message, Toast.LENGTH_SHORT).show();

        Register registerFragment = new Register();
        FragmentTransaction transactionRegister = requireActivity().getSupportFragmentManager().beginTransaction();
        transactionRegister.replace(R.id.fragmentLogin, registerFragment);
        transactionRegister.addToBackStack(null);
        transactionRegister.commit();
    }

    @Override
    public void onLoginError(String message) {
        Toast.makeText(this.getContext(), "OnLoginErrorCall: " + message, Toast.LENGTH_SHORT).show();

    }
}