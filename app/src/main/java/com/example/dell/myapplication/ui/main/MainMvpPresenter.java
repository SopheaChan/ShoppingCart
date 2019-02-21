package com.example.dell.myapplication.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dell.myapplication.adapter.MyAdapter;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.model.Product;
import com.example.dell.myapplication.model.ProductData;
import com.example.dell.myapplication.model.UserInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public interface MainMvpPresenter {
    void onSignOut(Activity activity, Dialog dialog);
    void onViewProfileClicked();
    void onLoadUserInfo(Activity activity, TextView tvUserName, CircleImageView imgUserProfile);
    void onCheckOutClickedListener(double totalAmount, TextView tvTotalPrice, Button btnCancel,
            Button btnSubmit, Button btnCheckout);
    void onButtonConcelClickedListener(Button btnCancel, Button btnSubmit, Button btnCheckout,
                                       TextView tvTotalPrice);
    void onOpenGallery(Activity activity, Dialog dialog);
    void onViewProfileImage();
    void onUploadProfile(Context context, Uri imageUri, DialogDisplayLoadingProgress displayLoadingProgress);
    void onUpdateUserInfo(UserInfo userInfo, DialogDisplayLoadingProgress displayLoadingProgress,
                          EditText etName, EditText etGender,
                          EditText etTel, EditText etOther, Button btnDone);
    void setDataToList(MyAdapter myAdapter, List<ProductData> mProductList,
                       DialogDisplayLoadingProgress displayLoadingProgress);
    String addProductViewsRecord(String productID);
}
