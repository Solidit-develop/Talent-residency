package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

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

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentItemResponse;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Models.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProfileDto;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObtainAppointments extends Fragment implements ObtainAppointmentView {
    private ObtainAppointmentPresenter presenter;
    private int idLogged;
    private RecyclerView rvAppointmentsList;
    private ArrayList<AppointmentItemResponse> appointmentsList;
    private AppointmentAdapter adapter;
    private boolean isProvider;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obtain_appointments, container, false);
        this.rvAppointmentsList = view.findViewById(R.id.recyclerViewAppointments);
        this.presenter = new ObtainAppointmentPresenter(this);

        this.appointmentsList = new ArrayList<>();
        this.adapter = new AppointmentAdapter(appointmentsList);
        this.rvAppointmentsList.setAdapter(adapter);
        this.rvAppointmentsList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        // Validar si soy proveedor o si soy cliente
        getProviderIdByUserId(getLoggedId());
        // Mover
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
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccessObtainResponse(ArrayList<AppointmentItemResponse> result) {
        Log.i("ObtainAppointment", "Founded: " + result.size());
        this.appointmentsList.clear();
        if(!result.isEmpty()){
            this.appointmentsList.addAll(result);
            Log.i("ObtainAppointment", "Se encontró: " + result.size());
        }else{
            Log.i("ObtainAppointment", "No se encontraron negocios que coincidan");
        }
        this.adapter.notifyDataSetChanged();
        this.onHideProgress();
    }

    @Override
    public void onErrorObtainResponse(String s) {
        Log.i("ObtainAppointmentClass", "onErrorObtainResponse: " + s);

    }

    @Override
    public void onSuccessGetInformationAsProvider(ProviderResponseDto result) {
        int id = result.getIdProvidersss();
        this.presenter.getAppointments(id, "asProvider");
    }

    @Override
    public void onErrorGetInformationAsProvider(String message) {
        Log.i("ObtainAppointmentClass", "onErrorGetInformationAsProvider: " + message);
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
                this.presenter.getAppointments(result.getIdUser(), "asCustomer");
            }
        }else {
            Log.i("ObtainAppointmentClass", "ErrorOnLoadProfileSuccess");
        }
    }

    @Override
    public void onLoadProfileError(String message) {
        Log.i("ObtainAppointmentClass", "ErrorOnLoadProfileError: " + message);
    }
}