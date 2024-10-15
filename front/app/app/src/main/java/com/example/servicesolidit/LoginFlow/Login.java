package com.example.servicesolidit.LoginFlow;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.servicesolidit.HomeFlow.Home;
import com.example.servicesolidit.Model.Responses.UserInfoDto;
import com.example.servicesolidit.R;
import com.example.servicesolidit.RegisterFlow.Register;
import com.example.servicesolidit.Utils.Constants;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import okhttp3.internal.concurrent.Task;

public class Login extends Fragment implements LoginView {

    private TextInputEditText edtUser, edtPassword;
    private TextInputLayout etPasswordLayout;
    private Boolean passwordToggleEnabled = false;
    private LoginPresenter presenter;
    private Button btnForgot;
    private Button btnLogin;
    private Button btnRegister;
    private ProgressBar loadingItem;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        edtUser = view.findViewById(R.id.edtxt_User);
        edtPassword = view.findViewById(R.id.edtxt_Password);
        etPasswordLayout = view.findViewById(R.id.txt_password_layout);
        btnForgot = view.findViewById(R.id.btn_forgot);
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegister = view.findViewById(R.id.btnGoToRegister);
        loadingItem = view.findViewById(R.id.loading_item_login);

        presenter = new LoginPresenter(this);

        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPasswordLayout.setEndIconDrawable(R.drawable.eye_visibility_off);
        etPasswordLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordToggleEnabled){
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPasswordLayout.setEndIconDrawable(R.drawable.eye_visibility_off);
                }else{
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPasswordLayout.setEndIconDrawable(R.drawable.eye_visibility);
                }
                passwordToggleEnabled = !passwordToggleEnabled;
                edtPassword.setSelection(edtPassword.getText().length());
            }
        });

        validateAlreadyLogged();
        return view;
    }

    public void validateAlreadyLogged(){
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);

            int userIdLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID,0);
            boolean isAlreadyLogged = sharedPreferences.getBoolean(Constants.IS_LOGGED, false);
            String userEmailLogged = sharedPreferences.getString(Constants.GET_EMAIL_USER, "");
            String userNameLogged = sharedPreferences.getString(Constants.GET_NAME_USER, "");


            Log.i("LoginClass", "Valor de userIdLogged: " + userIdLogged);
            Log.i("LoginClass", "Valor de isAlReadyLogged: " + isAlreadyLogged);
            Log.i("LoginClass", "Valor de userEmailLogged: " + userEmailLogged);
            Log.i("LoginClass", "Valor de userNameLogged: " + userNameLogged);

            if (userIdLogged!=0 &&
                    isAlreadyLogged &&
                    !userEmailLogged.isEmpty() &&
                    !userNameLogged.isEmpty()) {
                UserInfoDto alreadyLogged = new UserInfoDto();
                alreadyLogged.setIdUser(userIdLogged);
                alreadyLogged.setEmail(userEmailLogged);
                GoToHome(alreadyLogged, true);
            }else{
                Log.i("LoginClass", "Not shared preferences found");
            }
        }catch (Exception e){
            Log.i("LoginClass", "Error: "+ e.getMessage());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                Log.i("LoginClass","Should try loging");
                String username = edtUser.getText().toString();
                String password = edtPassword.getText().toString();
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
        GoToHome(userInfoDto, false);
    }

    @Override
    public void onLoginError(String message) {
        hideProgress();
        Log.i("LoginClass","Login Error with message: " + message);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    public void GoToHome(UserInfoDto userToLoadInfo, boolean alreadyLogged){
        Log.i("LoginClass", "alreadLogged on call to go to home: " + alreadyLogged);
        if(!alreadyLogged){
            saveUserLoggedInfo(userToLoadInfo);
            Log.i("LoginClass", "Info saved on Go To Home");
        }
        Intent intent = new Intent(getContext(), Home.class);
        startActivity(intent);
    }

    public void saveUserLoggedInfo(UserInfoDto userInfoDto){
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.IS_LOGGED, true);
            editor.putInt(Constants.GET_LOGGED_USER_ID, userInfoDto.getIdUser());
            editor.putString(Constants.GET_EMAIL_USER, userInfoDto.getEmail());
            editor.putString(Constants.GET_NAME_USER, userInfoDto.getNameUser());
            editor.apply();
            Log.i("LoginClass", "Saved success id: " + userInfoDto.getIdUser());
        }catch (Exception e){
            Log.i("LoginClass", "Error on save preferences");
        }
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