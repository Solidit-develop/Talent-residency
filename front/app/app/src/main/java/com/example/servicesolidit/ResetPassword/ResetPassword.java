package com.example.servicesolidit.ResetPassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.servicesolidit.LoginFlow.Login;
import com.example.servicesolidit.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.chrono.HijrahDate;

public class ResetPassword extends Fragment implements ResetPasswordView{

    private TextInputEditText email;
    private Button btnTryToReset;
    private ResetPasswordPresenter presenter;
    private ProgressBar loading_item_reset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        this.presenter = new ResetPasswordPresenter(this);
        this.email = view.findViewById(R.id.TIETemailToReset);
        this.btnTryToReset = view.findViewById(R.id.btnTryReset);
        this.loading_item_reset = view.findViewById(R.id.loading_item_reset);

        this.btnTryToReset.setOnClickListener(v->{
            String emailTxt = this.email.getText().toString();
            if(!emailTxt.isEmpty()){
                onShowProgress();
                this.presenter.resetPassword(emailTxt);
            }
        });

        return view;
    }

    @Override
    public void onShowProgress() {
        this.loading_item_reset.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        this.loading_item_reset.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessReset(String message) {
        onHideProgress();
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        GoToLogin();
    }

    @Override
    public void onErrorReset(String errror) {
        onHideProgress();
        Log.i("ResetPassword", "OnErrorReset: " + errror);
        Toast.makeText(requireContext(), errror, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to replace login fragment to reset password fragment
     */
    public void GoToLogin(){
        try {
            Login registerFragment = new Login();
            FragmentTransaction transactionRegister = getParentFragmentManager().beginTransaction();
            transactionRegister.replace(R.id.fragmentLogin, registerFragment);
            transactionRegister.addToBackStack(null);
            transactionRegister.commit();
        }catch (Exception e){
            Log.i("LoginClass", "Error on go to register fragment: " + e.getMessage());
        }
    }
}