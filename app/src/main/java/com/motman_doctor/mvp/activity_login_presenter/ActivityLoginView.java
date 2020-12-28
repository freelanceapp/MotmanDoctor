package com.motman_doctor.mvp.activity_login_presenter;

import com.motman_doctor.models.UserModel;

public interface ActivityLoginView {
    void onSuccess(UserModel userModel);
    void onFailed(String msg);
}
