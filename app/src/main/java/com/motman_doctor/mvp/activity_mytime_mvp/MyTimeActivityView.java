package com.motman_doctor.mvp.activity_mytime_mvp;

import com.motman_doctor.databinding.DialogAddTimeBinding;
import com.motman_doctor.models.DayModel;
import com.motman_doctor.models.MyTimeModel;

public interface MyTimeActivityView {
    void onFinished();

    void onFailed(String msg);

    void ondata(MyTimeModel body);

    void onLoad();

    void onFinishload();

    void sucese();

    void onServer();


    void onDateSelected(String date, DialogAddTimeBinding binding);
}
