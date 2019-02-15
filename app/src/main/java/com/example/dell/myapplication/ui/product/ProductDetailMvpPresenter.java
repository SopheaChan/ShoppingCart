package com.example.dell.myapplication.ui.product;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public interface ProductDetailMvpPresenter {
    void onAddToCart(int proQuantity, double proPrice, TextView tvOrderedProduct,
                     TextView tvTotalAmount, EditText etOrderedQuantity);
    void setDataToView(int proQuantity, double proPrice, TextView tvPro_Name, TextView tvPro_Price,
                       TextView tvOrderedProduct, TextView tvTotalAmount, TextView tvCompanyName,
                       TextView tvTel, ImageView imgPro_Image);
}
