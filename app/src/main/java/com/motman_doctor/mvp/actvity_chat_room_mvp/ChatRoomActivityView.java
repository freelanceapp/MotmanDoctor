package com.motman_doctor.mvp.actvity_chat_room_mvp;

import com.motman_doctor.models.UserRoomModelData;

public interface ChatRoomActivityView {
    void onFinished();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);

    void ondata(UserRoomModelData body);
}
