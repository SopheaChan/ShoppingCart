package com.example.dell.myapplication.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.model.UserInfo;

public class DisplayProfileInfo {
    private Dialog dialog;
    private UserInfo userInfo;

    public DisplayProfileInfo(Context context, UserInfo userInfo){
        this.userInfo = userInfo;

        this.dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profile_info_layout);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        dialog.setCancelable(true);
        dialog.show();

        ImageView profileImage = dialog.findViewById(R.id.imgProfileImage);
        TextView tvName = dialog.findViewById(R.id.tvName);
        TextView tvGender = dialog.findViewById(R.id.tvGender);
        TextView tvTel = dialog.findViewById(R.id.tvTel);
        TextView tvEmail = dialog.findViewById(R.id.tvEmail);
        TextView tvOther = dialog.findViewById(R.id.tvOther);

        profileImage.setImageURI(userInfo.getProfileUrl());
        tvName.setText(userInfo.getuName());
        tvGender.setText(userInfo.getuGender());
        tvTel.setText(userInfo.getuTel());
        tvEmail.setText(userInfo.getuEmail());
        tvOther.setText(userInfo.getuOther());
    }
}
