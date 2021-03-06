package com.motman_doctor.ui.activity_home.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.motman_doctor.databinding.DialogChooseAddDrugBinding;
import com.motman_doctor.databinding.FragmentHomeBinding;
import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.fragment_home_mvp.HomeFragmentPresenter;
import com.motman_doctor.mvp.fragment_home_mvp.HomeFragmentView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.share.Common;
import com.motman_doctor.ui.activity_emergency.AddDrugActivity;
import com.motman_doctor.ui.activity_home.HomeActivity;
import com.motman_doctor.ui.activity_live.LiveActivity;
import com.motman_doctor.ui.activity_patient_details.PatientDetailsActivity;
import com.motman_doctor.ui.activity_sign_up.SignUpActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

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
    private Dialog dialog2;
    private ApointmentModel.Data data;

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
        if (userModel != null) {
            presenter.getApointment(userModel);
        }
    }

    @Override
    public void onSuccess(ApointmentModel apointmentModel) {
        apointmentModelList.clear();
        apointmentModelList.addAll(apointmentModel.getData());
        adapter.notifyDataSetChanged();
        if(apointmentModelList.size()==0){
            binding.tvNoData.setVisibility(View.VISIBLE);
        }
        else {
            binding.tvNoData.setVisibility(View.GONE);
        }

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

    @Override
    public void onSuccess(ApointmentModel.Data data) {
        if(data.getReservation_type().equals("online")){
            Intent intent = new Intent(activity, LiveActivity.class);
            intent.putExtra("room", data.getId());
            intent.putExtra("type", data.getReservation_type());
            startActivityForResult(intent, 1);}
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

    public void setitem(ApointmentModel.Data data, int id, String reservation_type,String status) {
        Intent intent = new Intent(activity, PatientDetailsActivity.class);
        intent.putExtra("DATA", data);
        intent.putExtra("id", id);
        intent.putExtra("type", reservation_type);
        intent.putExtra("status",status);
        startActivity(intent);
    }

    public void open(ApointmentModel.Data data) {
//        String date = data.getDate()+" " + data.getTime()+" "+data.getTime_type();
//        Log.e("kdkdk", date);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa", Locale.US);
//        long datetime = 0;
//        try {
//            datetime = sdf.parse(date).getTime();
//        } catch (ParseException e) {
//           Log.e("dldkkd",e.toString());
//        }
//        long currenttime = System.currentTimeMillis();
//        Log.e("kdkdk", date+" "+currenttime+" "+datetime);

      //  if (currenttime >= datetime) {
//            if (data.getReservation_type().equals("online")) {
        this.data=data;
             presenter.opencall(data,userModel);
//            } else {
//                intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data.getPatient_fk().getPhone_code() + data.getPatient_fk().getPhone(), null));
//                if (intent != null) {
//                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
//                        } else {
//                            startActivity(intent);
//                        }
//                    } else {
//                        startActivity(intent);
//                    }
//                }
//            }
//        } else {
//            Toast.makeText(activity, activity.getResources().getString(R.string.not_avail_now), Toast.LENGTH_LONG).show();
//        }

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
    @Override
    public void onLoad() {
        dialog2 = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog2.setCancelable(false);
        dialog2.show();
    }

    @Override
    public void onFinishload() {
        dialog2.dismiss();
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

    }



    public void getdata() {
        apointmentModelList.clear();
        adapter.notifyDataSetChanged();
        presenter.getApointment(userModel);
    }

    @Override
    public void oncloseSuccess() {
        CreateDialogAlert(activity);
    }

    @Override
    public void onServer() {
        Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();

    }
    public  void CreateDialogAlert(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogChooseAddDrugBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_choose_add_drug, null, false);

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(activity, AddDrugActivity.class);
                intent.putExtra("DATA", data);

                startActivity(intent);

            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                apointmentModelList.clear();
                adapter.notifyDataSetChanged();
                presenter.getApointment(userModel);
            }
        });
        binding.btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.opencall(data,userModel);
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();

    }
//    public void showdetails(ApointmentModel.Data data) {
//        Intent intent=new Intent(activity, ReservationActivity.class);
//        intent.putExtra("data",data);
//        startActivity(intent);
//    }
@Override
public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode==RESULT_OK&&requestCode==1){
        presenter.closecall(this.data, userModel);
    }
}
}
