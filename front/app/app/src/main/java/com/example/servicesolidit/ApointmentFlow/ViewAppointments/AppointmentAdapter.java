package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentItemResponse;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private ArrayList<AppointmentItemResponse> appointmentList;

    public AppointmentAdapter(ArrayList<AppointmentItemResponse> list){
        this.appointmentList = list;
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
        holder.customerName.setText(item.getNameUser() + " " + item.getLastName());
        holder.providerName.setText(item.getWorkshopName());
        holder.location.setText(item.getAppointmentLocation());
        holder.date.setText(item.getAppointmentDate());
        holder.estatus.setText(item.getStatusUpdate());
    }

    @Override
    public int getItemCount() {
        return this.appointmentList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView date, providerName, location, customerName, estatus;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tvContentDate);
            customerName = itemView.findViewById(R.id.tvContentClient);
            location = itemView.findViewById(R.id.tvContentLocation);
            providerName = itemView.findViewById(R.id.tvContentProvider);
            estatus = itemView.findViewById(R.id.tvEstatusAppointment);
        }
    }
}
