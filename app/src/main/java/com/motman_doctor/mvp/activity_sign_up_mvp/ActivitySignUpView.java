package com.motman_doctor.mvp.activity_sign_up_mvp;

import com.motman_doctor.models.UserModel;

public interface ActivitySignUpView {
    void onFragmentSignUp1Displayed();
    void onFragmentSignUp2Displayed();
    void onFragmentSignUp3Displayed();
    void onBack();
    void onFailed(String msg);
    void onLoad();
    void onFinishload();
    void onSignupValid(UserModel userModel);
    void onServer();
    void onnotconnect(String msg);

}
