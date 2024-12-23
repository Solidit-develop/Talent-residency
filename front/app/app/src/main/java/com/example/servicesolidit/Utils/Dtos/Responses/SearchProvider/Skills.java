package com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider;

import com.google.gson.annotations.SerializedName;

public class Skills {

    @SerializedName("id_skills")
    private int idSkills;

    @SerializedName("name")
    private String name;

    // Getters y Setters
    public int getIdSkills() {
        return idSkills;
    }

    public void setIdSkills(int idSkills) {
        this.idSkills = idSkills;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
