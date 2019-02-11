package com.example.dell.myapplication.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.custom.DisplayProfileInfo;
import com.example.dell.myapplication.model.UserInfo;
import com.example.dell.myapplication.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainPresenter implements MainMvpPresenter{
    private FirebaseAuth mAuth;
    private Context context;

    public MainPresenter(Context context){
        this.context = context;
    }
    @Override
    public void onSignOut(final Activity activity, final Dialog dialog) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                context.startActivity(new Intent(context, LoginActivity.class));
                activity.finish();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void viewProfile(Context context, UserInfo userInfo) {
        new DisplayProfileInfo(context, userInfo);
    }


}
