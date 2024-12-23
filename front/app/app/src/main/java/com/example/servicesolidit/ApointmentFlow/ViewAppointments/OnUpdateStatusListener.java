package com.example.servicesolidit.ApointmentFlow.ViewAppointments;

public interface OnUpdateStatusListener {
    void onUpdateStatus(int appointmentId, String newStatus, int idProvider, int idCustomer);
}
