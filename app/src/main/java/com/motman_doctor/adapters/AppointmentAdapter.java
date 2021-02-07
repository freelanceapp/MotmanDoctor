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
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.ui.activity_home.fragments.Fragment_Home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class AppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int LOAD = 2;
    private final String lang;
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
        Paper.init(context);
        lang = Paper.book().read("lang", "ar");
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
            myHolder.binding.setLang(lang);
            myHolder.binding.setModel(list.get(position));
            String myTime = list.get(position).getDate()+" "+list.get(position).getTime()+" "+ list.get(position).getTime_type();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa", Locale.US);
            Date d = null;
            Log.e("dlldl",myTime);
            try {
                d = df.parse(myTime);
            } catch (ParseException e) {
                Log.e("llflfl",e.toString());
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, Integer.parseInt(Preferences.getInstance().getUserData(context).getData().getDetection_time()));
            long time = System.currentTimeMillis();

//            if(time>cal.getTimeInMillis()){
//                myHolder.binding.image.setEnabled(false);
//                myHolder.binding.image.setClickable(false);
//
//            }
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
                        fragment_home.setitem(list.get(position),list.get(position).getId(),list.get(position).getReservation_type(),list.get(position).getReservation_status());
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
