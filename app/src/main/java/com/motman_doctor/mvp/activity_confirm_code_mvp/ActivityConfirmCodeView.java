package com.motman_doctor.mvp.activity_confirm_code_mvp;

public interface ActivityConfirmCodeView {
    void onCounterStarted(String time);
    void onCounterFinished();
    void onCodeFailed(String msg);
    void onSuccess();
}
