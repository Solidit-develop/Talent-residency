package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.servicesolidit.ApointmentFlow.AgreementsFlow.Agreement;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentItemResponse;
import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ObtainAppointments extends Fragment implements ObtainAppointmentView, OnUpdateStatusListener {
    private ObtainAppointmentPresenter presenter;
    private RecyclerView rvAppointmentsList;
    private ArrayList<AppointmentItemResponse> appointmentsList;
    private AppointmentAdapter adapter;
    private boolean isProvider;
    private ProgressBar progressBar;
    private TextView itemNoAppointmentsView;

    //Headers view
    private Button btnShowAppointmentsAsProvider;
    private Button btnShowAppointmentsAsCustomer;
    private LinearLayout headerAsProvider;
    private LinearLayout headerAsCustomer;
    private TextView tvHeaderAppointmentsType;

    private UserInfoProfileDto userLoggedDto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obtain_appointments, container, false);
        this.rvAppointmentsList = view.findViewById(R.id.recyclerViewAppointments);
        this.itemNoAppointmentsView = view.findViewById(R.id.idNoAppointmentsView);
        this.progressBar = view.findViewById(R.id.progressBarOnUpdateAppointments);

        this.btnShowAppointmentsAsCustomer = view.findViewById(R.id.btnShowWhereImCustomer);
        this.btnShowAppointmentsAsProvider = view.findViewById(R.id.btnShowWhereImProvider);

        this.headerAsCustomer = view.findViewById(R.id.headerAppointmentsCustomer);
        this.headerAsProvider = view.findViewById(R.id.headerAppointmentsProvider);

        this.tvHeaderAppointmentsType = view.findViewById(R.id.tvHeaderAppointmentsType);

        this.presenter = new ObtainAppointmentPresenter(this);

        this.appointmentsList = new ArrayList<>();
        this.adapter = new AppointmentAdapter(appointmentsList, this);
        this.rvAppointmentsList.setAdapter(adapter);
        this.rvAppointmentsList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        this.btnShowAppointmentsAsProvider.setOnClickListener(v->{
            onShowProgress();
            this.tvHeaderAppointmentsType.setText("Citas donde soy proveedor");
            this.presenter.getProviderInformationFromUserId(this.userLoggedDto.getIdUser());
        });

        this.btnShowAppointmentsAsCustomer.setOnClickListener(v->{
            onShowProgress();
            this.tvHeaderAppointmentsType.setText("Citas donde soy cliente");
            this.presenter.getAppointments(this.userLoggedDto.getIdUser(), "asCustomer");

        });


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
        onShowProgress();
        this.presenter.information(userId);
    }

    @Override
    public void onShowProgress() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        this.progressBar.setVisibility(View.GONE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccessObtainResponse(ArrayList<AppointmentItemResponse> result) {
        Log.i("ObtainAppointment", "Founded: " + result.size());
        this.appointmentsList.clear();
        if(!result.isEmpty()){
            this.rvAppointmentsList.setVisibility(View.VISIBLE);
            this.itemNoAppointmentsView.setVisibility(View.GONE);
            this.appointmentsList.addAll(result);
            Log.i("ObtainAppointment", "Se encontró: " + result.size());
        }else{
            this.rvAppointmentsList.setVisibility(View.GONE);
            this.itemNoAppointmentsView.setVisibility(View.VISIBLE);
            Log.i("ObtainAppointment", "No se encontraron citas que coincidan");
        }
        this.adapter.notifyDataSetChanged();
        this.onHideProgress();
    }

    @Override
    public void onErrorObtainResponse(String s) {
        Log.i("ObtainAppointmentClass", "onErrorObtainResponse: " + s);
        onHideProgress();
    }

    @Override
    public void onSuccessGetInformationAsProvider(ProviderResponseDto result) {
        int id = result.getIdProvidersss();
        this.presenter.getAppointments(id, "asProvider");
    }

    @Override
    public void onErrorGetInformationAsProvider(String message) {
        Log.i("ObtainAppointmentClass", "onErrorGetInformationAsProvider: " + message);
        onHideProgress();
    }

    @Override
    public void onLoadProfileSuccess(UserInfoProfileDto result) {
        if(result != null){
            Gson gson = new Gson();
            Log.i("ObtainAppointmentClass", "Information: " + gson.toJson(result));
            this.userLoggedDto = result;
            this.isProvider = result.getTypes().isValue();
            if(this.isProvider){
                // Obtain appointments where i am provider
                setHeaderView("asProvider");
                this.presenter.getProviderInformationFromUserId(result.getIdUser());
            }else{
                // Obtain appointments where i am customer
                setHeaderView("asCustomer");
                this.presenter.getAppointments(result.getIdUser(), "asCustomer");
            }
        }else {
            Log.i("ObtainAppointmentClass", "ErrorOnLoadProfileSuccess");
        }
        onHideProgress();
    }

    private void setHeaderView(String userType) {
        if(userType.equals("asCustomer")){
            this.headerAsCustomer.setVisibility(View.VISIBLE);
            this.headerAsProvider.setVisibility(View.GONE);
        }
        if(userType.equals("asProvider")){
            this.headerAsCustomer.setVisibility(View.GONE);
            this.headerAsProvider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadProfileError(String message) {
        Log.i("ObtainAppointmentClass", "ErrorOnLoadProfileError: " + message);
    }

    @Override
    public void onAppointmentUpdated(String actualizadoCorrectamente) {
        getProviderIdByUserId(getLoggedId());
    }

    @Override
    public void onAppointmentUpdatedError(String error) {
        Log.i("ObtainAppointmentClass", "OnAppointmentUdpatedError: " + error);
    }

    @Override
    public void onUpdateStatus(int appointmentId, String newStatus, int idProvider, int idCustomer) {
        onShowProgress();
        switch (newStatus) {
            case "Cancelar": {
                this.presenter.updateAppointment(appointmentId, newStatus, idProvider, idCustomer);
                break;
            }
            case "Aprobar": {
                // Redirect to create agreement
                Agreement fragment = new Agreement(idProvider, idCustomer, appointmentId);
                navigateTo(fragment);
            }
        }
    }

    /**
     * Method to load a new fragment from the slide menu.
     * @param fragmentDestiny is the fragment to navigate.
     */
    public void navigateTo(Fragment fragmentDestiny) {
        Log.i("ObtainAppointment", "Start slide transaction fragment");
        FragmentTransaction transactionTransaction = this.requireActivity().getSupportFragmentManager().beginTransaction();
        transactionTransaction.replace(R.id.frame_container, fragmentDestiny);
        transactionTransaction.addToBackStack(null);
        transactionTransaction.commit();
    }
}