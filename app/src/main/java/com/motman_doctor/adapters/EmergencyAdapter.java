package com.motman_doctor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motman_doctor.R;
import com.motman_doctor.databinding.AppointmentRowBinding;
import com.motman_doctor.databinding.EmergencyDoctorRowBinding;
import com.motman_doctor.databinding.LoadMoreRowBinding;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;

    public EmergencyAdapter(List<Object> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        EmergencyDoctorRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.emergency_doctor_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private EmergencyDoctorRowBinding binding;

        public MyHolder(EmergencyDoctorRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }




}
