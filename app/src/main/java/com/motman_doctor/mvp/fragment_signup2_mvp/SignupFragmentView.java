package com.motman_doctor.mvp.fragment_signup2_mvp;


import com.motman_doctor.models.AllCityModel;
import com.motman_doctor.models.AllSpiclixationModel;

import java.util.List;

public interface SignupFragmentView {
    void onSuccess(AllSpiclixationModel apointmentModel);
    void onFailed(String msg);
    void onLoad();
    void onFinishload();

    void onSuccesscitie(AllCityModel body);

    void onGenderSuccess(List<String> genderList);
}
