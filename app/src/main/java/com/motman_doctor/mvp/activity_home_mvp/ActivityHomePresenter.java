package com.motman_doctor.mvp.activity_home_mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MenuItem;

import androidx.fragment.app.FragmentManager;

import com.motman_doctor.R;
import com.motman_doctor.ui.activity_home.fragments.Fragment_Home;
import com.motman_doctor.ui.activity_home.fragments.Fragment_Chat;
import com.motman_doctor.ui.activity_home.fragments.Fragment_Patient;
import com.motman_doctor.ui.activity_home.fragments.Fragment_More;

public class ActivityHomePresenter {
    private Context context;
    private FragmentManager fragmentManager;
    private HomeActivityView view;
    private Fragment_Patient fragment_patient;
    public Fragment_Home fragment_home;
    private Fragment_Chat fragment_chat;
    private Fragment_More fragment_more;
    private double lat=0.0,lng=0.0;

    public ActivityHomePresenter(Context context,HomeActivityView view, FragmentManager fragmentManager,double lat,double lng) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.view = view;
        this.lat = lat;
        this.lng = lng;
        displayFragmentHome();
    }

    @SuppressLint("NonConstantResourceId")
    public void manageFragments(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.chat :
                displayFragmentChat();
                break;
            case R.id.patient :
                displayFragmentPatient();
                break;
            case R.id.more :
                displayFragmentMore();
                break;
            default:
                displayFragmentHome();
                break;
        }
    }
    private void displayFragmentPatient(){
        if (fragment_patient ==null){
            fragment_patient = Fragment_Patient.newInstance(lat,lng);
        }

        if (fragment_home !=null&& fragment_home.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_patient !=null&& fragment_patient.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_chat !=null&& fragment_chat.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_chat).commit();
        }
        if (fragment_more!=null&&fragment_more.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_patient.isAdded()){
            fragmentManager.beginTransaction().show(fragment_patient).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_patient,"fragment_live").commit();
        }
    }

    private void displayFragmentHome(){
        if (fragment_home ==null){
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_patient !=null&& fragment_patient.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_patient !=null&& fragment_patient.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_chat !=null&& fragment_chat.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_chat).commit();
        }
        if (fragment_more!=null&&fragment_more.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_home.isAdded()){
            fragmentManager.beginTransaction().show(fragment_home).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_home,"fragment_appointment").commit();
        }
    }

    private void displayFragmentChat(){
        if (fragment_chat ==null){
            fragment_chat = Fragment_Chat.newInstance();
        }


        if (fragment_patient !=null&& fragment_patient.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_patient !=null&& fragment_patient.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_home !=null&& fragment_home.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }
        if (fragment_more!=null&&fragment_more.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }


        if (fragment_chat.isAdded()){
            fragmentManager.beginTransaction().show(fragment_chat).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_chat,"fragment_chat").commit();
        }
    }



    private void displayFragmentMore(){
        if (fragment_more==null){
            fragment_more = Fragment_More.newInstance();
        }

        if (fragment_patient !=null&& fragment_patient.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }

        if (fragment_chat !=null&& fragment_chat.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_chat).commit();
        }

        if (fragment_home !=null&& fragment_home.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }
        if (fragment_patient !=null&& fragment_patient.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_patient).commit();
        }
        if (fragment_more.isAdded()){
            fragmentManager.beginTransaction().show(fragment_more).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment_more,"fragment_more").commit();
        }
    }

    public void backPress(){
        if (fragment_home !=null&& fragment_home.isAdded()&& fragment_home.isVisible()){
            view.onFinished();
        }else {
            displayFragmentHome();
            view.onHomeFragmentSelected();
        }
    }
}
