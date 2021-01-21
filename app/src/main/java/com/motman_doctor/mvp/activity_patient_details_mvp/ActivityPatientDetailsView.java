package com.motman_doctor.mvp.activity_patient_details_mvp;

import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.DrugModel;
import com.motman_doctor.models.UserModel;

import java.util.List;

public interface ActivityPatientDetailsView {
    void oncloseSuccess(List<DrugModel> data);
    void onFailed(String msg);
    void showProgressBar();
    void hideProgressBar();
    void oncopenSuccess(ApointmentModel.Data data);
    void onLoad();
    void onFinishload();
    void onServer();
    void onnotconnect(String msg);

    void oncloseSuccess();
}
