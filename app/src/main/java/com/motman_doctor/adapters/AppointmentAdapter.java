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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.motman_doctor.R;
import com.motman_doctor.databinding.AppointmentRowBinding;
import com.motman_doctor.databinding.LoadMoreRowBinding;
import com.motman_doctor.models.ApointmentModel;
import com.motman_doctor.ui.activity_home.fragments.Fragment_Home;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int LOAD = 2;
    private List<ApointmentModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    private Fragment fragment;

    public AppointmentAdapter(List<ApointmentModel.Data> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
        this.fragment = fragment;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            AppointmentRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.appointment_row, parent, false);
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
            myHolder.binding.setModel(list.get(position));
           // Log.e("flkfkfk", list.get(position).getPatient_fk().getAppointment_time());
            myHolder.binding.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fragment instanceof  Fragment_Home){
                        Fragment_Home fragment_home=(Fragment_Home)fragment;
                        fragment_home.open(list.get(position));
                    }
                }
            });
//            myHolder.binding.btnDetails.setOnClickListener(v -> {
//
//            });
            myHolder.binding.tvdetials.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof Fragment_Home){
                        Fragment_Home fragment_home=(Fragment_Home)fragment;
                        fragment_home.setitem(list.get(position).patient_fk,list.get(position).getId(),list.get(position).getReservation_type());
                    }
                }
            });
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
        private AppointmentRowBinding binding;

        public MyHolder(AppointmentRowBinding binding) {
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
