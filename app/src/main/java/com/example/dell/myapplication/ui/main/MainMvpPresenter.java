package com.example.dell.myapplication.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.example.dell.myapplication.model.Product;
import com.example.dell.myapplication.model.UserInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public interface MainMvpPresenter {
    void onSignOut(Activity activity, Dialog dialog);
    void onViewProfileClicked();
    void onLoadUserInfo(Activity activity, TextView tvUserName, CircleImageView imgUserProfile);
    void onCheckOutClickedListener(List<Product> productList, TextView tvTotalPrice);
    void onOpenGallery(Activity activity, Dialog dialog);
    void onViewProfileImage();
}
