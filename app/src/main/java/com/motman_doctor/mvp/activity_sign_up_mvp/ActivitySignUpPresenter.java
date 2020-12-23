package com.motman_doctor.mvp.activity_sign_up_mvp;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;

import com.motman_doctor.R;
import com.motman_doctor.models.SignUpModel;
import com.motman_doctor.ui.activity_home.HomeActivity;
import com.motman_doctor.ui.activity_sign_up.fragments.FragmentSignUp1;
import com.motman_doctor.ui.activity_sign_up.fragments.FragmentSignUp2;
import com.motman_doctor.ui.activity_sign_up.fragments.FragmentSignUp3;

public class ActivitySignUpPresenter {
    private Context context;
    private ActivitySignUpView view;
    private FragmentManager fragmentManager;
    private FragmentSignUp1 fragmentSignUp1;
    private FragmentSignUp2 fragmentSignUp2;
    private FragmentSignUp3 fragmentSignUp3;
    private SignUpModel signUpModel;





    public ActivitySignUpPresenter(Context context, ActivitySignUpView view,FragmentManager fragmentManager,SignUpModel signUpModel)
    {
        this.context = context;
        this.view = view;
        this.fragmentManager = fragmentManager;
        this.signUpModel = signUpModel;
        displayFragment1();

    }

    public void manageData(boolean isBack)
    {
        if (fragmentSignUp1!=null&&fragmentSignUp1.isAdded()&&fragmentSignUp1.isVisible()){
            this.signUpModel = fragmentSignUp1.signUpModel;
            if (isBack){
                view.onBack();
            }else {
                if (signUpModel.isStep1Valid(context)){
                    displayFragment2();
                }
            }
        }else if (fragmentSignUp2!=null&&fragmentSignUp2.isAdded()&&fragmentSignUp2.isVisible()){
            this.signUpModel = fragmentSignUp2.signUpModel;

            if (isBack){
                displayFragment1();
            }else {

                if (this.signUpModel.isStep2Valid(context)){
                    displayFragment3();
                }
            }
        }else if (fragmentSignUp3!=null&&fragmentSignUp3.isAdded()&&fragmentSignUp3.isVisible()){
            this.signUpModel = fragmentSignUp3.signUpModel;

            if (isBack){
                displayFragment2();
            }else {
                if (this.signUpModel.isStep3Valid(context)){
                    signUp();
                }
            }
        }
    }

    private void displayFragment1()
    {
        if (fragmentSignUp1==null){
            fragmentSignUp1 = FragmentSignUp1.newInstance(signUpModel);
        }

        if (fragmentSignUp2!=null&&fragmentSignUp2.isAdded()){
            fragmentManager.beginTransaction().hide(fragmentSignUp2).commit();
        }

        if (fragmentSignUp3!=null&&fragmentSignUp3.isAdded()){
            fragmentManager.beginTransaction().hide(fragmentSignUp3).commit();
        }

        if (fragmentSignUp1.isAdded()){
            fragmentManager.beginTransaction().show(fragmentSignUp1).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragmentSignUp1,"fragmentSignUp1").commit();
        }
        view.onFragmentSignUp1Displayed();
    }

    private void displayFragment2()
    {
        if (fragmentSignUp2==null){
            fragmentSignUp2 = FragmentSignUp2.newInstance(signUpModel);
        }

        if (fragmentSignUp1!=null&&fragmentSignUp1.isAdded()){
            fragmentManager.beginTransaction().hide(fragmentSignUp1).commit();
        }

        if (fragmentSignUp3!=null&&fragmentSignUp3.isAdded()){
            fragmentManager.beginTransaction().hide(fragmentSignUp3).commit();
        }

        if (fragmentSignUp2.isAdded()){
            fragmentManager.beginTransaction().show(fragmentSignUp2).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragmentSignUp2,"fragmentSignUp2").commit();
        }
        view.onFragmentSignUp2Displayed();
    }

    private void displayFragment3()
    {
        if (fragmentSignUp3==null){
            fragmentSignUp3 = FragmentSignUp3.newInstance(signUpModel);
        }

        if (fragmentSignUp1!=null&&fragmentSignUp1.isAdded()){
            fragmentManager.beginTransaction().hide(fragmentSignUp1).commit();
        }

        if (fragmentSignUp2!=null&&fragmentSignUp2.isAdded()){
            fragmentManager.beginTransaction().hide(fragmentSignUp2).commit();
        }

        if (fragmentSignUp3.isAdded()){
            fragmentManager.beginTransaction().show(fragmentSignUp3).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragmentSignUp3,"fragmentSignUp3").commit();
        }
        view.onFragmentSignUp3Displayed();
    }


    private void signUp() {
        Intent intent=new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
}
