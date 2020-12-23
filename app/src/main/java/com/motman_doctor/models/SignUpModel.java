package com.motman_doctor.models;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.motman_doctor.BR;
import com.motman_doctor.R;

import java.io.Serializable;

public class SignUpModel extends BaseObservable implements Serializable {
    private String imageUrl;
    private String name;
    private String gender;
    private String address;
    private double lat;
    private double lng;
    private String phone_code;
    private String phone;
    private int specialization_id;
    private int city_id;
    private String price;
    private String licenseImage;
    private int degree_id;
    private String email;
    private String password;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();
    public ObservableField<String> error_price = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();


    public SignUpModel(String phone_code, String phone) {
        this.phone_code = phone_code;
        this.phone = phone;
        this.imageUrl="";
        this.name = "";
        this.gender="";
        this.address = "";
        this.lat=0.0;
        this.lng = 0.0;
        this.specialization_id = 1;
        this.city_id = 1;
        this.price ="";
        this.licenseImage ="";
        this.degree_id =1;
        this.email = "";
        this.password ="";

    }

    public boolean isStep1Valid(Context context)
    {
        if (!name.isEmpty()&&
                !gender.isEmpty()
                &&!address.isEmpty()){
            error_name.set(null);
            error_address.set(null);
            return true;
        }else {
            if (name.isEmpty()){
                error_name.set(context.getString(R.string.field_req));
            }else {
                error_name.set(null);
            }

            if (address.isEmpty()){
                error_address.set(context.getString(R.string.field_req));
            }else {
                error_address.set(null);
            }
            if (gender.isEmpty()){
                Toast.makeText(context, context.getString(R.string.ch_gender), Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
    public boolean isStep2Valid(Context context)
    {
        if (specialization_id!=0&&
                city_id!=0&&
                !price.isEmpty()&&
                !licenseImage.isEmpty()&&
                degree_id!=0
        )
        {
            error_price.set(null);
            return true;
        }else {
            if (specialization_id==0){
                Toast.makeText(context, R.string.ch_specialization, Toast.LENGTH_SHORT).show();
            }

            if (city_id==0){
                Toast.makeText(context, R.string.ch_city, Toast.LENGTH_SHORT).show();
            }

            if (price.isEmpty()){
                error_price.set(context.getString(R.string.field_req));
            }else {
                error_price.set(null);
            }

            if (degree_id==0){
                Toast.makeText(context, R.string.ch_degree, Toast.LENGTH_SHORT).show();
            }

            if (licenseImage.isEmpty()){
                Toast.makeText(context, R.string.ch_license_image, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
    public boolean isStep3Valid(Context context){
        if (!email.isEmpty()&&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()&&
                !password.isEmpty()&&password.length()>=6
        ){
            error_email.set(null);
            error_password.set(null);
            return true;
        }else {
            if (email.isEmpty()){
                error_email.set(context.getString(R.string.field_req));
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                error_email.set(context.getString(R.string.inv_email));

            }else {
                error_email.set(null);
            }

            if (password.isEmpty()){
                error_password.set(context.getString(R.string.field_req));
            }else if (password.length()<6){
                error_password.set(context.getString(R.string.password_short));
            }else {
                error_password.set(null);
            }
            return false;
        }
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

   @Bindable
   public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSpecialization_id() {
        return specialization_id;
    }

    public void setSpecialization_id(int specialization_id) {
        this.specialization_id = specialization_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    public String getLicenseImage() {
        return licenseImage;
    }

    public void setLicenseImage(String licenseImage) {
        this.licenseImage = licenseImage;
    }

    public int getDegree_id() {
        return degree_id;
    }

    public void setDegree_id(int degree_id) {
        this.degree_id = degree_id;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
