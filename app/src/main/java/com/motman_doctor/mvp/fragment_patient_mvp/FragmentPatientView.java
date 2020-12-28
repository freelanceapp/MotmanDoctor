package com.motman_doctor.mvp.fragment_patient_mvp;

import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.UserModel;

import java.util.List;

public interface FragmentPatientView {
    void onSuccess(List<UserModel.User> data);
    void onFailed(String msg);
    void showProgressBar();
    void hideProgressBar();

}
