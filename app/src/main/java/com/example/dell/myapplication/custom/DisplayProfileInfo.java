package com.example.dell.myapplication.custom;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.model.UserInfo;

import java.util.Objects;

public class DisplayProfileInfo {
    private Context context;
    private UserInfo userInfo;

    public DisplayProfileInfo(Context context, UserInfo userInfo){
        this.context = context;
        this.userInfo = userInfo;

    }

    public void viewUserInfo(){
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profile_info_layout);
        Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.CENTER_VERTICAL);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );
        dialog.setCancelable(true);
        dialog.show();

        ImageView profileImage = dialog.findViewById(R.id.imgProfileImage);
        TextView tvName = dialog.findViewById(R.id.tvName);
        TextView tvGender = dialog.findViewById(R.id.tvGender);
        TextView tvTel = dialog.findViewById(R.id.tvTel);
        TextView tvEmail = dialog.findViewById(R.id.tvEmail);
        TextView tvOther = dialog.findViewById(R.id.tvOther);
        Button btnEditProfile = dialog.findViewById(R.id.btnEditInfo);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        Glide.with(context).load(Uri.parse(userInfo.getProfileUrl())).into(profileImage);
        tvName.setText(userInfo.getuName());
        tvGender.setText(userInfo.getuGender());
        tvTel.setText(userInfo.getuTel());
        tvEmail.setText(userInfo.getuEmail());
        tvOther.setText(userInfo.getuOther());

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Todo.....................*/
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
