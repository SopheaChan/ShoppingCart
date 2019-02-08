package com.example.dell.myapplication.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dell.myapplication.MainActivity;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:{
                new LoginMvpView() {
                    @Override
                    public void onLoginClickListener() {
                        new DialogDisplayLoadingProgress(LoginActivity.this, this);
                    }

                    @Override
                    public void onLoginSuccess() {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }.onLoginClickListener();
                break;
            }
            default:{
                Log.d("Button ID: ", Integer.toString(v.getId())+"?="+Integer.toString(R.id.btnLogin));
                Snackbar.make(btnLogin, "No action matches this button.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

}
