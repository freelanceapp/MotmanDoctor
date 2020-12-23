package com.motman_doctor.ui.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.motman_doctor.R;
import com.motman_doctor.databinding.FragmentMoreBinding;
import com.motman_doctor.ui.activity_home.HomeActivity;
import com.motman_doctor.ui.activity_language.LanguageActivity;

import io.paperdb.Paper;

public class Fragment_More extends Fragment {
    private FragmentMoreBinding binding;
    private String lang;
    private HomeActivity activity;

    public static Fragment_More newInstance(){
        return new Fragment_More();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);

        binding.llChangeLanguage.setOnClickListener(view -> {
            Intent intent = new Intent(activity, LanguageActivity.class);
            startActivityForResult(intent,100);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100&&resultCode== Activity.RESULT_OK&&data!=null){
            String lang = data.getStringExtra("lang");
            activity.refreshActivity(lang);
        }
    }
}
