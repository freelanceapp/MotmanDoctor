package com.motman_doctor.ui.activity_clinic_reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.motman_doctor.R;
import com.motman_doctor.databinding.ActivityClinicReservationBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.mvp.activity_clinic_reservation_mvp.ActivityClinicReservationPresenter;
import com.motman_doctor.mvp.activity_clinic_reservation_mvp.ActivityClinicReservationView;

import io.paperdb.Paper;

public class ClinicReservationActivity extends AppCompatActivity implements ActivityClinicReservationView {
    private String lang;
    private ActivityClinicReservationBinding binding;
    private String type="clinic";
    private String date ="";
    private String time = "";
    private ActivityClinicReservationPresenter presenter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_clinic_reservation);
        initView();

    }



    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        presenter = new ActivityClinicReservationPresenter(this,this);


        binding.imageBack.setOnClickListener(view -> {
            finish();
        });

        binding.llDate.setOnClickListener(view -> presenter.showDateDialog(getFragmentManager()));
    }

    @Override
    public void onDateSelected(String date) {
        this.date = date;
        binding.tvDate.setText(date);
    }
}