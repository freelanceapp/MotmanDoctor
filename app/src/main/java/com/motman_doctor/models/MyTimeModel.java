package com.motman_doctor.models;

import java.io.Serializable;
import java.util.List;

public class MyTimeModel implements Serializable{

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable {
        private int id;
                private int doctor_time_id;
                private String from_hour;
                private String to_hour;

        public int getId() {
            return id;
        }

        public int getDoctor_time_id() {
            return doctor_time_id;
        }

        public String getFrom_hour() {
            return from_hour;
        }

        public String getTo_hour() {
            return to_hour;
        }
    }


}