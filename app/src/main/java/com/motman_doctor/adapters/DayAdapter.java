package com.motman_doctor.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motman_doctor.R;
import com.motman_doctor.databinding.AppointmentRowBinding;
import com.motman_doctor.databinding.LoadMoreRowBinding;
import com.motman_doctor.databinding.MyAppointmentRowBinding;
import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.models.DayModel;
import com.motman_doctor.ui.activity_my_appoiment.MyAppoinmentActivity;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int LOAD = 2;
    private List<DayModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;

    public DayAdapter(List<DayModel.Data> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            MyAppointmentRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.my_appointment_row, parent, false);
            return new MyHolder(binding);
        } else {
            LoadMoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            String day = "";
            if (list.get(position).getDay_name().equals("SAT")) {
                day = context.getString(R.string.SAT);
            } else if (list.get(position).getDay_name().equals("SUN")) {
                day = context.getString(R.string.Sun);
            } else if (list.get(position).getDay_name().equals("MON")) {
                day = context.getString(R.string.mon);
            } else if (list.get(position).getDay_name().equals("TUE")) {
                day = context.getString(R.string.Tue);
            } else if (list.get(position).getDay_name().equals("WED")) {
                day = context.getString(R.string.wed);
            } else if (list.get(position).getDay_name().equals("THU")) {
                day = context.getString(R.string.Thr);
            } else if (list.get(position).getDay_name().equals("FRI")) {
                day = context.getString(R.string.fri);
            }
            myHolder.binding.setDay(day);
            myHolder.binding.fladd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context instanceof MyAppoinmentActivity){
                        MyAppoinmentActivity myAppoinmentActivity=(MyAppoinmentActivity)context;
                        myAppoinmentActivity.CreateDialogTime(context,list.get(position).getId());
                    }
                }
            });
            // Log.e("flkfkfk",list.get(position).getReservation_type());
//            myHolder.binding.btnDetails.setOnClickListener(v -> {
//
//            });
        } else if (holder instanceof LoadMoreHolder) {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.prgBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.prgBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private MyAppointmentRowBinding binding;

        public MyHolder(MyAppointmentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public static class LoadMoreHolder extends RecyclerView.ViewHolder {
        private LoadMoreRowBinding binding;

        public LoadMoreHolder(LoadMoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() > 0) {
            if (list.get(position) == null) {
                return LOAD;
            } else {
                return DATA;
            }
        } else {
            return DATA;

        }

    }
}
