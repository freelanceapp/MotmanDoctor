package com.motman_doctor.mvp.activity_contactus_mvp;

import android.content.Context;
import android.util.Log;

import com.motman_doctor.models.ContactUsModel;
import com.motman_doctor.remote.Api;
import com.motman_doctor.tags.Tags;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityContactusPresenter {
    private Context context;
    private ActivityContactusView view;



    public ActivityContactusPresenter(Context context, ActivityContactusView view) {
        this.context = context;
        this.view = view;

    }

    public void checkData(ContactUsModel contactUsModel) {
        if (contactUsModel.isDataValid(context)) {
                Contactus(contactUsModel);

        }
    }



    private void Contactus(ContactUsModel contactUsModel) {

        view.onLoad();
        Api.getService(Tags.base_url)
                .contactUs(contactUsModel.getName(),contactUsModel.getEmail(),contactUsModel.getSubject(),contactUsModel.getMessage())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.onContactVaild();
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                view.onFailed();
                                //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            view.onFinishload();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onnotconnect(t.getMessage().toLowerCase());
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed();
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });


    }


}
