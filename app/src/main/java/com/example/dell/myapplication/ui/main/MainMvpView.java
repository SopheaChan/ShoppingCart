package com.example.dell.myapplication.ui.main;

import android.widget.Button;
import android.widget.EditText;

import com.example.dell.myapplication.model.UserInfo;

public interface MainMvpView {
    void onUpdateUserInfoSuccess(UserInfo userInfo, EditText etName, EditText etGender,
                                 EditText etTel, EditText etOther, Button btnDone);
}
