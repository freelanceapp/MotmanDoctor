package com.motman_doctor.ui.activity_patient_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.motman_doctor.R;
import com.motman_doctor.adapters.DrugsAdapter;
import com.motman_doctor.databinding.ActivityMedicalAdviceBinding;
import com.motman_doctor.databinding.ActivityPatientDetailsBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.DrugModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.activity_patient_details_mvp.ActivityPatientDetailsPresenter;
import com.motman_doctor.mvp.activity_patient_details_mvp.ActivityPatientDetailsView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class PatientDetailsActivity extends AppCompatActivity implements ActivityPatientDetailsView {
    private ActivityPatientDetailsBinding binding;
    private String lang;
    private UserModel.User patientModel;
    private ActivityPatientDetailsPresenter presenter;
    private List<DrugModel> drugModelList;
    private DrugsAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_patient_details);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        patientModel = (UserModel.User) intent.getSerializableExtra("data");
    }

    private void initView() {
        drugModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.setModel(patientModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DrugsAdapter(drugModelList,this);
        binding.recView.setAdapter(adapter);
        presenter = new ActivityPatientDetailsPresenter(this,this);
        presenter.getDrugs(patientModel.getId());
        binding.imageBack.setOnClickListener(view -> finish());
    }

    @Override
    public void onSuccess(List<DrugModel> data) {
        binding.view.setVisibility(View.GONE);
        if (data.size()>0){
            drugModelList.addAll(data);
            binding.tvNoData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        binding.view.setVisibility(View.VISIBLE);
        binding.progBar.setVisibility(View.VISIBLE);
        drugModelList.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        binding.progBar.setVisibility(View.GONE);

    }
}