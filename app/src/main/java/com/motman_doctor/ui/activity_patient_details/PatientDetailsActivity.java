package com.motman_doctor.ui.activity_patient_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.motman_doctor.R;
import com.motman_doctor.adapters.DrugsAdapter;
import com.motman_doctor.databinding.ActivityMedicalAdviceBinding;
import com.motman_doctor.databinding.ActivityPatientDetailsBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.DrugModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.activity_patient_details_mvp.ActivityPatientDetailsPresenter;
import com.motman_doctor.mvp.activity_patient_details_mvp.ActivityPatientDetailsView;
import com.motman_doctor.ui.activity_live.LiveActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class PatientDetailsActivity extends AppCompatActivity implements ActivityPatientDetailsView {
    private ActivityPatientDetailsBinding binding;
    private String lang;
    private UserModel.User patientModel;
    private ApointmentModel.Data.PatientFk patientFk;
    private ActivityPatientDetailsPresenter presenter;
    private List<DrugModel> drugModelList;
    private DrugsAdapter adapter;
    private int id;
    private String type;
    private static final int REQUEST_PHONE_CALL = 1;
    private Intent intent;
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
        if(intent.getSerializableExtra("data")!=null){
        patientModel = (UserModel.User) intent.getSerializableExtra("data");
    }
        else {
            patientFk=(ApointmentModel.Data.PatientFk)intent.getSerializableExtra("DATA");
            id=intent.getIntExtra("id",0);
            type=intent.getStringExtra("type");
        }
    }

    private void initView() {
        drugModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.setModel(patientModel);
        binding.setPatinetmodel(patientFk);
        binding.setType(type);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DrugsAdapter(drugModelList,this);
        binding.recView.setAdapter(adapter);
        presenter = new ActivityPatientDetailsPresenter(this,this);
        if(patientModel!=null){
        presenter.getDrugs(patientModel.getId());}
        else {
            presenter.getDrugs(patientFk.getId());
        }
binding.imageCall.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(type.equals("online")){
            Intent intent = new Intent(PatientDetailsActivity.this, LiveActivity.class);
            intent.putExtra("room", id);
            startActivity(intent);
        }
        else {
            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", patientFk.getPhone_code() + patientFk.getPhone(), null));
            if (intent != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(PatientDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PatientDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            }
        }
    }
});
        binding.imageBack.setOnClickListener(view -> finish());
        if(patientFk==null){
            binding.imageCall.setVisibility(View.GONE);
        }
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
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (this.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
}