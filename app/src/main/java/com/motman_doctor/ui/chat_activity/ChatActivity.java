package com.motman_doctor.ui.chat_activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.motman_doctor.R;
import com.motman_doctor.adapters.Chat_Adapter;
import com.motman_doctor.databinding.ActivityChatBinding;
import com.motman_doctor.databinding.DialogSelectImageBinding;
import com.motman_doctor.language.Language;
import com.motman_doctor.models.ChatUserModel;
import com.motman_doctor.models.MessageDataModel;
import com.motman_doctor.models.MessageModel;
import com.motman_doctor.models.UserModel;
import com.motman_doctor.mvp.activity_chat_mvp.ActivityChatPresenter;
import com.motman_doctor.mvp.activity_chat_mvp.ChatActivityView;
import com.motman_doctor.preferences.Preferences;
import com.motman_doctor.share.Common;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ChatActivity extends AppCompatActivity implements ChatActivityView {
    private ActivityChatBinding binding;
    private String lang;
    private Chat_Adapter chat_adapter;
    private List<MessageModel> messagedatalist;
    private LinearLayoutManager manager;
    private Preferences preferences;
    private ActivityChatPresenter presenter;
    private UserModel userModel;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri url = null;
    private ChatUserModel chatUserModel;
    private int current_page = 1;
    private boolean isLoading=false;
    private ProgressDialog dialog2;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        initView();
        //   getChatMessages();
        presenter.getChatMessages(chatUserModel);

    }

    private void initView() {
        EventBus.getDefault().register(this);

        getDataFromIntent();
        messagedatalist = new ArrayList<>();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        presenter = new ActivityChatPresenter(this, this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setName(chatUserModel.getName());

        manager = new LinearLayoutManager(this);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(manager);
        chat_adapter = new Chat_Adapter(messagedatalist, userModel.getData().getId(), this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
        // binding.llMsgContainer.setVisibility(View.GONE);

        binding.recView.setAdapter(chat_adapter);
        binding.imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdata();
            }
        });

        binding.imagePhoto.setOnClickListener(view -> CreateImageAlertDialog());
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("sksks",dy+"");
                if (dy < 0) {
                    int lastItemPos = manager.findLastCompletelyVisibleItemPosition();
                    int total_items = chat_adapter.getItemCount();
                    Log.e("kdkdkdk",lastItemPos+" "+total_items);
                    if (!isLoading) {
                        isLoading = true;
                        messagedatalist.add(0, null);
                        chat_adapter.notifyItemInserted(0);
                        int next_page = current_page + 1;
                        presenter.loadMore(next_page, chatUserModel);


                    }
                }
            }
        });
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {

            chatUserModel = (ChatUserModel) intent.getSerializableExtra("chat_user_data");
            binding.setName(chatUserModel.getName());

        }
    }


    private void checkdata() {
        String message = binding.edtMsgContent.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            Common.CloseKeyBoard(this, binding.edtMsgContent);
            binding.edtMsgContent.setText("");
            // sendmessagetext(message);
            presenter.sendmessagetext(message, userModel, chatUserModel);

        } else {
            binding.edtMsgContent.setError(getResources().getString(R.string.field_req));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNewMessage(MessageModel messageModel) {
        messagedatalist.add(messageModel);
        scrollToLastPosition();
    }

    private void scrollToLastPosition() {

        new Handler()
                .postDelayed(() -> binding.recView.scrollToPosition(messagedatalist.size() - 1), 10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        preferences.clearChatUserData(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            url = getUriFromBitmap(bitmap);
            //sendmessageimage();
            presenter.sendmessageimage(userModel, chatUserModel, url);

        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            url = data.getData();
            presenter.sendmessageimage(userModel, chatUserModel, url);

            //   sendmessageimage();


        }

    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }


    @Override
    public void onFinished() {
        finish();
    }

    @Override
    public void onProgressShow() {
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressHide() {
        binding.progBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public void ondataload(MessageModel body) {
        preferences.create_update_ChatUserData(ChatActivity.this, chatUserModel);

        messagedatalist.clear();
        messagedatalist.addAll(body.getData());
        chat_adapter.notifyDataSetChanged();
        scrollToLastPosition();
    }

    @Override
    public void ondataloadmore(MessageModel body) {

        isLoading = false;
        messagedatalist.remove(0);
        chat_adapter.notifyItemRemoved(0);
        if(body.getCurrent_page()==current_page){
        current_page = body.getCurrent_page();

        messagedatalist.addAll(0, body.getData());
        chat_adapter.notifyItemRangeInserted(0, body.getData().size());
    }}

    @Override
    public void onremove() {
        isLoading = false;

        if (messagedatalist.get(0) == null) {
            messagedatalist.remove(0);
            chat_adapter.notifyItemRemoved(0);
        }
    }

    @Override
    public void onLoad() {
        dialog2 = Common.createProgressDialog(this, getString(R.string.wait));
        dialog2.setCancelable(false);
     //   dialog2.show();
    }

    @Override
    public void onFinishload() {
        dialog2.dismiss();
    }

    @Override
    public void ondataload(MessageDataModel body) {
        messagedatalist.add(body.getData());
        chat_adapter.notifyDataSetChanged();
        scrollToLastPosition();
    }

    @Override
    public void onfinishloadmore() {
      //  binding.progBarLoadMore.setVisibility(View.VISIBLE);
    }

    @Override
    public void onloadmore() {
//binding.progBarLoadMore.setVisibility(View.GONE);
    }
}
