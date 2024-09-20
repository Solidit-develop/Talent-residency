package com.example.servicesolidit.Model.Responses;

import com.google.gson.annotations.SerializedName;

public class UserInfoDto {
    @SerializedName("id_user")
    private int idUser;

    @SerializedName("name_User")
    private String nameUser;

    @SerializedName("lasname")
    private String lastname;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("age")
    private int age;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    public UserInfoDto(int idUser, String nameUser, String lastname, String email, String password, int age, String phoneNumber) {
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
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
}
