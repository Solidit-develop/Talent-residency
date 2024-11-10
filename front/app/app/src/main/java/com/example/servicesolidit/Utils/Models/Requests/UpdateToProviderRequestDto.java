package com.example.servicesolidit.Utils.Models.Requests;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UpdateToProviderRequestDto {
    private String email;
    private String nameState;
    private String zipCode;
    private String nameTown;
    private String str1;
    private String str2;
    private String localidad;
    private ArrayList<String> skills;
    private String experience;
    private String workshopName;
    private String workshopPhone;

    public UpdateToProviderRequestDto(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameState() {
        return nameState;
    }

    public void setNameState(String nameState) {
        this.nameState = nameState;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNameTown() {
        return nameTown;
    }

    public void setNameTown(String nameTown) {
        this.nameTown = nameTown;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getWorkshopPhone() {
        return workshopPhone;
    }

    public void setWorkshopPhone(String workshopPhone) {
        this.workshopPhone = workshopPhone;
    }
}
