package com.motman_doctor.mvp.activity_adddrug_mvp;

import android.content.Context;
import android.util.Log;


import com.motman_doctor.R;
import com.motman_doctor.models.AdddrugModel;
import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.remote.Api;
import com.motman_doctor.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAdddrugPresenter {
    private UserModel userModel;
    private Preferences preferences;
    private AdddrugActivityView view;
    private Context context;
private List<String > drag_name;
private List<String>take_num;
    private List<String>detilas;

    public ActivityAdddrugPresenter(AdddrugActivityView view, Context context) {
        this.view = view;
        this.context = context;
       drag_name=new ArrayList<>();
       take_num=new ArrayList<>();
       detilas=new ArrayList<>();
    }

    public void backPress() {

        view.onFinished();


    }


    public void checkdata(List<AdddrugModel> adddrugModelList, UserModel userModel, ApointmentModel.Data data) {
        drag_name.clear();
        take_num.clear();
        detilas.clear();
        int check=-1;
        for(int i=0;i<adddrugModelList.size();i++){
            if(adddrugModelList.get(i).isDataValid(context)){
drag_name.add(adddrugModelList.get(i).getName());
take_num.add(adddrugModelList.get(i).getDose());
detilas.add(adddrugModelList.get(i).getDetials());
check=0;
            }
            else {
                check=-1;

            }

        }
        if(check!=-1){
Adddrug(userModel,data);
        }
    }
    private void Adddrug(UserModel userModel,ApointmentModel.Data data) {

        view.onLoad();
        Api.getService(Tags.base_url)
                .Adddrug("Bearer "+userModel.getData().getToken(),userModel.getData().getId()+"",data.getPatient_fk().getId()+"",data.getId()+"",drag_name,take_num,detilas)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.onsucess();
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {

                                    view.onFailed(context.getResources().getString(R.string.failed) + "");
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
                                    view.onFailed(t.getMessage().toLowerCase());
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed(t.getMessage());
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
