package com.example.servicesolidit.ApointmentFlow.ViewAgreements;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.servicesolidit.ApointmentFlow.ViewAppointments.ObtainAppointmentPresenter;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Dtos.Responses.Agreements.AgreementAppointmentResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Agreements.AgreementResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ObtainAgreements extends Fragment implements ObtainAgreementsView{

    private RecyclerView rvListAgreemt;
    private AgreementAdapter adapter;
    private ArrayList<AgreementAppointmentResponseDto> agreementsList;
    private TextView noItemView;
    private boolean isProvider;
    private ObtainAgreementsPresenter presenter;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_agreements, container, false);
        this.noItemView = view.findViewById(R.id.itemNoAgreementView);
        this.progressBar = view.findViewById(R.id.progressBarOnUpdateAgreements);
        this.rvListAgreemt = view.findViewById(R.id.rvListAgreements);

        this.presenter = new ObtainAgreementsPresenter(this);

        this.agreementsList = new ArrayList<>();
        this.adapter = new AgreementAdapter(this.agreementsList);
        this.rvListAgreemt.setAdapter(this.adapter);
        this.rvListAgreemt.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        // Validar si soy proveedor o si soy cliente
        getProviderIdByUserId(getLoggedId());

        return view;
    }

    public int getLoggedId(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        int userIdLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID, 0);
        Log.i("ObtainAppointment", "IdLogged: " + userIdLogged);
        return  userIdLogged;
    }

    public void getProviderIdByUserId(int userId){
        this.presenter.information(userId);
    }

    @Override
    public void onLoadProfileSuccess(UserInfoProfileDto result) {
        if(result != null){
            Gson gson = new Gson();
            Log.i("ObtainAppointmentClass", "Information: " + gson.toJson(result));
            this.isProvider = result.getTypes().isValue();
            if(this.isProvider){
                this.presenter.getProviderInformationFromUserId(result.getIdUser());
            }else{
                this.noItemView.setVisibility(View.VISIBLE);
                this.noItemView.setText("Usted no puede acceder a esta sección");
                this.rvListAgreemt.setVisibility(View.GONE);
            }
        }else {
            Log.i("ObtainAppointmentClass", "ErrorOnLoadProfileSuccess");
        }
        onHideProgress();
    }

    @Override
    public void onLoadProfileError(String message) {
        Log.i("ObtainAppointmentClass", "ErrorOnLoadProfileError: " + message);
    }

    @Override
    public void onShowProgress() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetInformationAsProvider(ProviderResponseDto result) {
        int id = result.getIdProvidersss();
        this.presenter.getAgreements(id, "asProvider");
    }

    @Override
    public void onErrorGetInformationAsProvider(String message) {
        Log.i("ObtainAgreementsClass", "onErrorGetInformationAsProvider: " + message);
        onHideProgress();
    }

    @Override
    public void onErrorObtainAgreements(String s) {
        Log.i("OAC", "ErrorObatinAgreements: " + s);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccessObtainAgreements(ArrayList<AgreementAppointmentResponseDto> citas) {
        if(!citas.isEmpty()){
            this.noItemView.setVisibility(View.GONE);
            this.rvListAgreemt.setVisibility(View.VISIBLE);
            this.agreementsList.addAll(citas);
        }else{
            this.noItemView.setVisibility(View.VISIBLE);
            this.rvListAgreemt.setVisibility(View.GONE);
        }
        this.adapter.notifyDataSetChanged();
        this.onHideProgress();
    }
}