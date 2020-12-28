package com.motman_doctor.mvp.activity_contactus_mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.motman_doctor.R;
import com.motman_doctor.models.ContactUsModel;
import com.motman_doctor.remote.Api;
import com.motman_doctor.share.Common;
import com.motman_doctor.tags.Tags;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityContactUsPresenter {
    private Context context;
    private ActivityContactUsView view;


    public ActivityContactUsPresenter(Context context, ActivityContactUsView view) {
        this.context = context;
        this.view = view;

    }

    public void checkData(ContactUsModel contactUsModel) {
        if (contactUsModel.isDataValid(context)) {
            ContactUs(contactUsModel);

        }
    }


    private void ContactUs(ContactUsModel contactUsModel) {

        ProgressDialog dialog = Common.createProgressDialog(context,context.getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .contactUs(contactUsModel.getName(), contactUsModel.getEmail(), contactUsModel.getSubject(), contactUsModel.getMessage())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                           view.onSuccess();
                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onFailed(context.getString(R.string.server_error));
                            } else {
                                view.onFailed(context.getString(R.string.failed));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onFailed(context.getString(R.string.something));
                                } else {
                                    view.onFailed(context.getString(R.string.failed));

                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });


    }


}
