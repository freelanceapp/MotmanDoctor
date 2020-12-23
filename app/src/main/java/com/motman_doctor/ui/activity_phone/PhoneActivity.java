package com.motman_doctor.ui.activity_phone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.motman_doctor.R;
import com.motman_doctor.databinding.ActivityLoginBinding;
import com.motman_doctor.databinding.ActivityPhoneBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.share.Common;
import com.motman_doctor.ui.activity_confirm_code.ConfirmCodeActivity;
import com.motman_doctor.ui.activity_login.LoginActivity;
import com.motman_doctor.ui.activity_sign_up.SignUpActivity;

import io.paperdb.Paper;

public class PhoneActivity extends AppCompatActivity{
    private ActivityPhoneBinding binding;
    private String phone_code="+20";
    private String phone ="";
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_phone);
        initView();
    }



    private void initView() {
        binding.tv1.setText(Html.fromHtml(getString(R.string.verification_phone)));
        binding.btnConfirm.setOnClickListener(view -> {
            phone = binding.edtPhone.getText().toString();
           if (!phone.isEmpty()){
               binding.edtPhone.setError(null);
               Common.CloseKeyBoard(this,binding.edtPhone);
               navigateVerificationCodeActivity(String.valueOf(Integer.parseInt(phone)));
           }else {
               binding.edtPhone.setError(getString(R.string.field_req));
           }
        });
    }

    private void navigateVerificationCodeActivity(String phone) {

        Intent intent = new Intent(this, ConfirmCodeActivity.class);
        intent.putExtra("phone_code",phone_code);
        intent.putExtra("phone",phone);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            navigateToSignInActivity();
        }
    }

    private void navigateToSignInActivity() {
        Intent intent  = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone_code",phone_code);
        intent.putExtra("phone",phone);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent  = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}