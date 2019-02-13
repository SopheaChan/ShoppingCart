package com.example.dell.myapplication.ui.login;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.myapplication.custom.DialogChooseImageForSignUp;
import com.example.dell.myapplication.ui.main.MainActivity;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.ui.sign_up.SignUpActivity;
import com.google.firebase.FirebaseApp;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        LoginMvpView {

    private Button btnLogin;
    private TextView btnSignUp;
    private TextInputLayout etEmail;
    private TextInputLayout etPassword;

    private LoginMvpPresenter mLoginMvpPresenter = new LoginPresenter(this);
    private DialogDisplayLoadingProgress mDisplayLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        /*set activity transition feature
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Fade());*/
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.tvSignUpButton);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        mDisplayLoadingProgress = new DialogDisplayLoadingProgress(LoginActivity.this);
        mLoginMvpPresenter.checkUser(this);
    }

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        switch (buttonID) {
            case R.id.btnLogin: {
                String email = etEmail.getEditText().getText().toString();
                String password = etPassword.getEditText().getText().toString();
                if (password.length()<6 || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        etEmail.setError("invalid email");
                    }
                    if (password.length()<6){
                        etPassword.setError("invalid password");
                    }
                    return;
                } else {
                    mDisplayLoadingProgress.displayLoadingProgress("Logging in...");
                    mLoginMvpPresenter.onLogin(email, password, LoginActivity.this);
                }
                break;
            }
            case R.id.tvSignUpButton: {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        }
    }

    @Override
    public void onLoginSuccess() {
        /*process activity transition
        startActivity(new Intent(LoginActivity.this, MainActivity.class),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());*/
        startActivity(new Intent(LoginActivity.this, MainActivity.class),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        mDisplayLoadingProgress.getDialog().dismiss();
        finish();
    }

    @Override
    public void onLoginFailed() {
        mDisplayLoadingProgress.getDialog().dismiss();
    }
}
