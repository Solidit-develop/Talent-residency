package com.example.servicesolidit.ApointmentFlow;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.servicesolidit.R;

import java.util.Calendar;

public class Appointment extends Fragment implements AppointmentView{

    private TextView tvHoraSeleccionada;
    private Button btnSeleccionarHora;
    private Button btnConfirmarCita;
    private int idOrigen;
    private AppointmentPresenter presenteer;
    private CalendarView calendar;
    private String dateSelected;
    private String hourSelected;

    public Appointment(int idOrigen){
        this.idOrigen = idOrigen;
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



        this.presenteer = new AppointmentPresenter(this);

        Calendar calendarTime = Calendar.getInstance();
        long currentDateInMillis = calendarTime.getTimeInMillis();
        calendar.setDate(currentDateInMillis, false, true);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // La fecha seleccionada se proporciona directamente como año, mes y día
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                dateSelected = "Fecha seleccionada: \n" + selectedDate;
            }
        });

        btnConfirmarCita.setOnClickListener(v->{
            Toast.makeText(requireContext(), "Confirmar cita: ", Toast.LENGTH_LONG).show();
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

    }

    @Override
    public void onErrorAppointmentCreated(String errorMessage) {

    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }
}