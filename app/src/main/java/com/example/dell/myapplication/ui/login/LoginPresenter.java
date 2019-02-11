package com.example.dell.myapplication.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements LoginMvpPresenter {

    private FirebaseAuth mAuth;
    private LoginMvpView mLoginMvpView;

    public LoginPresenter(LoginMvpView mView){
        this.mLoginMvpView = mView;
    }

    @Override
    public void onLogin(String email, String password, final Context context) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mLoginMvpView.onLoginSuccess();
                        } else {
                            mLoginMvpView.onLoginFailed();
                        }
                    }
                })
                .addOnFailureListener((Activity) context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mLoginMvpView.onLoginFailed();
                        Snackbar.make(((Activity) context).findViewById(R.id.btnLogin), e.getMessage(),
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void checkUser(Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        try {
            if (firebaseUser != null){
                activity.startActivity(new Intent(activity.getBaseContext(), MainActivity.class));
                activity.finish();
            }
        }catch (NullPointerException e){
            Toast.makeText(activity.getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

