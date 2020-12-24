package com.motman_doctor.services;

import com.motman_doctor.models.AllCityModel;
import com.motman_doctor.models.AllSpiclixationModel;
import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.PlaceGeocodeData;
import com.motman_doctor.models.PlaceMapDetailsData;
import com.motman_doctor.models.SettingModel;
import com.motman_doctor.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);


    @GET("api/get-my-reservation")
    Call<ApointmentModel> getMyApointment(
            @Header("Authorization") String Authorization,
            @Query("pagination_status") String pagination_status,
            @Query("per_link_") int per_link_,
            @Query("page") int page,
            @Query("doctor_id") int doctor_id,
            @Query("reservation_type") String reservation_type

    );

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone

    );
    @GET("api/get-specializations")
    Call<AllSpiclixationModel> getspicailest();

    @GET("api/get-cities")
    Call<AllCityModel> getcities();
    @Multipart
    @POST("api/doctor-register")
    Call<UserModel> signup(@Part("phone_code") RequestBody phone_code,
                           @Part("phone") RequestBody phone,
                           @Part("name") RequestBody name,
                           @Part("latitude")RequestBody lat_part,
                           @Part("longitude")RequestBody lng_part,
                           @Part("address")RequestBody address_part,

                           @Part("gender")RequestBody gender,
                           @Part("user_type")RequestBody user_type,
                           @Part("software_type") RequestBody software_type,
                           @Part("specialization_id") RequestBody spicial_part,
                           @Part("city_id") RequestBody city_part,
                           @Part("email") RequestBody email_part,
                           @Part MultipartBody.Part logo
            ,
                           @Part MultipartBody.Part image





    );
    @Multipart
    @POST("api/doctor-register")
    Call<UserModel> signup(@Part("phone_code") RequestBody phone_code,
                           @Part("phone") RequestBody phone,
                           @Part("name") RequestBody name,
                           @Part("latitude")RequestBody lat_part,
                           @Part("longitude")RequestBody lng_part,
                           @Part("address")RequestBody address_part,

                           @Part("gender")RequestBody gender,
                           @Part("user_type")RequestBody user_type,
                           @Part("software_type") RequestBody software_type,
                           @Part("specialization_id") RequestBody spicial_part,
                           @Part("city_id") RequestBody city_part,
                           @Part("email") RequestBody email_part,
                           @Part MultipartBody.Part liceimage

    );
    @FormUrlEncoded
    @POST("api/contact-us")
    Call<ResponseBody> contactUs(@Field("name") String name,
                                 @Field("email") String email,
                                 @Field("subject") String subject,
                                 @Field("message") String message


    );
    @POST("api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String user_token
    );
    @GET("api/sttings")
    Call<SettingModel> getSetting();
}