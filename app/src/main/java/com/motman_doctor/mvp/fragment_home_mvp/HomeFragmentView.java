package com.motman_doctor.mvp.fragment_home_mvp;

import com.motman_doctor.models.ApointmentModel;

public interface HomeFragmentView {
    void onSuccess(ApointmentModel apointmentModel);
    void onFailed(String msg);
    void showProgressBar();
    void hideProgressBar();

}
