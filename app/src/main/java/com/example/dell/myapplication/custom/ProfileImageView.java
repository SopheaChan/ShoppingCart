package com.example.dell.myapplication.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.button.MaterialButton;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.listener.OnDialogClickListener;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.listener.ProfileImageViewOnClickListener;

public class ProfileImageView implements View.OnClickListener {
    private Context context;
    private Dialog dialog;
    private static final int DONE = 1;
    private ProfileImageViewOnClickListener callback;

    public ProfileImageView(Context context, Uri profileUri, ProfileImageViewOnClickListener callback){
        this.context = context;
        this.callback = callback;

        this.dialog = new Dialog(this.context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile_image_view);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        dialog.show();

        ImageView mProfileImage = dialog.findViewById(R.id.imgProfileImage);
        Button btnDone = dialog.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
        if (profileUri != null) {
            Glide.with(this.context).load(profileUri).into(mProfileImage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDone: {
                this.callback.onClickListener(DONE, v, dialog);
                break;
            }
            default:{
                break;
            }
        }
    }
}
