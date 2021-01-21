package com.motman_doctor.ui.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import com.motman_doctor.R;
import com.motman_doctor.databinding.ActivityHomeBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.mvp.activity_home_mvp.ActivityHomePresenter;
import com.motman_doctor.mvp.activity_home_mvp.HomeActivityView;
import com.motman_doctor.ui.activity_clinic_reservation.ClinicReservationActivity;
import com.motman_doctor.ui.activity_login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.motman_doctor.ui.activity_notifications.NotificationActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements HomeActivityView {
    private ActivityHomeBinding binding;
    private FragmentManager fragmentManager;
    private ActivityHomePresenter presenter;
    private double lat=0.0,lng=0.0;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent  = getIntent();
        lat = intent.getDoubleExtra("lat",0.0);
        lng = intent.getDoubleExtra("lng",0.0);
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        presenter = new ActivityHomePresenter(this, this, fragmentManager,lat,lng);
        binding.navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                presenter.manageFragments(item);
                return true;
            }
        });


        binding.flReserveClinic.setOnClickListener(view -> {
            Intent intent = new Intent(this, ClinicReservationActivity.class);
            startActivity(intent);
        });


        binding.flNotification.setOnClickListener(view -> {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        });

    }


    public void refreshActivity(String lang) {
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            Paper.init(this);
            Paper.book().write("lang",lang);
            Language.updateResources(this,lang);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        },1500);


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        List<Fragment> fragments = fragmentManager.getFragments();
//        for (Fragment fragment : fragments) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        presenter.backPress();
    }

    @Override
    public void onHomeFragmentSelected() {
        binding.navigationView.setSelectedItemId(R.id.home);
    }


    @Override
    public void onNavigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(presenter.fragment_home!=null){
            presenter.fragment_home.getdata();
        }
    }

    @Override
    public void onFinished() {
        finish();
    }
}