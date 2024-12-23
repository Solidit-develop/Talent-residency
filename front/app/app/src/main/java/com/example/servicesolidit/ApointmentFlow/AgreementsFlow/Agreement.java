package com.example.servicesolidit.ApointmentFlow.AgreementsFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Dtos.Requests.CreateAgreementRequest;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Agreement extends Fragment implements AgreementView{
    private EditText description, descriptionService;
    private Button btnConfirmAgreement;
    private CalendarView calendar;
    private String dateSelected;
    private AgreementPresenter presenter;
    private ProgressBar progressBar;
    private int idProvider,  idCustomer,  appointmentId;

    public Agreement(int idProvider, int idCustomer, int appointmentId) {
        this.idProvider = idProvider;
        this.idCustomer = idCustomer;
        this.appointmentId = appointmentId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agreement, container, false);
        description = view.findViewById(R.id.etDescripcion);
        descriptionService = view.findViewById(R.id.etDescripcionService);
        btnConfirmAgreement = view.findViewById(R.id.btnConfirmarAcuerdo);
        calendar = view.findViewById(R.id.cvServiceDate);
        progressBar = view.findViewById(R.id.progressBarOnConfirmAgreement);

        this.presenter = new AgreementPresenter(this);

        Calendar calendarTime = Calendar.getInstance();
        long currentDateInMillis = calendarTime.getTimeInMillis();
        calendar.setDate(currentDateInMillis, false, true);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // La fecha seleccionada se proporciona directamente como año, mes y día
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                dateSelected = selectedDate;
            }
        });

        btnConfirmAgreement.setOnClickListener(v->{
            if(inputValidate()){
                CreateAgreementRequest request = new CreateAgreementRequest(
                        this.description.getText().toString(),
                        getCurrentDateTime(),
                        this.descriptionService.getText().toString(),
                        this.dateSelected
                );
                this.presenter.createAgreement(request, appointmentId, idProvider);
                onShowProgress();
            }
            Toast.makeText(v.getContext(), "Date selcted: " + dateSelected, Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    private boolean inputValidate() {
        return !description.getText().toString().isEmpty() && !descriptionService.getText().toString().isEmpty() && dateSelected!="";
    }

    @Override
    public void onCreateAgreementSuccess(String acuerdoGeneradoCorrectamente) {
        onHideProgress();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCreateAgreementError(String s) {
        Toast.makeText(requireContext(), "Error on create agreement: " + s, Toast.LENGTH_SHORT).show();
        onHideProgress();
    }

    @Override
    public void onShowProgress() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        this.progressBar.setVisibility(View.GONE);
    }

    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS'Z'")
                .withZone(ZoneOffset.UTC);
        return formatter.format(Instant.now());
    }
}