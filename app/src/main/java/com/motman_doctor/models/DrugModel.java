package com.motman_doctor.models;

import java.io.Serializable;

public class DrugModel implements Serializable {
    private int id;
    private int user_id;
    private int doctor_id;
    private int reservation_id;
    private String drag_name;
    private int take_num;
    private String details;
    private String status;
    private String started_at;
    private String created_at;
    private String updated_at;
    private UserModel.User patient_fk;
    private UserModel.User doctor_fk;
    private UserModel.LastReservationFk reservation_fk;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public String getDrag_name() {
        return drag_name;
    }

    public int getTake_num() {
        return take_num;
    }

    public String getDetails() {
        return details;
    }

    public String getStatus() {
        return status;
    }

    public String getStarted_at() {
        return started_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel.User getPatient_fk() {
        return patient_fk;
    }

    public UserModel.User getDoctor_fk() {
        return doctor_fk;
    }

    public UserModel.LastReservationFk getReservation_fk() {
        return reservation_fk;
    }
}
