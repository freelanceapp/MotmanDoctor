package com.motman_doctor.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private int id;
    private int user_id;
    private int doctor_id;
    private String created_at;
    private String updated_at;
    private UserModel.User doctor_fk;
    private UserModel.User user_fk;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel.User getDoctor_fk() {
        return doctor_fk;
    }

    public UserModel.User getUser_fk() {
        return user_fk;
    }
}
