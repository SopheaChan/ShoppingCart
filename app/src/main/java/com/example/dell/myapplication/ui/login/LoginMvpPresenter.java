package com.example.dell.myapplication.ui.login;

import android.app.Activity;
import android.content.Context;

import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;

interface LoginMvpPresenter {
    void onLogin(String email, String password, Context context);
    void checkUser(Activity activity);
}
