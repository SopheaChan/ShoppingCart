package com.example.dell.myapplication.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myapplication.listener.OnDialogClickListener;
import com.example.dell.myapplication.R;

public class DialogMenu implements View.OnClickListener{
    private Context context;
    private Dialog dialog;
    private OnDialogClickListener callback;
    private static final int GALLERY = 1;
    private static final int CAMERA = 2;
    private static final int VIEW_PROFILE_PICTURE = 3;
    public  DialogMenu(Context context, OnDialogClickListener callback){
        this.context = context;
        this.callback = callback;
        this.dialog =  new Dialog(this.context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_profile_photo);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        dialog.setCancelable(true);
        dialog.show();

        TextView tvGallery = dialog.findViewById(R.id.tvGallery);
        TextView tvTakePhoto = dialog.findViewById(R.id.tvTakePhoto);
        TextView tvViewProfile = dialog.findViewById(R.id.tvViewProfilePicture);
        tvGallery.setOnClickListener(this);
        tvTakePhoto.setOnClickListener(this);
        tvViewProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvGallery: {
                this.callback.onItemClickListener(GALLERY, v, dialog);
                break;
            }
            case R.id.tvTakePhoto: {
                this.callback.onItemClickListener(CAMERA, v, dialog);
                break;
            }
            case R.id.tvViewProfilePicture: {
                this.callback.onItemClickListener(VIEW_PROFILE_PICTURE, v, dialog);
                break;
            }
            default: {
                Toast.makeText(context, "Nothing clicked...", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
