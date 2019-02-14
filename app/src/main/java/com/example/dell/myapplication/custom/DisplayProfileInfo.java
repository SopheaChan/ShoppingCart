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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.model.UserInfo;
import com.example.dell.myapplication.ui.main.MainMvpPresenter;
import com.example.dell.myapplication.ui.main.MainPresenter;

import java.util.Objects;

public class DisplayProfileInfo {
    private Context context;
    private UserInfo userInfo;
    private MainMvpPresenter mainMvpPresenter;
    private DialogDisplayLoadingProgress loadingProgress;

    public DisplayProfileInfo(Context context, UserInfo userInfo){
        this.context = context;
        this.userInfo = userInfo;
        mainMvpPresenter = new MainPresenter(context);
        loadingProgress = new DialogDisplayLoadingProgress(context);

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
        final EditText tvName = dialog.findViewById(R.id.tvName);
        final EditText tvGender = dialog.findViewById(R.id.tvGender);
        final EditText tvTel = dialog.findViewById(R.id.tvTel);
        final EditText tvEmail = dialog.findViewById(R.id.tvEmail);
        final EditText tvOther = dialog.findViewById(R.id.tvOther);
        final Button btnEditProfile = dialog.findViewById(R.id.btnEditInfo);
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
                String buttonTitle = btnEditProfile.getText().toString();
                if (buttonTitle.equalsIgnoreCase("edit")){
                    tvName.setEnabled(true);
                    tvGender.setEnabled(true);
                    tvName.setEnabled(true);
                    tvTel.setEnabled(true);
                    tvOther.setEnabled(true);
                    btnEditProfile.setText("Save");
                } else {
//                    String name = tvName.getText().toString();
//                    String gender = tvGender.getText().toString();
//                    String tel = tvTel.getText().toString();
//                    String email = tvEmail.getText().toString();
//                    String otherContact = tvOther.getText().toString();
//                    UserInfo userInfo1 = new UserInfo(userInfo.getProfileUrl(), name, gender, tel, email, otherContact);
//                    mainMvpPresenter.onUpdateUserInfo(userInfo1, loadingProgress);

                    tvName.setEnabled(false);
                    tvGender.setEnabled(false);
                    tvName.setEnabled(false);
                    tvTel.setEnabled(false);
                    tvOther.setEnabled(false);
                    btnEditProfile.setText("Edit");
                }
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
