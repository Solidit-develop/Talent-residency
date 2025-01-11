package com.example.servicesolidit.Utils.Dtos.Requests;

public class ResetPasswordRequest {
    private String correo;

    public ResetPasswordRequest(String emailTxt) {
        this.correo = emailTxt;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
