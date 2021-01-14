package com.motman_doctor.ui.activity_mytime;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motman_doctor.R;
import com.motman_doctor.adapters.MyTimeAdapter;
import com.motman_doctor.databinding.ActivityMyTimesBinding;
import com.motman_doctor.databinding.DialogAddTimeBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.MyTimeModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.activity_mytime_mvp.ActivityMyTimePresenter;
import com.motman_doctor.mvp.activity_mytime_mvp.MyTimeActivityView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.share.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MyTimeActivity extends AppCompatActivity implements MyTimeActivityView {
    private ActivityMyTimesBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private List<MyTimeModel.Data> dataList;
    private MyTimeAdapter myTimeAdapter;

    private ActivityMyTimePresenter presenter;
    private ProgressDialog dialog;
    int doctortimeid;
    String day = "";
    private int pos;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_times);
        getDataFromIntent();
        initView();
        if (userModel != null) {
            presenter.gettime(userModel, doctortimeid);
        }

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            doctortimeid = intent.getIntExtra("id", 0);
            day = intent.getStringExtra("day");
        }
    }

    private void initView() {

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        presenter = new ActivityMyTimePresenter(this, this);
        binding.setLang(lang);
        binding.setDay(day);
        dataList = new ArrayList<>();
        myTimeAdapter = new MyTimeAdapter(dataList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setAdapter(myTimeAdapter);

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


    @Override
    public void onFinished() {
        finish();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public void ondata(MyTimeModel body) {
        dataList.clear();
        dataList.addAll(body.getData());
        myTimeAdapter.notifyDataSetChanged();

    }

    @Override
    public void onServer() {
        Toast.makeText(MyTimeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoad() {
        dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onFinishload() {
        dialog.dismiss();
    }

    @Override
    public void sucese() {
        presenter.gettime(userModel, doctortimeid);
    }

//    public void CreateDialogTime(Context context, int dayid) {
//        final AlertDialog dialog = new AlertDialog.Builder(context)
//                .create();
//
//        DialogAddTimeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_time, null, false);
//
//        binding.tvaddfrom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                typedate=0;
//                presenter.showDateDialog(getFragmentManager(), binding);
//            }
//        });
//        binding.tvaddto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                typedate=1;
//                presenter.showDateDialog(getFragmentManager(), binding);
//            }
//        });
//        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setView(binding.getRoot());
//        dialog.show();
//    }

    @Override
    public void onDateSelected(String date, DialogAddTimeBinding binding) {
//        if(typedate==0){
//        binding.tvaddfrom.setText(date);}
//        else {
//            binding.tvaddto.setText(date);
//        }
    }

    @Override
    public void delteucese() {
        dataList.remove(pos);
        myTimeAdapter.notifyItemRemoved(pos);
    }

    public void remove(int pos) {
        this.pos=pos;
        presenter.remove(dataList.get(pos).getId(),userModel);
    }
}
