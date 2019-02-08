package com.example.dell.myapplication.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.ui.LoginMvpView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogDisplayLoadingProgress {
    private Dialog dialog;

    public  DialogDisplayLoadingProgress(Context context, final LoginMvpView loginSuccessListener){

        this.dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading_progress);
        Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        dialog.show();

        CircleImageView imgLoading = dialog.findViewById(R.id.imgLoadingProgress);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        imgLoading.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loginSuccessListener.onLoginSuccess();
                dialog.dismiss();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
