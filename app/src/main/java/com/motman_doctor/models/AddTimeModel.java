package com.motman_doctor.models;

import java.io.Serializable;
import java.util.List;

public class AddTimeModel implements Serializable {

    private int doctor_time_id;
    private List<Times> times;

    public int getDoctor_time_id() {
        return doctor_time_id;
    }

    public void setDoctor_time_id(int doctor_time_id) {
        this.doctor_time_id = doctor_time_id;
    }

    public List<Times> getTimes() {
        return times;
    }

    public void setTimes(List<Times> times) {
        this.times = times;
    }

    public static class Times implements Serializable {
        private String from_hour;
        private String to_hour;
        private String type;

        public String getFrom_hour() {
            return from_hour;
        }

        public void setFrom_hour(String from_hour) {
            this.from_hour = from_hour;
        }

        public String getTo_hour() {
            return to_hour;
        }

        public void setTo_hour(String to_hour) {
            this.to_hour = to_hour;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}