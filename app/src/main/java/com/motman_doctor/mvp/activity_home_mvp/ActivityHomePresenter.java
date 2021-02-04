package com.motman_doctor.mvp.activity_home_mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.motman_doctor.R;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.remote.Api;
import com.motman_doctor.tags.Tags;
import com.motman_doctor.ui.activity_home.fragments.Fragment_Home;
import com.motman_doctor.ui.activity_home.fragments.Fragment_Chat;
import com.motman_doctor.ui.activity_home.fragments.Fragment_Patient;
import com.motman_doctor.ui.activity_home.fragments.Fragment_More;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHomePresenter {
    private Context context;
    private FragmentManager fragmentManager;
    private HomeActivityView view;
    private Fragment_Patient fragment_patient;
    public Fragment_Home fragment_home;
    private Fragment_Chat fragment_chat;
    private Fragment_More fragment_more;
    private double lat = 0.0, lng = 0.0;
    private Preferences preferences;
    private UserModel userModel;

    public ActivityHomePresenter(Context context, HomeActivityView view, FragmentManager fragmentManager, double lat, double lng) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.view = view;
        this.lat = lat;
        this.lng = lng;
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(context);
        updateTokenFireBase();
        displayFragmentHome();
    }

    @SuppressLint("NonConstantResourceId")
    public void manageFragments(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.chat:
                displayFragmentChat();
                break;
            case R.id.patient:
                displayFragmentPatient();
                break;
            case R.id.more:
                displayFragmentMore();
                break;
            default:
                displayFragmentHome();
                break;
        }
    }

    private void displayFragmentPatient() {
        if (fragment_patient == null) {
            fragment_patient = Fragment_Patient.newInstance(lat, lng);
        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_patient != null && fragment_patient.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_chat != null && fragment_chat.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_chat).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_patient.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_patient).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_patient, "fragment_live").commit();
        }
    }

    private void displayFragmentHome() {
        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_patient != null && fragment_patient.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_patient != null && fragment_patient.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_chat != null && fragment_chat.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_chat).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_home, "fragment_appointment").commit();
        }
    }

    private void displayFragmentChat() {
        if (fragment_chat == null) {
            fragment_chat = Fragment_Chat.newInstance();
        }


        if (fragment_patient != null && fragment_patient.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_patient != null && fragment_patient.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }


        if (fragment_chat.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_chat).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_chat, "fragment_chat").commit();
        }
    }


    private void displayFragmentMore() {
        if (fragment_more == null) {
            fragment_more = Fragment_More.newInstance();
        }

        if (fragment_patient != null && fragment_patient.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_chat != null && fragment_chat.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_chat).commit();
        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }
        if (fragment_patient != null && fragment_patient.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }
        if (fragment_more.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_more).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_more, "fragment_more").commit();
        }
    }

    public void backPress() {
        if (fragment_home != null && fragment_home.isAdded() && fragment_home.isVisible()) {
            view.onFinished();
        } else {
            displayFragmentHome();
            view.onHomeFragmentSelected();
        }
    }

    private void updateTokenFireBase() {
        Log.e("ssss", userModel.getData().getPhone()+" "+userModel.getData().getId());
        if (userModel != null) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        Log.e("token", token);
                        Api.getService(Tags.base_url)
                                .updateFirebaseToken(userModel.getData().getToken(), userModel.getData().getId(), token, "android")
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            userModel.getData().setFireBaseToken(token);
                                            preferences.create_update_userdata(context, userModel);

                                        } else {
                                            try {

                                                Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        try {

                                            if (t.getMessage() != null) {
                                                Log.e("errorToken2", t.getMessage());
                                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        } catch (Exception e) {
                                        }
                                    }
                                });
                    }
                }
            });

        }
    }

}
