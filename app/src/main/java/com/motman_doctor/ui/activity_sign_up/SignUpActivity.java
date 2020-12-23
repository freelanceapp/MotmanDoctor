package com.motman_doctor.ui.activity_sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.motman_doctor.R;
import com.motman_doctor.databinding.ActivitySignUpBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.SignUpModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.activity_sign_up_mvp.ActivitySignUpPresenter;
import com.motman_doctor.mvp.activity_sign_up_mvp.ActivitySignUpView;
import com.motman_doctor.ui.activity_login.LoginActivity;

import java.util.List;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity implements ActivitySignUpView{
    private ActivitySignUpBinding binding;
    private FragmentManager fragmentManager;
    private ActivitySignUpPresenter presenter;
    private String phone_code="";
    private String phone="";
    private SignUpModel signUpModel;




    @Override
    protected void attachBaseContext(Context newBase)
    {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        phone_code = intent.getStringExtra("phone_code");
        phone = intent.getStringExtra("phone");

    }

    private void initView()
    {
        fragmentManager = getSupportFragmentManager();
        signUpModel = new SignUpModel(phone_code,phone);
        presenter = new ActivitySignUpPresenter(this,this,fragmentManager,signUpModel);
        binding.btnNext.setOnClickListener(view -> {
            presenter.manageData(false);
        });

        binding.btnPrevious.setOnClickListener(view -> {
            presenter.manageData(true);
        });
    }

    @Override
    public void onFragmentSignUp1Displayed() {
        binding.tv1.setBackgroundResource(R.drawable.circle_bg);
        binding.tv1.setTextColor(ContextCompat.getColor(this,R.color.white));

        binding.tv2.setBackgroundResource(R.drawable.circle_primary_stroke);
        binding.tv2.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv3.setBackgroundResource(R.drawable.circle_primary_stroke);
        binding.tv3.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.btnPrevious.setVisibility(View.GONE);
    }

    @Override
    public void onFragmentSignUp2Displayed() {
        binding.tv1.setBackgroundResource(R.drawable.circle_primary_stroke);
        binding.tv1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv2.setBackgroundResource(R.drawable.circle_bg);
        binding.tv2.setTextColor(ContextCompat.getColor(this,R.color.white));

        binding.tv3.setBackgroundResource(R.drawable.circle_primary_stroke);
        binding.tv3.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.btnPrevious.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentSignUp3Displayed() {
        binding.tv1.setBackgroundResource(R.drawable.circle_primary_stroke);
        binding.tv1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv2.setBackgroundResource(R.drawable.circle_primary_stroke);
        binding.tv2.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv3.setBackgroundResource(R.drawable.circle_bg);
        binding.tv3.setTextColor(ContextCompat.getColor(this,R.color.white));

        binding.btnPrevious.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBack() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    public void onSuccess(UserModel userModel) {

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment :fragmentList){
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment :fragmentList){
            fragment.onActivityResult(requestCode, resultCode, data);
        }



    }

    @Override
    public void onBackPressed() {
        presenter.manageData(true);
    }
}