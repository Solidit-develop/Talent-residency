package com.example.servicesolidit.RegisterFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.servicesolidit.LoginFlow.Login;
import com.example.servicesolidit.Utils.Dtos.Requests.RegisterRequestDto;
import com.example.servicesolidit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Password extends Fragment implements RegisterView {

    private Button btnConfirm;
    private TextInputLayout etPassword;
    private TextInputEditText edtxtPassword;
    private TextInputLayout etConfirmPassword;
    private TextInputEditText edtxtConfirmPassword;
    private Boolean passwordToggleEnabled = false;

    private RegisterRequestDto request;
    private RegisterPresenter presenter;

    private ProgressBar progressBar;

    public Password(RegisterRequestDto requestDto){
        this.request = requestDto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        etPassword = view.findViewById(R.id.txt_password_input);
        edtxtPassword = view.findViewById(R.id.edtxt_password_fp);
        etConfirmPassword = view.findViewById(R.id.txt_confirm_password_input);
        edtxtConfirmPassword = view.findViewById(R.id.edtxt_conf_pass_fp);
        progressBar = view.findViewById(R.id.progress_item);
        presenter = new RegisterPresenter(this);


        // Switch toggle control
        edtxtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword.setEndIconDrawable(R.drawable.eye_visibility_off);
        etPassword.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordToggleEnabled){
                    edtxtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setEndIconDrawable(R.drawable.eye_visibility_off);
                }else{
                    edtxtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPassword.setEndIconDrawable(R.drawable.eye_visibility);
                }
                passwordToggleEnabled = !passwordToggleEnabled;
                edtxtPassword.setSelection(edtxtPassword.getText().length());
            }
        });


        edtxtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etConfirmPassword.setEndIconDrawable(R.drawable.eye_visibility_off);
        etConfirmPassword.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordToggleEnabled){
                    edtxtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etConfirmPassword.setEndIconDrawable(R.drawable.eye_visibility_off);
                }else{
                    edtxtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etConfirmPassword.setEndIconDrawable(R.drawable.eye_visibility);
                }
                passwordToggleEnabled = !passwordToggleEnabled;
                edtxtConfirmPassword.setSelection(edtxtConfirmPassword.getText().length());
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getEditText().getText().toString();
                String confirmPassword = etConfirmPassword.getEditText().getText().toString();

                if(isValidaPassword(password, confirmPassword)){
                    request.setPassword(password);
                    showProgress();
                    presenter.register(request);
                }else{
                    Toast.makeText(requireContext(), "Las contraseñas ingresadas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private boolean isValidaPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRegisterSuccess(String message) {
        hideProgress();
        Log.i("PasswordClass", "Should show alert with message: " + message);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        Login loginFragment = new Login();
        FragmentTransaction transactionRegister = getParentFragmentManager().beginTransaction();
        transactionRegister.replace(R.id.fragmentLogin, loginFragment);
        transactionRegister.addToBackStack(null);
        transactionRegister.commit();
    }

    @Override
    public void onRegisterError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}