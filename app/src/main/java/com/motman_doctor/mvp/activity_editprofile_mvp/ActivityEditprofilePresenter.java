package com.motman_doctor.mvp.activity_editprofile_mvp;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.motman_doctor.R;
import com.motman_doctor.models.DiseaseModel;
import com.motman_doctor.models.EditProfileModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.remote.Api;
import com.motman_doctor.share.Common;
import com.motman_doctor.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.motman_doctor.mvp.activity_editprofile_mvp.EditprofileActivityView;
public class ActivityEditprofilePresenter {

    private UserModel userModel;
    private Preferences preferences;
    private EditprofileActivityView view;
    private Context context;


    public ActivityEditprofilePresenter(EditprofileActivityView view, Context context) {
        this.context = context;
        this.view = view;
    }

    public void backPress() {

        view.onFinished();


    }

    public void checkData(EditProfileModel editProfileModel, UserModel userModel) {
        if (editProfileModel.isDataValid(context)) {
            if (!editProfileModel.getImageUrl().isEmpty()) {
                if (editProfileModel.getPassword().isEmpty()) {
                    editprofileimage(editProfileModel, userModel, context);
                } else {
                    editprofileimagepass(editProfileModel, userModel, context);
                }

            } else {
                if (editProfileModel.getPassword().isEmpty()) {

                    editprofile(editProfileModel, userModel);
                } else {
                    editprofilepass(editProfileModel, userModel);
                }
            }
        }
    }

    private void editprofileimage(

            EditProfileModel editProfileModel, UserModel userModel, Context context) {

        RequestBody name_part = Common.getRequestBodyText(editProfileModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(editProfileModel.getPhone_code().replace("+", "00"));
        RequestBody phone_part = Common.getRequestBodyText(editProfileModel.getPhone());
        RequestBody email_part = Common.getRequestBodyText(editProfileModel.getEmail());
        RequestBody job_part = Common.getRequestBodyText(editProfileModel.getJob_title());
        RequestBody user_part = Common.getRequestBodyText(userModel.getData().getId() + "");
        RequestBody time_part = Common.getRequestBodyText(editProfileModel.getTime() + "");


        MultipartBody.Part image_form_part = Common.getMultiPart(context, Uri.parse(editProfileModel.getImageUrl()), "logo");
        view.onLoad();
        Api.getService(Tags.base_url)
                .editprofile("Bearer " + userModel.getData().getToken(), user_part, name_part, email_part,time_part, job_part, image_form_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.onupdateValid(response.body());
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                if (response.code() == 409) {
                                    view.onFailed(context.getString(R.string.phone_found));
                                } else {
                                    view.onFailed(response.message() + "");
                                }                                 //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
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

    private void editprofileimagepass(

            EditProfileModel editProfileModel, UserModel userModel, Context context) {

        RequestBody name_part = Common.getRequestBodyText(editProfileModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(editProfileModel.getPhone_code().replace("+", "00"));
        RequestBody phone_part = Common.getRequestBodyText(editProfileModel.getPhone());
        RequestBody email_part = Common.getRequestBodyText(editProfileModel.getEmail());
        RequestBody job_part = Common.getRequestBodyText(editProfileModel.getJob_title());
        RequestBody user_part = Common.getRequestBodyText(userModel.getData().getId() + "");
        RequestBody pass_part = Common.getRequestBodyText(editProfileModel.getPassword());
        RequestBody time_part = Common.getRequestBodyText(editProfileModel.getTime());


        MultipartBody.Part image_form_part = Common.getMultiPart(context, Uri.parse(editProfileModel.getImageUrl()), "logo");
        view.onLoad();
        Api.getService(Tags.base_url)
                .editprofile("Bearer " + userModel.getData().getToken(), user_part, name_part, email_part,time_part, job_part, pass_part, image_form_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.onupdateValid(response.body());
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                if (response.code() == 409) {
                                    view.onFailed(context.getString(R.string.phone_found));
                                } else {
                                    view.onFailed(response.message() + "");
                                }                                 //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
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

    private void editprofile(EditProfileModel editProfileModel, UserModel userModel) {

        view.onLoad();
        Api.getService(Tags.base_url)
                .editprofile("Bearer " + userModel.getData().getToken(), userModel.getData().getId() + "", editProfileModel.getName(), editProfileModel.getEmail(),editProfileModel.getTime(), editProfileModel.getJob_title())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.onupdateValid(response.body());
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                if (response.code() == 409) {
                                    view.onFailed(context.getString(R.string.phone_found));
                                } else {
                                    view.onFailed(response.message() + "");
                                }                                 //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
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

    private void editprofilepass(EditProfileModel editProfileModel, UserModel userModel) {

        view.onLoad();
        Api.getService(Tags.base_url)
                .editprofile("Bearer " + userModel.getData().getToken(), userModel.getData().getId() + "", editProfileModel.getName(), editProfileModel.getEmail(),editProfileModel.getTime(), editProfileModel.getJob_title(), editProfileModel.getPassword())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.onupdateValid(response.body());
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                if (response.code() == 409) {
                                    view.onFailed(context.getString(R.string.phone_found));
                                } else {
                                    view.onFailed(response.message() + "");
                                }                                 //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
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
