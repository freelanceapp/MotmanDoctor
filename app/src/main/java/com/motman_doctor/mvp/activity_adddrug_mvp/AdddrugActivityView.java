package com.motman_doctor.mvp.activity_adddrug_mvp;


public interface AdddrugActivityView {
    void onFinished();
    void onLoad();
    void onFinishload();
    void onFailed(String msg);

    void onsucess();
    void onServer();

}
