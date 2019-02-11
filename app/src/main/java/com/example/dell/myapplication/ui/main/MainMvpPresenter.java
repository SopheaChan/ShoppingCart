package com.example.dell.myapplication.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.example.dell.myapplication.model.UserInfo;

public interface MainMvpPresenter {
    void onSignOut(Activity activity, Dialog dialog);
    void viewProfile(Context context, UserInfo userInfo);
}
