package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentItemResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private ArrayList<AppointmentItemResponse> appointmentList;
    private OnUpdateStatusListener updateStatusListener;

    public AppointmentAdapter(ArrayList<AppointmentItemResponse> list, OnUpdateStatusListener listener) {
        this.appointmentList = list;
        this.updateStatusListener = listener;
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment_list, parent, false);
        return new AppointmentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {
        AppointmentItemResponse item = this.appointmentList.get(position);
        Gson gson = new Gson();
        Log.i("AppointmentAdapter", gson.toJson(item));
        holder.spinnerEstatusAppointment.setText(item.getStatusUpdate());

        if(item.getNameUser() == null){
            // CustomerView
            holder.customerTitle.setText("Proveedor:");
            holder.customerName.setText(item.getNameProvider());
            holder.spinnerEstatusAppointment.setEnabled(false);
            holder.btnUpdate.setVisibility(View.GONE);
        }else{
            // ProviderView
            holder.customerName.setText(item.getNameUser() + " " + item.getLastName());
            holder.spinnerEstatusAppointment.setEnabled(true);
            holder.spinnerEstatusAppointment.setAdapter(
                    new ArrayAdapter<>(
                            holder.spinnerEstatusAppointment.getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            new String[] {"En espera", "Aprobar", "Cancelar"}
                    )
            );
            holder.btnUpdate.setVisibility(View.VISIBLE);

            holder.btnUpdate.setOnClickListener(v->{
                Toast.makeText(v.getContext(), "Trying to update the appointment with id: " + item.getIdAppointment() + " to " + holder.spinnerEstatusAppointment.getText().toString(), Toast.LENGTH_SHORT).show();
                if (updateStatusListener != null) {
                    updateStatusListener.onUpdateStatus(
                            item.getIdAppointment(),
                            holder.spinnerEstatusAppointment.getText().toString(),
                            item.getIdProvider(),
                            item.getIdUser()
                    );
                }
            });
            if(holder.spinnerEstatusAppointment.getText().toString().equals("Cancelado")){
                holder.btnUpdate.setEnabled(false);
                holder.spinnerEstatusAppointment.setEnabled(false);

            }
        }
        holder.providerName.setText(item.getWorkshopName());
        holder.location.setText(item.getAppointmentLocation());
        holder.date.setText(item.getAppointmentDate());
    }

    @Override
    public int getItemCount() {
        return this.appointmentList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView date, providerName, location, customerName, estatus, customerTitle;
        Button btnUpdate;
        // Spinners (AutoCompleteTextView)
        private AutoCompleteTextView spinnerEstatusAppointment;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tvContentDate);
            customerName = itemView.findViewById(R.id.tvContentClient);
            customerTitle = itemView.findViewById(R.id.tvTitleClient);
            location = itemView.findViewById(R.id.tvContentLocation);
            providerName = itemView.findViewById(R.id.tvContentProvider);
            spinnerEstatusAppointment = itemView.findViewById(R.id.spinnerEstatusAppointment);
            btnUpdate = itemView.findViewById(R.id.btnUpdateStatusAppointment);

            spinnerEstatusAppointment.setAdapter(
                new ArrayAdapter<>(
                    itemView.getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    new String[] {"En espera", "Aprobar", "Cancelar"}
                )
            );
        }
    }
}
