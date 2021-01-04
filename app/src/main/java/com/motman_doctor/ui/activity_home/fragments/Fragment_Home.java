package com.motman_doctor.ui.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        adapter = new AppointmentAdapter(apointmentModelList, activity,this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        presenter = new HomeFragmentPresenter(this, activity);
        presenter.getApointment(userModel);
    }

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

    public void setitem(UserModel.User patient_fk) {
        Intent intent = new Intent(activity, PatientDetailsActivity.class);
        intent.putExtra("data",patient_fk);
        startActivity(intent);
    }
//    public void showdetails(ApointmentModel.Data data) {
//        Intent intent=new Intent(activity, ReservationActivity.class);
//        intent.putExtra("data",data);
//        startActivity(intent);
//    }
}
