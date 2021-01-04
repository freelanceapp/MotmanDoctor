package com.motman_doctor.ui.activity_sign_up.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.motman_doctor.R;
import com.motman_doctor.adapters.SpinnerAdapter;
import com.motman_doctor.adapters.SpinnerCityAdapter;
import com.motman_doctor.adapters.SpinnerSpicialAdapter;
import com.motman_doctor.databinding.DialogSelectImageBinding;
import com.motman_doctor.databinding.FragmentSignUp2Binding;
import com.motman_doctor.models.AllCityModel;
import com.motman_doctor.models.AllSpiclixationModel;
import com.motman_doctor.models.CityModel;
import com.motman_doctor.models.SignUpModel;
import com.motman_doctor.models.SpecializationModel;
import com.motman_doctor.mvp.fragment_signup2_mvp.SignupFragmentView;
import com.motman_doctor.mvp.fragment_signup2_mvp.SignupPresenter;
import com.motman_doctor.share.Common;
import com.motman_doctor.ui.activity_sign_up.SignUpActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class FragmentSignUp2 extends Fragment implements SignupFragmentView {
    private SignUpActivity activity;
    private static final String TAG = "DATA";
    private FragmentSignUp2Binding binding;
    public SignUpModel signUpModel;
    private SignupPresenter presenter;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private Uri uri = null;
    private AlertDialog dialog;
    private ProgressDialog dialog2;
    private List<SpecializationModel> specializationModelList;
    private List<CityModel> cityModelList;
    private List<String> genderList;
    private SpinnerSpicialAdapter spinnerSpicialAdapter;
    private SpinnerCityAdapter spinnerCityAdapter;
    private SpinnerAdapter genderAdapter;
    private String lang;

    public static FragmentSignUp2 newInstance(SignUpModel signUpModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, signUpModel);
        FragmentSignUp2 fragmentSignUp2 = new FragmentSignUp2();
        fragmentSignUp2.setArguments(bundle);
        return fragmentSignUp2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up2, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.flSelectImage.setOnClickListener(view2 -> dialog.show());
        binding.setModel(signUpModel);
        createImageDialogAlert();
    }

    private void initView() {
        activity = (SignUpActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            signUpModel = (SignUpModel) bundle.getSerializable(TAG);
        }
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        presenter = new SignupPresenter(this, activity);
        specializationModelList=new ArrayList<>();
        genderList = new ArrayList<>();
        genderAdapter = new SpinnerAdapter(genderList,activity);
        binding.spinnerGender.setAdapter(genderAdapter);
        cityModelList=new ArrayList<>();
        spinnerCityAdapter=new SpinnerCityAdapter(cityModelList,activity);
        spinnerSpicialAdapter=new SpinnerSpicialAdapter(specializationModelList,activity);
        binding.spinnerCity.setAdapter(spinnerCityAdapter);
        binding.spinnerspicial.setAdapter(spinnerSpicialAdapter);
        binding.spinnerspicial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    signUpModel.setSpecialization_id(0);
                }else {
                    signUpModel.setSpecialization_id(specializationModelList.get(i).getId());
                }
                binding.setModel(signUpModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        binding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i==0){
//                    signUpModel.setGender("");
//                }else {if(lang.equals("ar")){
//                    if(i==1){
//                        signUpModel.setGender("male");
//                    }
//                    else {
//                        signUpModel.setGender("female");
//                    }
//                }else {
//
//                    signUpModel.setGender(genderList.get(i));}
//                }
//                binding.setModel(signUpModel);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        binding.spinnerGender.setVisibility(View.GONE);
        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    signUpModel.setCity_id(0);
                }else {
                    signUpModel.setCity_id(cityModelList.get(i).getId());
                }
                binding.setModel(signUpModel);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        presenter.getSpecilization();
        presenter.getcities();
        presenter.getGender();


    }


    private void createImageDialogAlert() {
        dialog = new AlertDialog.Builder(activity)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_select_image, null, false);
        binding.btnCamera.setOnClickListener(view -> {
            dialog.dismiss();
            checkCameraPermission();
        });
        binding.btnGallery.setOnClickListener(view -> {
            dialog.dismiss();
            checkReadPermission();
        });
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
    }

    public void checkReadPermission() {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {


        if (ContextCompat.checkSelfPermission(activity, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, req);
            } catch (SecurityException e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {

            uri = data.getData();
            File file = new File(Common.getImagePath(activity, uri));
            Picasso.get().load(file).fit().into(binding.imageLicense);
            binding.icon.setVisibility(View.GONE);
            signUpModel.setLicenseImage(uri.toString());
            binding.setModel(signUpModel);

        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uri = getUriFromBitmap(bitmap);
            binding.icon.setVisibility(View.GONE);

            if (uri != null) {
                signUpModel.setLicenseImage(uri.toString());
                binding.setModel(signUpModel);
                String path = Common.getImagePath(activity, uri);
                if (path != null) {
                    Picasso.get().load(new File(path)).fit().into(binding.imageLicense);

                } else {
                    Picasso.get().load(uri).fit().into(binding.imageLicense);

                }
            }


        }

    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "", ""));
    }


    @Override
    public void onSuccess(AllSpiclixationModel apointmentModel) {
specializationModelList.add(new SpecializationModel(activity.getResources().getString(R.string.ch_specialization)));
specializationModelList.addAll(apointmentModel.getData());
spinnerSpicialAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoad() {
        dialog2 = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog2.setCancelable(false);
        dialog2.show();
    }

    @Override
    public void onFinishload() {
        dialog2.dismiss();
    }

    @Override
    public void onSuccesscitie(AllCityModel body) {
        cityModelList.add(new CityModel(activity.getResources().getString(R.string.ch_city)));
        cityModelList.addAll(body.getData());
        spinnerCityAdapter.notifyDataSetChanged();
    }
    @Override
    public void onGenderSuccess(List<String> genderList)
    {
        this.genderList.clear();
        this.genderList.addAll(genderList);
        genderAdapter.notifyDataSetChanged();
    }
}
