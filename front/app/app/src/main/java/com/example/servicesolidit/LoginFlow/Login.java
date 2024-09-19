package com.example.servicesolidit.LoginFlow;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicesolidit.HomeFlow.Home;
import com.example.servicesolidit.Model.Responses.UserInfoDto;
import com.example.servicesolidit.R;
import com.example.servicesolidit.RegisterFlow.Register;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import okhttp3.internal.concurrent.Task;

public class Login extends Fragment implements LoginView {

    private Button btnRegister;
    private TextInputEditText etUser;
    private TextInputEditText etPassword;
    private LoginPresenter presenter;
    private TextView btnLogin;
    private ProgressBar loadingItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        btnRegister = view.findViewById(R.id.btnGoToRegister);
        etUser = view.findViewById(R.id.etUser);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        loadingItem = view.findViewById(R.id.loading_item_login);

        presenter = new LoginPresenter(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                Log.i("LoginClass","Should try loging");
                String username = etUser.getText().toString();
                String password = etPassword.getText().toString();
                Log.i("LoginClass","Login called with: "+username+" y "+password );
                presenter.login(username, password);
            }
        });

        btnRegister.setOnClickListener(v->{
            Log.i("LoginClass", "Should go to register fragment");
            GoToRegister();
        });
    }

    @Override
    public void showProgress() {
        loadingItem.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loadingItem.setVisibility(View.GONE);
    }

    @Override
    public void onLoginSuccess(UserInfoDto userInfoDto) {
        hideProgress();
        Gson gson = new Gson();
        String json = gson.toJson(userInfoDto);
        Log.i("LoginClass","Login Success with userData: " + json);
        Log.i("LoginClass","ShouldGoToHome");
        GoToHome();
    }

    @Override
    public void onLoginError(String message) {
        hideProgress();
        Log.i("LoginClass","Login Error with message: " + message);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    public void GoToHome(){
        Intent intent = new Intent(getContext(), Home.class);
        startActivity(intent);
    }

    public void GoToRegister(){
        try {
            Register registerFragment = new Register();
            FragmentTransaction transactionRegister = getParentFragmentManager().beginTransaction();
            transactionRegister.replace(R.id.fragmentLogin, registerFragment);
            transactionRegister.addToBackStack(null);
            transactionRegister.commit();
        }catch (Exception e){
            Log.i("LoginClass", "Error on go to register fragment: " + e.getMessage());
        }
    }
}