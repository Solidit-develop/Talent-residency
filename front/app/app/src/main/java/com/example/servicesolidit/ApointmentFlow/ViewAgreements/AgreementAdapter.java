package com.example.servicesolidit.ApointmentFlow.ViewAgreements;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Dtos.Responses.Agreements.AgreementAppointmentAgreeemntResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Agreements.AgreementAppointmentResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Agreements.AgreementResponseDto;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AgreementAdapter extends RecyclerView.Adapter<AgreementAdapter.AgreementViewHolder> {

    private ArrayList<AgreementAppointmentResponseDto> agreementsList;

    public AgreementAdapter(ArrayList<AgreementAppointmentResponseDto> agreementsList) {
        this.agreementsList = agreementsList;
    }

    @NonNull
    @Override
    public AgreementAdapter.AgreementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agreement, parent, false);
        return new AgreementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgreementAdapter.AgreementViewHolder holder, int position) {
        AgreementAppointmentResponseDto item = this.agreementsList.get(position);

        holder.dateAppointment.setText(item.getAppointmentDate());
        holder.locationAppointment.setText(item.getAppointmentLocation());
        holder.customerName.setText(item.getUser() != null ? item.getUser().getNameUser() + " " + item.getUser().getLastname() : "No name identified");
        holder.customerContact.setText(item.getUser() != null ? item.getUser().getPhoneNumber(): "No contact identified");
        holder.serviceDesription.setText(!item.getAgreements().isEmpty() ? item.getAgreements().get(0).getDescription() : "Sin descripción");
        holder.currentStatus.setText(item.getStatusAppointment());

    }

    @Override
    public int getItemCount() {
        return this.agreementsList.size();
    }

    public class AgreementViewHolder extends RecyclerView.ViewHolder {
        private TextView dateAppointment, locationAppointment, customerName, customerContact, serviceDesription, currentStatus;
        public AgreementViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dateAppointment = itemView.findViewById(R.id.tvAppointmentDate);
            this.locationAppointment = itemView.findViewById(R.id.tvAppointmentLocation);
            this.customerName = itemView.findViewById(R.id.tvAppointmentCustomerName);
            this.customerContact = itemView.findViewById(R.id.tvAppointmentCustomerContact);
            this.serviceDesription = itemView.findViewById(R.id.tvAppointmentServiceDescription);
            this.currentStatus = itemView.findViewById(R.id.tvAppointmentCurrentStatus);
        }
    }
}
