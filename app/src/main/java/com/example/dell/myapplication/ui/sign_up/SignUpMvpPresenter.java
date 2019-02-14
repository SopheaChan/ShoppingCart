package com.example.dell.myapplication.ui.sign_up;

import android.content.Context;

import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.model.UserInfo;

public interface SignUpMvpPresenter {
    void chooseImage(Context context, SignUpMvpView signUpMvpView);

    void onSignUpUser(Context context, UserInfo userInfo, String password,
                      DialogDisplayLoadingProgress displayLoadingProgress);

    String onGetDefaultProfileUrl();

    void onSignUpWithoutProfilePicture(Context context, UserInfo userInfo, String password,
                                       String imageUri, DialogDisplayLoadingProgress displayLoadingProgress);
}
