package com.motman_doctor.ui.activity_contactus;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.motman_doctor.R;
import com.motman_doctor.databinding.ActivityContactUsBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.ContactUsModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.activity_contactus_mvp.ActivityContactUsPresenter;
import com.motman_doctor.mvp.activity_contactus_mvp.ActivityContactUsView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.share.Common;

import io.paperdb.Paper;

public class ContactusActivity extends AppCompatActivity implements ActivityContactUsView {
    private ActivityContactUsBinding binding;

    private ContactUsModel contactUsModel;
    private ActivityContactUsPresenter presenter;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        initView();

    }


    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setModel(userModel);
        Paper.init(this);

        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        contactUsModel = new ContactUsModel();
        binding.setContactModel(contactUsModel);

        presenter = new ActivityContactUsPresenter(this, this);
        binding.btnSend.setOnClickListener(v -> presenter.checkData(contactUsModel));
        binding.llBack.setOnClickListener(v -> finish());

    }


    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, R.string.suc, Toast.LENGTH_SHORT).show();
        finish();
    }


}