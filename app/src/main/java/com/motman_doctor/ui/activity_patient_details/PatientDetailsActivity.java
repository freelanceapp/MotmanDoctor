package com.motman_doctor.ui.activity_patient_details;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.motman_doctor.R;
import com.motman_doctor.adapters.DrugsAdapter;
import com.motman_doctor.databinding.ActivityPatientDetailsBinding;
import com.motman_doctor.databinding.DialogAlertBinding;
import com.motman_doctor.databinding.DialogChooseAddDrugBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.DrugModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.activity_patient_details_mvp.ActivityPatientDetailsPresenter;
import com.motman_doctor.mvp.activity_patient_details_mvp.ActivityPatientDetailsView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.share.Common;
import com.motman_doctor.ui.activity_emergency.AddDrugActivity;
import com.motman_doctor.ui.activity_live.LiveActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class PatientDetailsActivity extends AppCompatActivity implements ActivityPatientDetailsView {
    private ActivityPatientDetailsBinding binding;
    private String lang;
    private UserModel.User patientModel;
    private ApointmentModel.Data data;
    private ActivityPatientDetailsPresenter presenter;
    private List<DrugModel> drugModelList;
    private DrugsAdapter adapter;
    private int id;
    private String type;
    private static final int REQUEST_PHONE_CALL = 1;
    private Intent intent;
    private String status;
    private Dialog dialog2;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_details);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("data") != null) {
            patientModel = (UserModel.User) intent.getSerializableExtra("data");
        } else {
            data = (ApointmentModel.Data) intent.getSerializableExtra("DATA");
            id = intent.getIntExtra("id", 0);
            type = intent.getStringExtra("type");
            status = intent.getStringExtra("status");
        }
    }

    private void initView() {
        drugModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(patientModel);
        if (data != null) {
            binding.setPatinetmodel(data.getPatient_fk());
        }
        binding.setType(type);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DrugsAdapter(drugModelList, this);
        binding.recView.setAdapter(adapter);
        presenter = new ActivityPatientDetailsPresenter(this, this);
        if (patientModel != null) {
            presenter.getDrugs(patientModel.getId());
        } else {
            presenter.getDrugs(data.getPatient_fk().getId());
        }
        binding.imageAdddrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDetailsActivity.this, AddDrugActivity.class);
                intent.putExtra("DATA", data);

                startActivity(intent);
                finish();
            }
        });
        binding.imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (type.equals("online")) {
                presenter.opencall(data, userModel);
//                } else {
//                    intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", patientFk.getPhone_code() + patientFk.getPhone(), null));
//                    if (intent != null) {
//                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (ContextCompat.checkSelfPermission(PatientDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                                ActivityCompat.requestPermissions(PatientDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
//                            } else {
//                                startActivity(intent);
//                            }
//                        } else {
//                            startActivity(intent);
//                        }
//                    }
//                }
            }
        });
        binding.imageBack.setOnClickListener(view -> finish());
        if (data == null) {
            binding.imageCall.setVisibility(View.GONE);
            binding.imageAdddrug.setVisibility(View.GONE);
        } else {
            String myTime = data.getDate()+" "+data.getTime()+" "+ data.getTime_type();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa", Locale.US);
            Date d = null;
            Log.e("dlldl",myTime);
            try {
                d = df.parse(myTime);
            } catch (ParseException e) {
                Log.e("llflfl",e.toString());
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, Integer.parseInt(userModel.getData().getDetection_time()));
            long time = System.currentTimeMillis();

            if(time>cal.getTimeInMillis()){
                if (data.getReservation_type().equals("normal")) {
                    binding.imageAdddrug.setVisibility(View.VISIBLE);

                }else {
                    binding.imageAdddrug.setVisibility(View.GONE);

                }
                    binding.imageCall.setVisibility(View.GONE);

            }
            else {
                binding.imageCall.setVisibility(View.VISIBLE);

                if (data.getReservation_type().equals("normal")) {
                    binding.imageAdddrug.setVisibility(View.VISIBLE);

                } else {
                    binding.imageAdddrug.setVisibility(View.GONE);


                }
            }
//            if (status.equals("open")) {
//                binding.imageCall.setVisibility(View.VISIBLE);
//
//            }
//            else {
//                binding.imageCall.setVisibility(View.GONE);
//
//            }
        }
    }

    @Override
    public void oncloseSuccess(List<DrugModel> data) {
        binding.view.setVisibility(View.GONE);
        if (data.size() > 0) {
            drugModelList.addAll(data);
            binding.tvNoData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } else {
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
    public void oncopenSuccess(ApointmentModel.Data data) {
        if (data.getReservation_type().equals("online")) {
            Intent intent = new Intent(PatientDetailsActivity.this, LiveActivity.class);
            intent.putExtra("room", data.getId());
            intent.putExtra("type", data.getReservation_type());
            startActivityForResult(intent, 1);
        } else {
            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data.getPatient_fk().getPhone_code() + data.getPatient_fk().getPhone(), null));
            if (intent != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            presenter.closecall(this.data, userModel);
        }
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

    @Override
    public void onLoad() {
        dialog2 = Common.createProgressDialog(PatientDetailsActivity.this, getString(R.string.wait));
        dialog2.setCancelable(false);
        dialog2.show();
    }

    @Override
    public void onFinishload() {
        dialog2.dismiss();
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(PatientDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void oncloseSuccess() {
        CreateDialogAlert(this);
    }

    @Override
    public void onServer() {
        Toast.makeText(PatientDetailsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();

    }

    public void CreateDialogAlert(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogChooseAddDrugBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_choose_add_drug, null, false);

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(PatientDetailsActivity.this, AddDrugActivity.class);
                intent.putExtra("DATA", data);

                startActivity(intent);
                finish();
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                dialog.dismiss();

            }
        });
        binding.btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                presenter.opencall(data, userModel);

//                Intent intent = new Intent(PatientDetailsActivity.this, LiveActivity.class);
//                intent.putExtra("room", data.getId());
//                intent.putExtra("type", data.getReservation_type());
//                startActivityForResult(intent, 1);
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();

    }
}