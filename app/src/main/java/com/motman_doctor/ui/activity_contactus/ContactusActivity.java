package com.motman_doctor.ui.activity_contactus;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.motman_doctor.R;
import com.motman_doctor.databinding.ActivityContactUsBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.ContactUsModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.activity_contactus_mvp.ActivityContactusPresenter;
import com.motman_doctor.mvp.activity_contactus_mvp.ActivityContactusView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.share.Common;

import io.paperdb.Paper;

public class ContactusActivity extends AppCompatActivity implements ActivityContactusView {
    private ActivityContactUsBinding binding;

    private ContactUsModel contactUsModel;
    private ActivityContactusPresenter presenter;
    private Preferences preferences;
    private String lang;
    private ProgressDialog dialog2;
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

        presenter = new ActivityContactusPresenter(this, this);
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkData(contactUsModel);
            }
        });
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onLoad() {
        dialog2 = Common.createProgressDialog(this, getString(R.string.wait));
        dialog2.setCancelable(false);
        dialog2.show();
    }

    @Override
    public void onFinishload() {
        dialog2.dismiss();
    }

    @Override
    public void onContactVaild() {
        finish();

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(ContactusActivity.this, msg, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onFailed() {
        Toast.makeText(ContactusActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServer() {
        Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();

    }
}