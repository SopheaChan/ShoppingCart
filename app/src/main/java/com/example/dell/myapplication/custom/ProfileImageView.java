package com.example.dell.myapplication.custom;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.listener.ProfileImageViewOnClickListener;

public class ProfileImageView {
    private Context context;
    private Dialog dialog;
    private Uri profileUri;
    private String buttonTitle;

    private Button btnDone;

    public ProfileImageView(Context context, Uri profileUri, String buttonTitle){
        this.context = context;
        this.profileUri = profileUri;
        this.buttonTitle = buttonTitle;
    }

    public ProfileImageView(Context context, Uri profileUri){
        this.context = context;
        this.profileUri = profileUri;
    }

    public void onDisplayProfilePicture(final ProfileImageViewOnClickListener callback){

        dialog = new Dialog(this.context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile_image_view);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        dialog.show();

        ImageView mProfileImage = dialog.findViewById(R.id.imgProfileImage);
        btnDone = dialog.findViewById(R.id.btnDone);
        if (buttonTitle != null){
            btnDone.setText(buttonTitle);
        }
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonTitle = btnDone.getText().toString();
                callback.onClickListener(buttonTitle, dialog);
            }
        });
        if (profileUri != null) {
            Glide.with(this.context).load(profileUri).into(mProfileImage);
        }
    }

    public void setButtonTitle(String title){
        btnDone.setText(title);
    }

    public Dialog getProfileImageView(){
        return this.dialog;
    }
}
