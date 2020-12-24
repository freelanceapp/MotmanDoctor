package com.motman_doctor.mvp.fragment_more_mvp;

import com.motman_doctor.models.SettingModel;

public interface MoreFragmentView {
    void onFailed(String msg);

    void onLoad();

    void onFinishload();


    void logout();

    void onsetting(SettingModel body);
    void ViewSocial(String path);
}
