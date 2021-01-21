package com.motman_doctor.ui.activity_emergency;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;


import com.motman_doctor.R;
import com.motman_doctor.adapters.AdddrugAdapter;
import com.motman_doctor.databinding.ActivityAddDrugsBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.AdddrugModel;
import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.activity_adddrug_mvp.ActivityAdddrugPresenter;
import com.motman_doctor.mvp.activity_adddrug_mvp.AdddrugActivityView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.share.Common;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class AddDrugActivity extends AppCompatActivity implements AdddrugActivityView {
    private ActivityAddDrugsBinding binding;
    private ActivityAdddrugPresenter presenter;
    private String lang;
    private AdddrugAdapter adapter;
    private List<AdddrugModel> adddrugModelList;
    private UserModel userModel;
    private Preferences preferences;
    private ApointmentModel.Data data;
    private ProgressDialog dialog2;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_drugs);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();

        data = (ApointmentModel.Data) intent.getSerializableExtra("DATA");


    }

    private void initView() {
        adddrugModelList = new ArrayList<>();
        adddrugModelList.add(new AdddrugModel());
        presenter = new ActivityAdddrugPresenter(this, this);
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        adapter = new AdddrugAdapter(adddrugModelList, this);

        binding.recView.setLayoutManager(new GridLayoutManager(this, 1));
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(view -> presenter.backPress());
        binding.llnewrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddrugModelList.add(new AdddrugModel());
                adapter.notifyDataSetChanged();
            }

        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkdata(adddrugModelList, userModel, data);
            }
        });


    }

    @Override
    public void onFinished() {
        finish();
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
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onsucess() {
        finish();
    }


    @Override
    public void onServer() {
        Toast.makeText(AddDrugActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();

    }

    public void delete(int position) {
        adddrugModelList.remove(position);
        adapter.notifyItemRemoved(position);
    }
}