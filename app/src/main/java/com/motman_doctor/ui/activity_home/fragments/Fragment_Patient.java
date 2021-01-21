package com.motman_doctor.ui.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

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
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.fragment_patient_mvp.FragmentPatientPresenter;
import com.motman_doctor.mvp.fragment_patient_mvp.FragmentPatientView;
import com.motman_doctor.ui.activity_home.HomeActivity;
import com.motman_doctor.ui.activity_patient_details.PatientDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Patient extends Fragment implements FragmentPatientView {
    private FragmentPatientBinding binding;
    private double lat = 0.0, lng = 0.0;
    private HomeActivity activity;
    private PatientAdapter adapter;
    private FragmentPatientPresenter presenter;
    private List<UserModel.User> userList;


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
        userList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
        }

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new PatientAdapter(userList, activity, this);
        binding.recView.setAdapter(adapter);
        presenter = new FragmentPatientPresenter(this, activity);
        presenter.getPatient("");

        binding.edtSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.edtSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    presenter.getPatient(query);
                }
            }
            return false;
        });

        binding.imageSearch.setOnClickListener(view -> {
            String query = binding.edtSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                presenter.getPatient(query);
            }
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                if (!query.isEmpty()) {
                    presenter.getPatient(query);
                } else {
                    presenter.getPatient("all");
                }
            }
        });

    }

    @Override
    public void onSuccess(List<UserModel.User> data) {
        if (data.size() > 0) {
            binding.tvNoData.setVisibility(View.GONE);
            userList.addAll(data);
            adapter.notifyDataSetChanged();
        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        userList.clear();
        adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        binding.progBar.setVisibility(View.GONE);

    }

    public void setItemData(UserModel.User user) {
        Intent intent = new Intent(activity, PatientDetailsActivity.class);
        intent.putExtra("data", user);
        startActivity(intent);
    }
}
