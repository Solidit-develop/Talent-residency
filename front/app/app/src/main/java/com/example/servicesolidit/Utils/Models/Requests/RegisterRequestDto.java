package com.example.servicesolidit.Utils.Models.Requests;

public class RegisterRequestDto {
    private String name_user;
    private String lastname;
    private String email;
    private String password;
    private int age;
    private String phoneNumber;
    private String street_1;
    private String street_2;
    private String localidad;
    private String name_Town;
    private String name_state;
    private String zipcode;

    public RegisterRequestDto(){

    }

    // Constructor
    public RegisterRequestDto(String name_user, String lastname, String email, String password, int age, String phoneNumber,
                             String street_1, String street_2, String localidad, String name_Town, String name_state, String zipcode) {
        this.name_user = name_user;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.street_1 = street_1;
        this.street_2 = street_2;
        this.localidad = localidad;
        this.name_Town = name_Town;
        this.name_state = name_state;
        this.zipcode = zipcode;
    }

    // Getters and Setters
    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet_1() {
        return street_1;
    }

    public void setStreet_1(String street_1) {
        this.street_1 = street_1;
    }

    public String getStreet_2() {
        return street_2;
    }

    public void setStreet_2(String street_2) {
        this.street_2 = street_2;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getName_Town() {
        return name_Town;
    }

    public void setName_Town(String name_Town) {
        this.name_Town = name_Town;
    }

    public String getName_state() {
        return name_state;
    }

    public void setName_state(String name_state) {
        this.name_state = name_state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
