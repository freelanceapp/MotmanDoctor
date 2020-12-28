package com.motman_doctor.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motman_doctor.R;
import com.motman_doctor.databinding.AppointmentRowBinding;
import com.motman_doctor.databinding.LoadMoreRowBinding;
import com.motman_doctor.databinding.PatientRowBinding;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.ui.activity_home.fragments.Fragment_Patient;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int LOAD = 2;
    private List<UserModel.User> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment_Patient fragment_patient;
    public PatientAdapter(List<UserModel.User> list, Context context,Fragment_Patient fragment_patient) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment_patient = fragment_patient;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==DATA){
            PatientRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.patient_row, parent, false);
            return new MyHolder(binding);
        }else {
            LoadMoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder){
            MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setModel(list.get(position));
            myHolder.itemView.setOnClickListener(view -> {
                UserModel.User user = list.get(myHolder.getAdapterPosition());
                fragment_patient.setItemData(user);
            });
        }else if (holder instanceof LoadMoreHolder){
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.prgBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.prgBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private PatientRowBinding binding;

        public MyHolder(PatientRowBinding binding) {
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
        if (list.size()>0){
            if (list.get(position)==null){
                return LOAD;
            }else {
                return DATA;
            }
        }else {
            return DATA;

        }

    }
}
