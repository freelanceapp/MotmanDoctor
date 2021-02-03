package com.motman_doctor.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.motman_doctor.BR;
import com.motman_doctor.R;

import java.util.ArrayList;
import java.util.List;

public class EditProfileModel extends BaseObservable {
    private String imageUrl;
    private String name;
    private String job_title;
    private String email;
    private String time;

    private String password;
    ;
    private String phone_code;
    private String phone;
    public ObservableField<String> error_password = new ObservableField<>();


    public ObservableField<String> error_name = new ObservableField<>();


    public EditProfileModel() {
        this.phone_code = "";
        this.phone = "";
        this.imageUrl = "";
        this.name = "";
        this.job_title = "";
        this.email = "";
        this.password = "";
        this.time = "";
    }

    public boolean isDataValid(Context context) {
        if (!name.isEmpty() &&
                (!password.isEmpty() && password.length() >= 6) || password.isEmpty()

        ) {

            error_name.set(null);
            error_password.set(null);
            return true;

        } else {
            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_req));
            } else {
                error_name.set(null);
            }
            if (!password.isEmpty() && password.length() < 6) {
                error_password.set(context.getString(R.string.password_short));
            } else {
                error_password.set(null);
            }
            return false;
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
