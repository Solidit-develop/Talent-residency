package com.example.servicesolidit.ApointmentFlow;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Models.Requests.CreateAppointmentRequestDto;

import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Appointment extends Fragment implements AppointmentView{

    private TextView tvHoraSeleccionada;
    private Button btnSeleccionarHora;
    private Button btnConfirmarCita;
    private int idOrigen;
    private int idDestino;
    private AppointmentPresenter presenteer;
    private CalendarView calendar;
    private String dateSelected;
    private String hourSelected = "";
    private EditText etAppointmentLocation;
    private ProgressBar progressBar;

    public Appointment(int idOrigen, int idDestino){
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        tvHoraSeleccionada = view.findViewById(R.id.textViewHora);
        btnSeleccionarHora = view.findViewById(R.id.buttonSeleccionarHora);
        btnConfirmarCita = view.findViewById(R.id.btnConfirmarCita);
        calendar = view.findViewById(R.id.claendarAppintment);
        etAppointmentLocation = view.findViewById(R.id.etAppointmentLocation);
        progressBar = view.findViewById(R.id.progressBarOnConfirmAppointment);


        this.presenteer = new AppointmentPresenter(this);

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


        btnConfirmarCita.setOnClickListener(v->{
            String locationSelected = etAppointmentLocation.getText().toString();
            if(locationSelected.isEmpty() || dateSelected != null) {
                // Obtener la fecha actual
                LocalDate today = LocalDate.now();

                // Formatear la fecha al formato deseado (ejemplo: 2024-12-01)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String fechaFormateada = today.format(formatter);
                CreateAppointmentRequestDto requestDto = new CreateAppointmentRequestDto();

                requestDto.setAppointmentDate(dateSelected);
                requestDto.setCreationDate(fechaFormateada);
                requestDto.setAppointmentLocation(locationSelected);
                Log.i("AppointmentClass", "Intenta generar cita con customer: " + idOrigen + " y con provider " + idDestino);

                this.presenteer.createAppointment(requestDto, idDestino, idOrigen);
                this.onShowProgress();
                Toast.makeText(requireContext(), "Confirmar cita: " + dateSelected + " en " + locationSelected, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(requireContext(), "Llena todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        btnSeleccionarHora.setOnClickListener(v->{
            mostrarTimePicker();
        });

        return  view;
    }

    private void mostrarTimePicker() {
        // Obtener la hora actual
        Calendar calendar = Calendar.getInstance();
        int horaActual = calendar.get(Calendar.HOUR_OF_DAY);
        int minutoActual = calendar.get(Calendar.MINUTE);

        // Crear el TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Formatear la hora seleccionada
                        String horaFormateada = String.format("%02d:%02d", hourOfDay, minute);
                        hourSelected = horaFormateada;
                        confirmInformation();
                    }
                },
                horaActual,
                minutoActual,
                false // true para formato de 24 horas, false para 12 horas (AM/PM)
        );

        // Mostrar el TimePickerDialog
        timePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void confirmInformation() {
        if(!this.hourSelected.isEmpty() && !this.dateSelected.isEmpty()){
            String selected = this.dateSelected + " a las \n" + this.hourSelected;
            this.tvHoraSeleccionada.setText(selected);
        }else{
            this.tvHoraSeleccionada.setText("Olvidaste seleccionar hora ó fecha para solicitar tu cita");
        }
    }

    @Override
    public void onSuccessAppintmentCreated(String appointmentResponse) {
        onHideProgress();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onErrorAppointmentCreated(String errorMessage) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowProgress() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        this.progressBar.setVisibility(View.GONE);
    }
}