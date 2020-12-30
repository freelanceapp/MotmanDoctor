package com.motman_doctor.ui.activity_home.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.motman_doctor.R;
import com.motman_doctor.databinding.FragmentMoreBinding;
import com.motman_doctor.models.SettingModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.fragment_more_mvp.FragmentMorePresenter;
import com.motman_doctor.mvp.fragment_more_mvp.MoreFragmentView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.share.Common;
import com.motman_doctor.ui.activity_contactus.ContactusActivity;
import com.motman_doctor.ui.activity_home.HomeActivity;
import com.motman_doctor.ui.activity_language.LanguageActivity;
import com.motman_doctor.ui.activity_login.LoginActivity;
import com.motman_doctor.ui.activity_room.ChatRoomActivity;

import io.paperdb.Paper;

public class Fragment_More extends Fragment implements MoreFragmentView {
    private FragmentMoreBinding binding;
    private String lang;
    private HomeActivity activity;
    private FragmentMorePresenter presenter;
    private Preferences preferences;
    private UserModel userModel;
    private ProgressDialog dialog;
    private SettingModel setting;
    private String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";


    public static Fragment_More newInstance() {
        return new Fragment_More();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(userModel);
        presenter = new FragmentMorePresenter(this, activity);
        binding.llChangeLanguage.setOnClickListener(view -> {
            Intent intent = new Intent(activity, LanguageActivity.class);
            startActivityForResult(intent, 100);
        });
        binding.llcontactus.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ContactusActivity.class);
            startActivity(intent);
        });
        binding.logout.setOnClickListener(view -> {
            if (userModel != null) {
                presenter.logout(userModel);
            } else {
                Common.CreateDialogAlert(activity, activity.getResources().getString(R.string.please_sign_in_or_sign_up));
            }
        });
        binding.llchat.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(activity, ChatRoomActivity.class);
                startActivity(intent);
            } else {
                Common.CreateDialogAlert(activity, activity.getResources().getString(R.string.please_sign_in_or_sign_up));
            }
        });
        binding.facebook.setOnClickListener(v -> {
            if (setting != null && setting.getSettings() != null && setting.getSettings().getFacebook() != null) {
                if (setting.getSettings().getInstagram().matches(regex)) {
                    presenter.open(setting.getSettings().getFacebook());

                } else {
                    Toast.makeText(activity, R.string.link_inc, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, R.string.not_avail_now, Toast.LENGTH_SHORT).show();
            }
        });
        binding.instgram.setOnClickListener(v -> {
            if (setting != null && setting.getSettings() != null && setting.getSettings().getInstagram() != null) {
                if (setting.getSettings().getInstagram().matches(regex)) {
                    presenter.open(setting.getSettings().getInstagram());

                } else {
                    Toast.makeText(activity, R.string.link_inc, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, R.string.not_avail_now, Toast.LENGTH_SHORT).show();
            }
        });

        binding.twitter.setOnClickListener(v -> {
            if (setting != null && setting.getSettings() != null && setting.getSettings().getTwitter() != null) {
                if (setting.getSettings().getTwitter().matches(regex)) {
                    presenter.open(setting.getSettings().getTwitter());

                } else {
                    Toast.makeText(activity, R.string.link_inc, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, R.string.not_avail_now, Toast.LENGTH_SHORT).show();
            }
        });
        presenter.getSetting();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            String lang = data.getStringExtra("lang");
            activity.refreshActivity(lang);

        }
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoad() {
        if (dialog == null) {
            dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
        } else {
            dialog.dismiss();
        }
        dialog.show();
    }

    @Override
    public void onFinishload() {
        dialog.dismiss();
    }


    @Override
    public void logout() {
        preferences.clear(activity);
        navigateToSignInActivity();
    }

    @Override
    public void onsetting(SettingModel body) {
        this.setting = body;
    }

    private void navigateToSignInActivity() {

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.finish();
        startActivity(intent);
    }

    @Override
    public void ViewSocial(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        startActivity(intent);

    }
}