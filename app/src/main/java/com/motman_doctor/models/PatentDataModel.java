package com.motman_doctor.models;

import java.io.Serializable;
import java.util.List;

public class PatentDataModel implements Serializable {
    private int current_page;
    private List<UserModel.User> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<UserModel.User> getData() {
        return data;
    }
}
