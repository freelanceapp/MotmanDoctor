package com.motman_doctor.services;

import com.motman_doctor.models.AddTimeModel;
import com.motman_doctor.models.AllCityModel;
import com.motman_doctor.models.AllSpiclixationModel;
import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.DayModel;
import com.motman_doctor.models.DrugDataModel;
import com.motman_doctor.models.MessageDataModel;
import com.motman_doctor.models.MessageModel;
import com.motman_doctor.models.MyTimeModel;
import com.motman_doctor.models.NotificationDataModel;
import com.motman_doctor.models.PatentDataModel;
import com.motman_doctor.models.PlaceGeocodeData;
import com.motman_doctor.models.PlaceMapDetailsData;
import com.motman_doctor.models.SettingModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.models.UserRoomModelData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
                          @Field("phone") String phone,
                          @Field("password") String password


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
                           @Part("latitude") RequestBody lat_part,
                           @Part("longitude") RequestBody lng_part,
                           @Part("address") RequestBody address_part,

                           @Part("gender") RequestBody gender,
                           @Part("user_type") RequestBody user_type,
                           @Part("software_type") RequestBody software_type,
                           @Part("specialization_id") RequestBody spicial_part,
                           @Part("city_id") RequestBody city_part,
                           @Part("email") RequestBody email_part,
                           @Part("national_id") RequestBody national_id,
                           @Part("password") RequestBody password,
                           @Part("syndicate_id_number") RequestBody syndicate_id_number,
                           @Part MultipartBody.Part logo
            ,
                           @Part List<MultipartBody.Part> image


    );

    @Multipart
    @POST("api/doctor-register")
    Call<UserModel> signup(@Part("phone_code") RequestBody phone_code,
                           @Part("phone") RequestBody phone,
                           @Part("name") RequestBody name,
                           @Part("latitude") RequestBody lat_part,
                           @Part("longitude") RequestBody lng_part,
                           @Part("address") RequestBody address_part,

                           @Part("gender") RequestBody gender,
                           @Part("user_type") RequestBody user_type,
                           @Part("software_type") RequestBody software_type,
                           @Part("specialization_id") RequestBody spicial_part,
                           @Part("city_id") RequestBody city_part,
                           @Part("email") RequestBody email_part,
                           @Part("national_id") RequestBody national_id,
                           @Part("password") RequestBody password,

                           @Part("syndicate_id_number") RequestBody syndicate_id_number,
                           @Part List<MultipartBody.Part> liceimage

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

    @GET("api/settings")
    Call<SettingModel> getSetting();

    @GET("api/get-medical-consultings")
    Call<UserRoomModelData> getRooms(
            @Query("user_id") int user_id,
            @Query("user_type") String user_type,
            @Query("pagination_status") String pagination_status

    );

    @GET("api/search-patient-by-name")
    Call<PatentDataModel> getMyPatient(@Header("Authorization") String Authorization,
                                       @Query("pagination_status") String pagination_status,
                                       @Query("per_link_") int per_link_,
                                       @Query("page") int page,
                                       @Query("doctor_id") int doctor_id,
                                       @Query("name") String name

    );

    @GET("api/get-drugs")
    Call<DrugDataModel> getDrugs(@Header("Authorization") String Authorization,
                                 @Query("doctor_id") int doctor_id,
                                 @Query("user_id") int user_id

    );

    @GET("api/get-my-notifications")
    Call<NotificationDataModel> getNotification(@Header("Authorization") String Authorization,
                                                @Query("user_type") String user_type,
                                                @Query("user_id") int user_id

    );

    @GET("api/get-one-consulting")
    Call<MessageModel> getRoomMessages(
            @Query("medical_consulting_id") int medical_consulting_id,
            @Query("pagination_status") String pagination_status,
            @Query("per_link_") int per_link_,
            @Query("page") int page


    );


    @FormUrlEncoded
    @POST("api/add-msg")
    Call<MessageDataModel> sendmessagetext(
            @Field("from_user_id") String from_id,

            @Field("to_user_id") String to_id,
            @Field("type") String type,
            @Field("medical_consulting_id") String medical_consulting_id,
            @Field("message") String message


    );

    @Multipart
    @POST("api/add-msg")
    Call<MessageDataModel> sendmessagewithimage
            (
                    @Part("from_user_id") RequestBody from_user_id,
                    @Part("to_user_id") RequestBody to_user_id,
                    @Part("type") RequestBody type,
                    @Part("medical_consulting_id") RequestBody medical_consulting_id,
                    @Part MultipartBody.Part imagepart

            );

    @FormUrlEncoded
    @POST("api/update-doctor-register")
    Call<UserModel> editprofile(
            @Header("Authorization") String user_token,
            @Field("detection_price") String detection_price,
            @Field("id") String id


    );

    @GET("api/Get-Days-with-Times")
    Call<DayModel> getDays(
            @Header("Authorization") String user_token,
            @Query("doctor_id") int doctor_id,
            @Query("pagination_status") String pagination_status

    );

    @FormUrlEncoded
    @POST("api/add-doctor-days")
    Call<ResponseBody> addday(
            @Header("Authorization") String user_token,
            @Field("doctor_id") String doctor_id,
            @Field("day_name[]") List<String> day_name


    );

    @GET("api/Get-Times-By-Day")
    Call<MyTimeModel> gettimes(
            @Header("Authorization") String user_token,
            @Query("doctor_time_id") int doctor_time_id,
            @Query("pagination_status") String pagination_status

    );

    @POST("api/add-doctor-times")
    Call<ResponseBody> addtime(
            @Header("Authorization") String user_token,
            @Body AddTimeModel addTimeModel


    );
    @FormUrlEncoded
    @POST("api/delete_notification")
    Call<ResponseBody> delteNotification(@Field("notification_id") int notification_id);
    @FormUrlEncoded
    @POST("api/update-doctor-register")
    Call<UserModel> editprofile(
            @Header("Authorization") String user_token,
            @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("detection_time") String detection_time,
            @Field("title_job_degree") String title_job_degree



            );
    @FormUrlEncoded
    @POST("api/update-doctor-register")
    Call<UserModel> editprofile(
            @Header("Authorization") String user_token,
            @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("detection_time") String detection_time,
            @Field("title_job_degree") String title_job_degree,
            @Field("password") String password




    );
    @Multipart
    @POST("api/update-doctor-register")
    Call<UserModel> editprofile(
            @Header("Authorization") String user_token,
            @Part("id") RequestBody id,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("detection_time") RequestBody detection_time,
            @Part("title_job_degree") RequestBody title_job_degree,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part logo




    );
    @Multipart
    @POST("api/update-doctor-register")
    Call<UserModel> editprofile(
            @Header("Authorization") String user_token,
            @Part("id") RequestBody id,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("detection_time") RequestBody detection_time,
            @Part("title_job_degree") RequestBody title_job_degree,
            @Part MultipartBody.Part logo

    );
    @FormUrlEncoded
    @POST("api/delete-doctor-days")
    Call<ResponseBody> deleteday(
            @Header("Authorization") String user_token,
            @Field("doctor_time_id") int doctor_time_id
    );
    @FormUrlEncoded
    @POST("api/delete-doctor-times")
    Call<ResponseBody> deltetime(
            @Header("Authorization") String user_token,
            @Field("doctor_time_detail_id") int doctor_time_detail_id
    );
    @FormUrlEncoded
    @POST("api/update-doctor-register")
    Call<UserModel> switchprofile(
            @Header("Authorization") String user_token,
            @Field("is_emergency") String is_emergency,
            @Field("id") String id


    );
    @FormUrlEncoded
    @POST("api/open-call-by-doctor")
    Call<ResponseBody> opencall(
            @Header("Authorization") String Authorization,
            @Field("doctor_id") String doctor_id,
            @Field("patient_id") String patient_id,
            @Field("reservation_id") String reservation_id



    );
    @FormUrlEncoded
    @POST("api/close-call-by-doctor")
    Call<ResponseBody> closecall(
            @Header("Authorization") String Authorization,
            @Field("doctor_id") String doctor_id,
            @Field("patient_id") String patient_id,
            @Field("reservation_id") String reservation_id



    );
    @FormUrlEncoded
    @POST("api/add-drugs")
    Call<ResponseBody> Adddrug(
            @Header("Authorization") String Authorization,
            @Field("doctor_id") String doctor_id,
            @Field("user_id") String user_id,
            @Field("reservation_id") String reservation_id,
            @Field("drag_name[]") List<String> drag_name,
            @Field("take_num[]") List<String> take_num,
            @Field("details[]") List<String> details




    );
}