package com.motman_doctor.mvp.fragment_caht_room_mvp;

import com.motman_doctor.models.UserRoomModelData;

public interface ChatRoomFragmentView {
    void onFinished();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);

    void ondata(UserRoomModelData body);
}
