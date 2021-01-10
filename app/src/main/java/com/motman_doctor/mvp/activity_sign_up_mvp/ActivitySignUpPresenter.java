package com.motman_doctor.mvp.activity_sign_up_mvp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.motman_doctor.R;
import com.motman_doctor.models.SignUpModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.remote.Api;
import com.motman_doctor.share.Common;
import com.motman_doctor.tags.Tags;
import com.motman_doctor.ui.activity_sign_up.fragments.FragmentSignUp1;
import com.motman_doctor.ui.activity_sign_up.fragments.FragmentSignUp2;
import com.motman_doctor.ui.activity_sign_up.fragments.FragmentSignUp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySignUpPresenter {
    private Context context;
    private ActivitySignUpView view;
    private FragmentManager fragmentManager;
    private FragmentSignUp1 fragmentSignUp1;
    private FragmentSignUp2 fragmentSignUp2;
    private FragmentSignUp3 fragmentSignUp3;
    private SignUpModel signUpModel;


    public ActivitySignUpPresenter(Context context, ActivitySignUpView view, FragmentManager fragmentManager, SignUpModel signUpModel) {
        this.context = context;
        this.view = view;
        this.fragmentManager = fragmentManager;
        this.signUpModel = signUpModel;
        displayFragment1();

    }

    public void manageData(boolean isBack) {
        if (fragmentSignUp1 != null && fragmentSignUp1.isAdded() && fragmentSignUp1.isVisible()) {
            this.signUpModel = fragmentSignUp1.signUpModel;
            if (isBack) {
                view.onBack();
            } else {
                if (signUpModel.isStep1Valid(context)) {
                    displayFragment2();
                }
            }
        } else if (fragmentSignUp2 != null && fragmentSignUp2.isAdded() && fragmentSignUp2.isVisible()) {
            this.signUpModel = fragmentSignUp2.signUpModel;

            if (isBack) {
                displayFragment1();
            } else {

                if (this.signUpModel.isStep2Valid(context)) {
                    displayFragment3();
                }
            }
        } else if (fragmentSignUp3 != null && fragmentSignUp3.isAdded() && fragmentSignUp3.isVisible()) {
            this.signUpModel = fragmentSignUp3.signUpModel;

            if (isBack) {
                displayFragment2();
            } else {
                if (this.signUpModel.isStep3Valid(context)) {
                    signUp(signUpModel);
                }
            }
        }
    }

    private void displayFragment1() {
        if (fragmentSignUp1 == null) {
            fragmentSignUp1 = FragmentSignUp1.newInstance(signUpModel);
        }

        if (fragmentSignUp2 != null && fragmentSignUp2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp2).commit();
        }

        if (fragmentSignUp3 != null && fragmentSignUp3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp3).commit();
        }

        if (fragmentSignUp1.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentSignUp1).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentSignUp1, "fragmentSignUp1").commit();
        }
        view.onFragmentSignUp1Displayed();
    }

    private void displayFragment2() {
        if (fragmentSignUp2 == null) {
            fragmentSignUp2 = FragmentSignUp2.newInstance(signUpModel);
        }

        if (fragmentSignUp1 != null && fragmentSignUp1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp1).commit();
        }

        if (fragmentSignUp3 != null && fragmentSignUp3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp3).commit();
        }

        if (fragmentSignUp2.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentSignUp2).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentSignUp2, "fragmentSignUp2").commit();
        }
        view.onFragmentSignUp2Displayed();
    }

    private void displayFragment3() {
        if (fragmentSignUp3 == null) {
            fragmentSignUp3 = FragmentSignUp3.newInstance(signUpModel);
        }

        if (fragmentSignUp1 != null && fragmentSignUp1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp1).commit();
        }

        if (fragmentSignUp2 != null && fragmentSignUp2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp2).commit();
        }

        if (fragmentSignUp3.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentSignUp3).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentSignUp3, "fragmentSignUp3").commit();
        }
        view.onFragmentSignUp3Displayed();
    }


    private void signUp(SignUpModel signUpModel) {
        if (signUpModel.getImagelist() == null || signUpModel.getImagelist().size() == 0) {
            sign_up_with_image(signUpModel);
        } else {
            sign_up_with_out_image(signUpModel);
        }

    }

    private void sign_up_with_image(SignUpModel signUpModel) {
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(signUpModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody lat_part = Common.getRequestBodyText(signUpModel.getLat() + "");
        RequestBody lng_part = Common.getRequestBodyText(signUpModel.getLng() + "");
        RequestBody gender_part = Common.getRequestBodyText(signUpModel.getGender());
        RequestBody address_part = Common.getRequestBodyText(signUpModel.getAddress());
        RequestBody spicial_part = Common.getRequestBodyText(signUpModel.getSpecialization_id() + "");
        RequestBody city_part = Common.getRequestBodyText(signUpModel.getCity_id() + "");
        RequestBody email_part = Common.getRequestBodyText(signUpModel.getEmail());

        RequestBody type_part = Common.getRequestBodyText("doctor");
        RequestBody soft_part = Common.getRequestBodyText("android");


        MultipartBody.Part image_form_part = Common.getMultiPart(context, Uri.parse(signUpModel.getImageUrl()), "logo");
        List<MultipartBody.Part> imageparts = getMultiPartImages(signUpModel.getImagelist());
        RequestBody nationl_part = Common.getRequestBodyText(signUpModel.getNationalid());
        RequestBody syndicateidnumber_part = Common.getRequestBodyText(signUpModel.getSyndicateidnumber());
        RequestBody pass_part = Common.getRequestBodyText(signUpModel.getPassword());

        view.onLoad();
        Api.getService(Tags.base_url)
                .signup(phone_code_part, phone_part, name_part, lat_part, lng_part, address_part, gender_part, type_part, soft_part, spicial_part, city_part, email_part, nationl_part, pass_part, syndicateidnumber_part, image_form_part, imageparts)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            view.onSignupValid(response.body());
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                if (response.code() == 406) {
                                    view.onFailed(context.getString(R.string.phone_found));
                                } else {
                                    view.onFailed(response.message() + "");
                                }
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
                                    view.onFailed(context.getResources().getString(R.string.failed));
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void sign_up_with_out_image(SignUpModel signUpModel) {
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(signUpModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody lat_part = Common.getRequestBodyText(signUpModel.getLat() + "");
        RequestBody lng_part = Common.getRequestBodyText(signUpModel.getLng() + "");
        RequestBody gender_part = Common.getRequestBodyText(signUpModel.getGender());
        RequestBody address_part = Common.getRequestBodyText(signUpModel.getAddress());
        RequestBody spicial_part = Common.getRequestBodyText(signUpModel.getSpecialization_id() + "");
        RequestBody city_part = Common.getRequestBodyText(signUpModel.getCity_id() + "");
        RequestBody email_part = Common.getRequestBodyText(signUpModel.getEmail());
        RequestBody nationl_part = Common.getRequestBodyText(signUpModel.getNationalid());
        RequestBody syndicateidnumber_part = Common.getRequestBodyText(signUpModel.getSyndicateidnumber());
        RequestBody type_part = Common.getRequestBodyText("doctor");
        RequestBody soft_part = Common.getRequestBodyText("android");

        RequestBody pass_part = Common.getRequestBodyText(signUpModel.getPassword());

        List<MultipartBody.Part> imageparts = getMultiPartImages(signUpModel.getImagelist());

        view.onLoad();
        Api.getService(Tags.base_url)
                .signup(phone_code_part, phone_part, name_part, lat_part, lng_part, address_part, gender_part, type_part, soft_part, spicial_part, city_part, email_part, nationl_part, pass_part, syndicateidnumber_part, imageparts)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.onSignupValid(response.body());
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {

                                if (response.code() == 406) {
                                    view.onFailed(context.getString(R.string.phone_found));
                                } else {
                                    view.onFailed(response.message() + "");
                                }                                //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    view.onFailed(context.getResources().getString(R.string.failed));
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private List<MultipartBody.Part> getMultiPartImages(List<String> imagesList) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (String uri : imagesList) {
            if (uri != null) {
                MultipartBody.Part part = Common.getMultiPartImage(context, Uri.parse(uri), "doctor_license_images[]");
                parts.add(part);
            }

        }
        return parts;
    }
}
