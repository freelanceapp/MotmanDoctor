package com.motman_doctor.mvp.fragment_signup2_mvp;

import android.content.Context;
import android.util.Log;

import com.motman_doctor.R;
import com.motman_doctor.models.AllCityModel;
import com.motman_doctor.models.AllSpiclixationModel;
import com.motman_doctor.remote.Api;
import com.motman_doctor.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupPresenter {
    private SignupFragmentView view;
    private Context context;
    private List<String> genderList;

    public SignupPresenter(SignupFragmentView view, Context context) {
        this.view = view;
        this.context = context;
        genderList=new ArrayList<>();
    }

    public void getSpecilization()
    {
        // Log.e("tjtjtj",userModel.getIs_confirmed());
        view.onLoad();

        Api.getService(Tags.base_url)
                .getspicailest()
                .enqueue(new Callback<AllSpiclixationModel>() {
                    @Override
                    public void onResponse(Call<AllSpiclixationModel> call, Response<AllSpiclixationModel> response) {
                       view.onFinishload();

                        if (response.isSuccessful() && response.body() != null) {
                            view.onSuccess(response.body());
                        } else {
                           view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_codess",response.code()+ response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AllSpiclixationModel> call, Throwable t) {
                        try {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Errorsss", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
    public void getcities()
    {
        // Log.e("tjtjtj",userModel.getIs_confirmed());
view.onLoad();
        Api.getService(Tags.base_url)
                .getcities()
                .enqueue(new Callback<AllCityModel>() {
                    @Override
                    public void onResponse(Call<AllCityModel> call, Response<AllCityModel> response) {
                        view.onFinishload();

                        if (response.isSuccessful() && response.body() != null) {
                            view.onSuccesscitie(response.body());
                        } else {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_codess",response.code()+ response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AllCityModel> call, Throwable t) {
                        try {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
    public void getGender() {
        genderList.clear();
        genderList.add(context.getResources().getString(R.string.ch_gender));
        genderList.add(context.getResources().getString(R.string.male));
        genderList.add(context.getResources().getString(R.string.female));
        view.onGenderSuccess(genderList);

    }

}