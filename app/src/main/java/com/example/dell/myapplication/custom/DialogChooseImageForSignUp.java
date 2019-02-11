package com.example.dell.myapplication.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.ui.sign_up.SignUpActivity;
import com.example.dell.myapplication.ui.sign_up.SignUpMvpView;

public class DialogChooseImageForSignUp implements View.OnClickListener {
    private Dialog dialog;
    private Context context;
    private SignUpMvpView signUpMvpView = new SignUpActivity();

    public DialogChooseImageForSignUp(Context context){
        this.context = context;

        this.dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_profile_photo_for_sign_up);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        dialog.show();

        TextView tvGallery = dialog.findViewById(R.id.tvGallery);
        TextView tvCamera = dialog.findViewById(R.id.tvTakePhoto);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        tvGallery.setOnClickListener(this);
        tvCamera.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvGallery: {
                signUpMvpView.chooseImageFromGallery();
                break;
            }
            case R.id.tvTakePhoto: {

            }
            case R.id.tvCancel: {
                break;
            }
            default:
                break;
        }
    }
}
