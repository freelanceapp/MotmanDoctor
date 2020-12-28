package com.motman_doctor.mvp.activity_patient_details_mvp;

import android.content.Context;
import android.util.Log;

import com.motman_doctor.R;
import com.motman_doctor.models.DrugDataModel;
import com.motman_doctor.models.PatentDataModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.fragment_patient_mvp.FragmentPatientView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.remote.Api;
import com.motman_doctor.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPatientDetailsPresenter {
    private ActivityPatientDetailsView view;
    private Context context;
    private UserModel userModel;
    private Preferences preference;
    private Call<DrugDataModel> call;

    public ActivityPatientDetailsPresenter(ActivityPatientDetailsView view, Context context) {
        this.view = view;
        this.context = context;
        preference = Preferences.getInstance();
        userModel = preference.getUserData(context);
    }

    public void getDrugs(int patient_id) {
        if (userModel == null) {
            return;
        }

        view.showProgressBar();
        if (call != null) {
            call.cancel();

        }
        call = Api.getService(Tags.base_url).
                getDrugs("Bearer " + userModel.getData().getToken(),userModel.getData().getId(), patient_id);

        call.enqueue(new Callback<DrugDataModel>() {
            @Override
            public void onResponse(Call<DrugDataModel> call, Response<DrugDataModel> response) {

                if (response.isSuccessful()) {
                    view.hideProgressBar();
                    if (response.body() != null) {
                        view.onSuccess(response.body().getData());

                    }

                } else {

                    try {
                        view.onFailed(context.getString(R.string.failed));
                        Log.e("error", response.code() + "_" + response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DrugDataModel> call, Throwable t) {
                try {
                    view.hideProgressBar();
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage() + "__");

                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            view.onFailed(context.getString(R.string.something));

                        } else if (t.getMessage().toLowerCase().equals("Canceled".toLowerCase()) || t.getMessage().toLowerCase().contains("Socket closed".toLowerCase())) {

                        } else {
                            view.onFailed(context.getString(R.string.failed));

                        }
                    }


                } catch (Exception e) {
                }

            }
        });
    }
}