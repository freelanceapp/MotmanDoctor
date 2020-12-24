package com.motman_doctor.ui.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motman_doctor.R;
import com.motman_doctor.adapters.AppointmentAdapter;
import com.motman_doctor.adapters.PatientAdapter;
import com.motman_doctor.databinding.FragmentPatientBinding;
import com.motman_doctor.ui.activity_home.HomeActivity;

import java.util.ArrayList;

public class Fragment_Patient extends Fragment {
    private FragmentPatientBinding binding;
    private double lat = 0.0, lng = 0.0;
    private HomeActivity activity;
    private PatientAdapter adapter;


    public static Fragment_Patient newInstance(double lat, double lng) {
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lng", lng);
        Fragment_Patient fragment_patient = new Fragment_Patient();
        fragment_patient.setArguments(bundle);
        return fragment_patient;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
        }

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(new PatientAdapter(new ArrayList<>(), activity));
        binding.progBar.setVisibility(View.GONE);
    }
}
