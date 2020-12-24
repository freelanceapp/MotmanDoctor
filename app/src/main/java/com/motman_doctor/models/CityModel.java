package com.motman_doctor.models;

import java.io.Serializable;

public class CityModel implements Serializable {
    private int id;
    private String name;

    public CityModel(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
