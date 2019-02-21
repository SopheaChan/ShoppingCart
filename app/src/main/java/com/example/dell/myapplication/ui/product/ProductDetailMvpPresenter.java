package com.example.dell.myapplication.ui.product;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public interface ProductDetailMvpPresenter {
    void onAddToCart(int proQuantity, double proPrice, TextView tvOrderedProduct,
                     TextView tvTotalAmount, EditText etOrderedQuantity);
    void onPhoneNumberClickedListener(Activity activity);
    void getProductViews(String productID, TextView productViewsCounter);
    void getSalerProfile(String userID, Context context, CircleImageView imgSalerProfile);

}
