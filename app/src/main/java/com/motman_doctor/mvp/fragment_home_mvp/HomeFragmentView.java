package com.motman_doctor.mvp.fragment_home_mvp;

import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.UserModel;

public interface HomeFragmentView {
    void onSuccess(ApointmentModel apointmentModel);
    void onFailed(String msg);
    void showProgressBar();
    void hideProgressBar();

    void onSuccess(ApointmentModel.Data data);
    void onLoad();
    void onFinishload();
    void onServer();
    void onnotconnect(String msg);
}
