package com.motman_doctor.ui.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motman_doctor.R;
import com.motman_doctor.adapters.AppointmentAdapter;
import com.motman_doctor.adapters.ChatAdapter;
import com.motman_doctor.databinding.FragmentChatBinding;
import com.motman_doctor.ui.activity_home.HomeActivity;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Fragment_Chat extends Fragment  {
    private FragmentChatBinding binding;
    private ChatAdapter adapter;
    private HomeActivity activity;
    public static Fragment_Chat newInstance(){
        return new Fragment_Chat();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        try {
            JitsiMeetUserInfo userInfo = new JitsiMeetUserInfo();

            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom("مطمئن")
                    .setUserInfo(userInfo)
                    .setAudioMuted(false)
                    .setVideoMuted(false)
                    .setAudioOnly(false)
                    .setWelcomePageEnabled(false)
                    .build();
            JitsiMeetActivity.launch(activity,options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    /*    binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(new ChatAdapter(new ArrayList<>(),activity));
        binding.progBar.setVisibility(View.GONE);*/
    }
}
