package com.example.dell.myapplication.ui.sale_product;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;

import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.model.CompanyInfo;
import com.example.dell.myapplication.model.Product;
import com.example.dell.myapplication.model.ProductData;

public interface AddProductToStoreMvpPresenter {
    void setButtonNextListener(String comName, String comTel, String comEmail,
                               EditText etComName, EditText etComTel, EditText etComEmail, Context context,
                               FragmentManager fragmentManager);
    void onButtonSubmitListener(Context context, ProductData productData, CompanyInfo companyInfo,
                                DialogDisplayLoadingProgress displayLoadingProgress);
    void loadUserData();
}
