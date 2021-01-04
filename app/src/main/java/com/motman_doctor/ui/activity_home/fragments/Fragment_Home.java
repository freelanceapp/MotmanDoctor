package com.motman_doctor.ui.activity_home.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motman_doctor.R;
import com.motman_doctor.adapters.AppointmentAdapter;
import com.motman_doctor.databinding.FragmentHomeBinding;
import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.fragment_home_mvp.HomeFragmentPresenter;
import com.motman_doctor.mvp.fragment_home_mvp.HomeFragmentView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.ui.activity_home.HomeActivity;
import com.motman_doctor.ui.activity_live.LiveActivity;
import com.motman_doctor.ui.activity_patient_details.PatientDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment implements HomeFragmentView {
    private FragmentHomeBinding binding;
    private AppointmentAdapter adapter;
    private HomeActivity activity;
    private HomeFragmentPresenter presenter;
    private List<ApointmentModel.Data> apointmentModelList;
    private Preferences preferences;

    private static final int REQUEST_PHONE_CALL = 1;
    private Intent intent;
    private UserModel userModel;

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        apointmentModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        adapter = new AppointmentAdapter(apointmentModelList, activity, this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        presenter = new HomeFragmentPresenter(this, activity);
        if(userModel!=null){
        presenter.getApointment(userModel);
    }}

    @Override
    public void onSuccess(ApointmentModel apointmentModel) {
        apointmentModelList.clear();
        apointmentModelList.addAll(apointmentModel.getData());
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        binding.progBar.setVisibility(View.GONE);

    }

    public void setitem(ApointmentModel.Data.PatientFk patient_fk, int id, String reservation_type) {
        Intent intent = new Intent(activity, PatientDetailsActivity.class);
        intent.putExtra("DATA", patient_fk);
        intent.putExtra("id",id);
        intent.putExtra("type",reservation_type);
        startActivity(intent);
    }

    public void open(ApointmentModel.Data data) {
        Log.e("lkdkdk",data.reservation_type);
        if (data.getReservation_type().equals("online")) {
            Intent intent = new Intent(activity, LiveActivity.class);
            intent.putExtra("room", data.getId());
            startActivity(intent);
        }
        else {
            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data.getPatient_fk().getPhone_code() + data.getPatient_fk().getPhone(), null));
            if (intent != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (activity.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(intent);
                } else {

                }
                return;
            }
        }
    }
//    public void showdetails(ApointmentModel.Data data) {
//        Intent intent=new Intent(activity, ReservationActivity.class);
//        intent.putExtra("data",data);
//        startActivity(intent);
//    }
}
