package com.motman_doctor.mvp.activity_my_appoinment_mvp;

import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.motman_doctor.R;
import com.motman_doctor.databinding.DialogAddTimeBinding;
import com.motman_doctor.models.AddTimeModel;
import com.motman_doctor.models.DayModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.remote.Api;
import com.motman_doctor.tags.Tags;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMyAppoimentPresenter implements TimePickerDialog.OnTimeSetListener {
    private UserModel userModel;
    private Preferences preferences;
    private MyAppoimentActivityView view;
    private Context context;
    private TimePickerDialog timePickerDialog;
    private DialogAddTimeBinding binding;

    public ActivityMyAppoimentPresenter(MyAppoimentActivityView view, Context context) {
        this.view = view;
        this.context = context;
        createDateDialog();

    }

    private void createDateDialog() {

        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), false);
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(ActivityCompat.getColor(context, R.color.colorPrimary));
        timePickerDialog.setCancelColor(ActivityCompat.getColor(context, R.color.gray4));
        timePickerDialog.setOkColor(ActivityCompat.getColor(context, R.color.colorPrimary));
        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        //  timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

    }

    public void showDateDialog(FragmentManager fragmentManager, DialogAddTimeBinding dialogAddTimeBinding) {
        try {
            this.binding=dialogAddTimeBinding;
            timePickerDialog.show(fragmentManager, "");

        } catch (Exception e) {
        }
    }

    public void backPress() {

        view.onFinished();


    }


    public void getDays(UserModel userModel) {

        try {
            Api.getService(Tags.base_url)
                    .getDays("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), "off")
                    .enqueue(new Callback<DayModel>() {
                        @Override
                        public void onResponse(Call<DayModel> call, Response<DayModel> response) {
                            // Log.e("kdkdkdk", response.body().getRooms().size() + "");
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                view.ondata(response.body());
                            } else {
                                if (response.code() == 500) {
                                    //   Toast.makeText(com.ghiar.activities_fragments.activity_room.ChatRoomActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    view.onFailed(context.getResources().getString(R.string.server_error));

                                } else {
                                    view.onFailed(context.getResources().getString(R.string.failed));

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DayModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        view.onFailed(context.getResources().getString(R.string.something));
                                    } else {
                                        view.onFailed(t.getMessage());
                                    }
                                }

                            } catch (Exception e) {

                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("eeee", e.getMessage() + "__");
        }
    }

    public void addday(String s, UserModel userModel) {
        view.onLoad();

        List<String> list = new ArrayList<>();
        list.add(s);
        Api.getService(Tags.base_url)
                .addday("Bearer " + userModel.getData().getToken(), userModel.getData().getId() + "", list)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.sucese();
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                if (response.code() == 409) {
                                    view.onFailed(context.getString(R.string.phone_found));
                                } else {
                                    view.onFailed(response.message() + "");
                                }                                 //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            view.onFinishload();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onFailed(context.getResources().getString(R.string.something));
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed(t.getMessage());
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
    public void addtime(AddTimeModel addTimeModel, UserModel userModel) {

        view.onLoad();
        Api.getService(Tags.base_url)
                .addtime("Bearer " + userModel.getData().getToken(), addTimeModel)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.suceseaddtime();
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                if (response.code() == 409) {
                                    view.onFailed(context.getString(R.string.phone_found));
                                } else {
                                    view.onFailed(response.message() + "");
                                }                                 //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            view.onFinishload();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onFailed(context.getResources().getString(R.string.something));
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed(t.getMessage());
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


//    private void loadMore(int page)
//    {
//        try {
//
//            Api.getService(Tags.base_url)
//                    .getRooms(userModel.getId(),page)
//                    .enqueue(new Callback<DayModel>() {
//                        @Override
//                        public void onResponse(Call<DayModel> call, Response<DayModel> response) {
//                            userRoomModelList.remove(userRoomModelList.size()-1);
//                            room_adapter.notifyItemRemoved(userRoomModelList.size()-1);
//                            isLoading = false;
//
//                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
//                            {
//                                userRoomModelList.addAll(response.body().getData());
//                                room_adapter.notifyDataSetChanged();
//                                current_page = response.body().getCurrent_page();
//                            }else
//                            {
//                                if (response.code() == 500) {
//                                    Toast.makeText(ChatRoomActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//
//
//                                }else
//                                {
//                                    Toast.makeText(ChatRoomActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//                                    try {
//
//                                        Log.e("error",response.code()+"_"+response.errorBody().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<DayModel> call, Throwable t) {
//                            try {
//                                if (userRoomModelList.get(userRoomModelList.size()-1)==null)
//                                {
//                                    userRoomModelList.remove(userRoomModelList.size()-1);
//                                    room_adapter.notifyItemRemoved(userRoomModelList.size()-1);
//                                    isLoading = false;
//                                }
//
//
//                                if (t.getMessage()!=null)
//                                {
//                                    Log.e("error",t.getMessage());
//                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
//                                    {
//                                        Toast.makeText(ChatRoomActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
//                                    }else
//                                    {
//                                        Toast.makeText(ChatRoomActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            }catch (Exception e){}
//                        }
//                    });
//        }catch (Exception e){
//            binding.progBar.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        String date = dateFormat.format(new Date(calendar.getTimeInMillis()));
        ActivityMyAppoimentPresenter.this.view.onDateSelected(date,binding);

    }

    public void deletday(UserModel userModel, int id) {
        view.onLoad();
        Api.getService(Tags.base_url)
                .deleteday("Bearer " + userModel.getData().getToken(),id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            // view.onUserFound(response.body());
                            view.onSuccessDelete();
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                // view.onServer();
                            } else {
                                view.onFailed(response.message());
                                //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            view.onFinishload();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //   view.onnotconnect(t.getMessage().toLowerCase());
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed(t.getMessage());
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
}
