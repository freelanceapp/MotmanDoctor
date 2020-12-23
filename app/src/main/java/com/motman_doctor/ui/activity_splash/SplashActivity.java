package com.motman_doctor.ui.activity_splash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.animation.LinearInterpolator;

import com.motman_doctor.R;
import com.motman_doctor.databinding.ActivitySplashBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.UserSettingsModel;
import com.motman_doctor.mvp.activity_splash_mvp.SplashPresenter;
import com.motman_doctor.mvp.activity_splash_mvp.SplashView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.ui.activity_home.HomeActivity;
import com.motman_doctor.ui.activity_language.LanguageActivity;
import com.motman_doctor.ui.activity_login.LoginActivity;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity implements SplashView {
    private ActivitySplashBinding binding;
    private SplashPresenter presenter;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Transition transition = new TransitionSet();
            transition.setInterpolator(new LinearInterpolator());
            transition.setDuration(500);
            getWindow().setEnterTransition(transition);
            getWindow().setExitTransition(transition);

        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        initView();
    }

    private void initView() {
        binding.tv.setText(Html.fromHtml(getString(R.string.logo)));
        presenter = new SplashPresenter(this,this);
        preferences = Preferences.getInstance();
    }


    @Override
    public void onNavigateToLanguageActivity() {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, binding.logo, binding.logo.getTransitionName());
        Intent intent = new Intent(this, LanguageActivity.class);
        startActivityForResult(intent,100,optionsCompat.toBundle());
    }

    @Override
    public void onNavigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNavigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void refreshActivity(String lang) {
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            Paper.init(this);
            Paper.book().write("lang",lang);
            Language.updateResources(this,lang);
            UserSettingsModel model = preferences.getUserSettings(this);
            if (preferences.getUserSettings(this)==null){
                model = new UserSettingsModel();
            }
            model.setLanguageSelected(true);
            preferences.create_update_user_settings(this,model);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        },1500);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK&&data!=null){
            String lang = data.getStringExtra("lang");
            refreshActivity(lang);
        }
    }


}