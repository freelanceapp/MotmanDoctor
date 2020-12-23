package com.motman_doctor.mvp.activity_confirm_code_mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.motman_doctor.R;
import com.motman_doctor.share.Common;
import com.motman_doctor.ui.activity_confirm_code.ConfirmCodeActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class ActivityConfirmCodePresenter {
    private Context context;
    private ActivityConfirmCodeView view;
    private String phone_code="",phone="";
    private CountDownTimer countDownTimer;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode = "";
    private ConfirmCodeActivity activity;
    private String lang;

    public ActivityConfirmCodePresenter(Context context, ActivityConfirmCodeView view, String phone,String phone_code)
    {
        this.context = context;
        this.view = view;
        this.phone = phone;
        this.phone_code = phone_code;
        activity = (ConfirmCodeActivity) context;
        mAuth = FirebaseAuth.getInstance();
        Paper.init(context);
        lang = Paper.book().read("lang", "ar");
        sendSmsCode();
    }

    public void sendSmsCode()
    {
        startCounter();
        mAuth.setLanguageCode(lang);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                smsCode = phoneAuthCredential.getSmsCode();
                checkValidCode(smsCode);
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                ActivityConfirmCodePresenter.this.verificationId = verification_id;
                Log.e("verification_id", verification_id);
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                if (e.getMessage() != null) {
                    view.onCodeFailed(e.getMessage());
                } else {
                    view.onCodeFailed(context.getString(R.string.failed));

                }
            }
        };
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                        phone_code+phone,
                        120,
                        TimeUnit.SECONDS,
                        activity,
                        mCallBack

                );
    }
    public void checkValidCode(String code)
    {
        ProgressDialog dialog = Common.createProgressDialog(context,context.getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {

                        dialog.dismiss();
                        view.onSuccess();
                    }).addOnFailureListener(e -> {
                        dialog.dismiss();
                if (e.getMessage() != null) {
                    try {
                        view.onCodeFailed(e.getMessage());

                    }catch (Exception ex)
                    {

                    }
                } else {
                    view.onCodeFailed(context.getString(R.string.failed));

                }
            });
        }

    }



    private void startCounter()
    {
        countDownTimer = new CountDownTimer(120000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) ((millisUntilFinished / 1000) / 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);

                String time = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
                view.onCounterStarted(time);



            }

            @Override
            public void onFinish() {
                view.onCounterFinished();


            }
        };

        countDownTimer.start();
    }
    public void resendCode()
    {
        if (countDownTimer != null) {
            countDownTimer.start();
        }
        sendSmsCode();
    }
    public void stopTimer()
    {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


}
