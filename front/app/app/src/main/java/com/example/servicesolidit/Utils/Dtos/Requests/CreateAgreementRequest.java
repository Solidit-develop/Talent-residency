package com.example.servicesolidit.Utils.Dtos.Requests;

public class CreateAgreementRequest {
    private String descripcion;
    private String creationDate;
    private String descripcionService;
    private String creacionDateService;

    public CreateAgreementRequest(String descripcion, String creationDate, String descripocionService, String creationDateService) {
        this.descripcion = descripcion;
        this.creationDate = creationDate;
        this.descripcionService = descripocionService;
        this.creacionDateService = creationDateService;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescripocionService() {
        return descripcionService;
    }

    public void setDescripocionService(String descripocionService) {
        this.descripcionService = descripocionService;
    }

    public String getCreationDateService() {
        return creacionDateService;
    }

    public void setCreationDateService(String creationDateService) {
        this.creacionDateService = creationDateService;
    }
}
