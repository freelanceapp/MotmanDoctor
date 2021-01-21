package com.motman_doctor.models;

import android.content.Context;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.motman_doctor.BR;
import com.motman_doctor.R;


public class AdddrugModel extends BaseObservable {
    private String name;
    private String dose;
    private String detials;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_dose = new ObservableField<>();
    public ObservableField<String> error_detials = new ObservableField<>();


    public boolean isDataValid(Context context) {

        if (!name.isEmpty() &&
                !dose.isEmpty() && !detials.isEmpty()


        ) {


            error_name.set(null);
            error_dose.set(null);
            error_detials.set(null);

            return true;

        } else {

            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);

            }


            if (dose.isEmpty()) {
                error_dose.set(context.getString(R.string.field_required));
            } else {
                error_dose.set(null);

            }
            if (detials.isEmpty()) {
                error_detials.set(context.getString(R.string.field_required));
            } else {
                error_detials.set(null);

            }


            return false;

        }

    }

    public AdddrugModel() {
        name = "";
        dose = "";
        detials="";
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getDetials() {
        return detials;
    }

    public void setDetials(String detials) {
        this.detials = detials;
    }
}
