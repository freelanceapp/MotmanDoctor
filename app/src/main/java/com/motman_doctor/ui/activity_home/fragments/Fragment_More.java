package com.motman_doctor.ui.activity_home.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
import com.motman_doctor.tags.Tags;
import com.motman_doctor.ui.activity_contactus.ContactusActivity;
import com.motman_doctor.ui.activity_edit_profile.EditprofileActivity;
import com.motman_doctor.ui.activity_home.HomeActivity;
import com.motman_doctor.ui.activity_language.LanguageActivity;
import com.motman_doctor.ui.activity_login.LoginActivity;
import com.motman_doctor.ui.activity_my_appoiment.MyAppoinmentActivity;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class Fragment_More extends Fragment  implements MoreFragmentView {
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
//        binding.llchat.setOnClickListener(view -> {
//            if (userModel != null) {
//                Intent intent = new Intent(activity, ChatRoomActivity.class);
//                startActivity(intent);
//            } else {
//                Common.CreateDialogAlert(activity, activity.getResources().getString(R.string.please_sign_in_or_sign_up));
//            }
//        });
        binding.llappoimnet.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(activity, MyAppoinmentActivity.class);
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
        binding.lleditprofile.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(activity, EditprofileActivity.class);
                startActivity(intent);
            } else {
                Common.CreateDialogAlert(activity, activity.getResources().getString(R.string.please_sign_in_or_sign_up));
            }
        });
        presenter.getSetting();
        if (userModel != null) {
            binding.edtAmount.setText(userModel.getData().getDetection_price() + "");

        }
        binding.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = binding.edtAmount.getText().toString();
                if (price != null && !price.isEmpty()) {
                    presenter.editprofile(price, userModel);

                } else {
                    binding.edtAmount.setError(getResources().getString(R.string.field_required));
                }
            }
        });
        binding.switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel.getData().getIs_emergency().equals("yes")){
                    presenter.switchprofile("no",userModel);
                }
                else {
                    presenter.switchprofile("yes",userModel);

                }
            }
        });
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

    @Override
    public void onupdateValid(UserModel body) {
        preferences.create_update_userdata(activity, body);
        userModel = body;
        binding.setModel(body);
        Toast.makeText(activity,activity.getResources().getString(R.string.suc),Toast.LENGTH_LONG).show();
        if(body.getData().getDetection_price()==0){
            binding.imageEdit.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_correct));
        }
        else {
            binding.imageEdit.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_edit2));

        }
    }

    @Override
    public void onServer() {
        Toast.makeText(activity, activity.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (preferences != null) {
            userModel = preferences.getUserData(activity);
            udpate();
        }
    }

    private void udpate() {
        if (userModel != null && userModel.getData().getLogo() != null) {
            Picasso.get().load(Tags.IMAGE_URL + userModel.getData().getLogo()).resize(720, 480).onlyScaleDown().into(binding.image);
        }
        binding.tvName.setText(userModel.getData().getName());
        binding.setModel(userModel);

    }
}