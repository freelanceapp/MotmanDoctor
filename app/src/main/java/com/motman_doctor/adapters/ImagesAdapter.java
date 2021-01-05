package com.motman_doctor.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.motman_doctor.R;
import com.motman_doctor.databinding.AddImagesMoreRowBinding;
import com.motman_doctor.databinding.AddImagesRowBinding;
import com.motman_doctor.ui.activity_sign_up.fragments.FragmentSignUp2;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int ADDMORE = 2;
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public ImagesAdapter(List<String> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            AddImagesRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.add_images_row, parent, false);
            return new MyHolder(binding);
        } else {
            AddImagesMoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.add_images_more_row, parent, false);
            return new AddMoreMoreHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            String uri = list.get(position);
            Picasso.get().load(Uri.parse(uri)).fit().into(myHolder.binding.image);
            myHolder.binding.cardViewDelete.setOnClickListener(v -> {
                if (fragment instanceof FragmentSignUp2) {
                    FragmentSignUp2 fragmentSignUp2 = (FragmentSignUp2) fragment;
                    fragmentSignUp2.delete(myHolder.getAdapterPosition());

                }
            });


        } else if (holder instanceof AddMoreMoreHolder) {
            AddMoreMoreHolder addMoreMoreHolder = (AddMoreMoreHolder) holder;
            addMoreMoreHolder.itemView.setOnClickListener(v -> {
                if (fragment instanceof FragmentSignUp2) {
                    Log.e("Dldldld","dkdkdk");

                    FragmentSignUp2 fragmentSignUp2 = (FragmentSignUp2) fragment;
                    fragmentSignUp2.createImageDialogAlert();

                }


            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private AddImagesRowBinding binding;

        public MyHolder(AddImagesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public static class AddMoreMoreHolder extends RecyclerView.ViewHolder {
        private AddImagesMoreRowBinding binding;

        public AddMoreMoreHolder(AddImagesMoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null) {
            Log.e("yy", "yy");
            return ADDMORE;
        } else {
            return DATA;
        }
    }
}
