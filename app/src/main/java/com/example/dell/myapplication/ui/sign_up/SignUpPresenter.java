package com.example.dell.myapplication.ui.sign_up;

import android.app.Activity;

import com.example.dell.myapplication.custom.DialogChooseImageForSignUp;

public class SignUpPresenter implements SignUpMvpPresenter{
    @Override
    public void chooseImage(Activity activity) {
        new DialogChooseImageForSignUp(activity.getBaseContext());
    }
}
