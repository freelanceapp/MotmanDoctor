package com.motman_doctor.ui.activity_my_appoiment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motman_doctor.R;
import com.motman_doctor.adapters.DayAdapter;
import com.motman_doctor.adapters.SpinnerAdapter;
import com.motman_doctor.databinding.ActivityMyAppoimentBinding;
import com.motman_doctor.databinding.DialogAddTimeBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.AddTimeModel;
import com.motman_doctor.models.DayModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.activity_my_appoinment_mvp.ActivityMyAppoimentPresenter;
import com.motman_doctor.mvp.activity_my_appoinment_mvp.MyAppoimentActivityView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.share.Common;
import com.motman_doctor.ui.activity_mytime.MyTimeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MyAppoinmentActivity extends AppCompatActivity implements MyAppoimentActivityView {
    private ActivityMyAppoimentBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private List<DayModel.Data> daylist;
    private DayAdapter dayAdapter;
    private LinearLayoutManager manager;
    private SpinnerAdapter adddayAdapter;
    private List<String> list;
    private List<String> list2;
    private List<String> list3;

    private int typedate = -1;
    private ActivityMyAppoimentPresenter presenter;
    private ProgressDialog dialog;
    private String from = "", to = "";
    private int pos;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_appoiment);
        initView();
        if (userModel != null) {
            presenter.getDays(userModel);
        }

    }

    private void initView() {
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        presenter = new ActivityMyAppoimentPresenter(this, this);
        binding.setLang(lang);
        daylist = new ArrayList<>();
        dayAdapter = new DayAdapter(daylist, this);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setAdapter(dayAdapter);
        adddayAdapter = new SpinnerAdapter(list, this);
        Addday();
        binding.spinnerday.setAdapter(adddayAdapter);
        binding.spinnerday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    presenter.addday(list2.get(position), userModel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.backPress();
            }
        });
//        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    int total_items = room_adapter.getItemCount();
//                    int lastItemPos = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//
//                    if (total_items >= 6 && (lastItemPos == total_items - 2) && !isLoading) {
//                        isLoading = true;
//                        userRoomModelList.add(null);
//                        room_adapter.notifyItemInserted(userRoomModelList.size() - 1);
//                        //  loadMore(page);
//                    }
//
//                }
//            }
//        });


    }

    private void Addday() {
        list.clear();
        list2.clear();
        list3.clear();
        list.add(getResources().getString(R.string.add_day));
        list.add(getResources().getString(R.string.SAT));
        list.add(getResources().getString(R.string.Sun));
        list.add(getResources().getString(R.string.mon));
        list.add(getResources().getString(R.string.Tue));
        list.add(getResources().getString(R.string.wed));
        list.add(getResources().getString(R.string.Thr));
        list.add(getResources().getString(R.string.fri));
        list2.add(getResources().getString(R.string.add_day));
        list2.add("SAT");
        list2.add("SUN");
        list2.add("MON");
        list2.add("TUE");
        list2.add("WED");
        list2.add("THU");
        list2.add("FRI");
//        list3.add(getResources().getString(R.string.add_day));
//        list3.add(getResources().getString(R.string.SAT));
//        list3.add(getResources().getString(R.string.Sun));
//        list3.add(getResources().getString(R.string.mon));
//        list3.add(getResources().getString(R.string.Tue));
//        list3.add(getResources().getString(R.string.wed));
//        list3.add(getResources().getString(R.string.Thr));
//        list3.add(getResources().getString(R.string.fri));

    }


    @Override
    public void onFinished() {
        finish();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public void ondata(DayModel body) {
        daylist.clear();
        daylist.addAll(body.getData());
        dayAdapter.notifyDataSetChanged();
        Addday();
        change();
    }

    private void change() {
        for (int i = 0; i < daylist.size(); i++) {

            if (list2.contains(daylist.get(i).getDay_name())) {
                list3.add(list.get(list2.indexOf(daylist.get(i).getDay_name())));
            }
        }
        if (list.size() - daylist.size() == 1) {
            binding.card.setVisibility(View.GONE);
        } else {
            binding.card.setVisibility(View.VISIBLE);

            for (int i = 0; i < daylist.size(); i++) {

                if (list2.contains(daylist.get(i).getDay_name())) {
                    list.remove(list2.indexOf(daylist.get(i).getDay_name()));
                    list2.remove(list2.indexOf(daylist.get(i).getDay_name()));

                }


            }

            adddayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onServer() {
        Toast.makeText(MyAppoinmentActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoad() {
        if (dialog == null) {
            dialog = Common.createProgressDialog(this, getString(R.string.wait));
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
    public void sucese() {
        presenter.getDays(userModel);
    }

    public void CreateDialogTime(Context context, int dayid) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogAddTimeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_time, null, false);

        binding.tvaddfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typedate = 0;
                presenter.showDateDialog(getFragmentManager(), binding);
            }
        });
        binding.tvaddto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typedate = 1;
                presenter.showDateDialog(getFragmentManager(), binding);
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!from.isEmpty() && !to.isEmpty()) {
                    AddTimeModel addTimeModel = new AddTimeModel();
                    addTimeModel.setDoctor_time_id(dayid);
                    List<AddTimeModel.Times> list = new ArrayList<>();
                    AddTimeModel.Times add = new AddTimeModel.Times();
                    add.setFrom_hour(from);
                    add.setTo_hour(to);
                    add.setType("normal");
                    list.add(add);
                    addTimeModel.setTimes(list);
                    presenter.addtime(addTimeModel, userModel);
                    from = "";
                    to = "";
                    dialog.dismiss();
                } else {
                    if (from.isEmpty() || to.isEmpty()) {
                        if (from.isEmpty()) {
                            Toast.makeText(MyAppoinmentActivity.this, getResources().getString(R.string.add_from), Toast.LENGTH_LONG).show();
                        }
                        if (to.isEmpty()) {
                            Toast.makeText(MyAppoinmentActivity.this, getResources().getString(R.string.add_to), Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(true);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    @Override
    public void onDateSelected(String date, DialogAddTimeBinding binding) {
        if (typedate == 0) {
            binding.tvaddfrom.setText(date);
            from = date;
        } else {
            binding.tvaddto.setText(date);
            to = date;
        }
    }

    @Override
    public void suceseaddtime() {
        Toast.makeText(this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSuccessDelete() {
        daylist.remove(pos);
        dayAdapter.notifyItemRemoved(pos);
        Addday();
        change();
    }

    public void show(Context context, int id, int pos) {
        Intent intent = new Intent(MyAppoinmentActivity.this, MyTimeActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("day", list3.get(pos));
        startActivity(intent);
    }

    public void delete(int position) {
        pos = position;
        presenter.deletday(userModel, daylist.get(position).getId());
    }
}
