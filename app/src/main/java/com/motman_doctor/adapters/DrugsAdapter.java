package com.motman_doctor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motman_doctor.R;
import com.motman_doctor.databinding.DrugRowBinding;
import com.motman_doctor.databinding.EmergencyDoctorRowBinding;
import com.motman_doctor.models.DrugModel;

import java.util.List;

public class DrugsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DrugModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;

    public DrugsAdapter(List<DrugModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DrugRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.drug_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private DrugRowBinding binding;

        public MyHolder(DrugRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }




}
