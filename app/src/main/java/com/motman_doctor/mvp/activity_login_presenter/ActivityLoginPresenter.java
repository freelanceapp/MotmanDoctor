package com.motman_doctor.mvp.activity_login_presenter;

import android.content.Context;

import com.motman_doctor.models.LoginModel;

public class ActivityLoginPresenter {
    private Context context;
    private ActivityLoginView view;
    private LoginModel model;

    public ActivityLoginPresenter(Context context, ActivityLoginView view) {
        this.context = context;
        this.view = view;
    }

    public void checkData(LoginModel loginModel){
        this.model = loginModel;
        if (model.isDataValid(context)){
            view.onLoginValid();
        }
    }
}
