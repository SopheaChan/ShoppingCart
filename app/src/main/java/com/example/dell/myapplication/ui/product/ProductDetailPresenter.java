package com.example.dell.myapplication.ui.product;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class ProductDetailPresenter implements ProductDetailMvpPresenter{
    @Override
    public void onAddToCart(int proQuantity, double proPrice, TextView tvOrderedProduct,
                            TextView tvTotalAmount, EditText etOrderedQuantity) {
        proQuantity += Integer.parseInt(etOrderedQuantity.getText().toString());
        double mAmount = proQuantity * proPrice;

        tvOrderedProduct.setText(String.format(Locale.US, "%d", proQuantity));
        tvTotalAmount.setText(String.format(Locale.US, "%.2f", mAmount));
        etOrderedQuantity.setText("");
    }

    @Override
    public void setDataToView(int proQuantity, double proPrice, TextView tvPro_Name, TextView tvPro_Price,
                              TextView tvOrderedProduct, TextView tvTotalAmount, TextView tvCompanyName,
                              TextView tvTel, ImageView imgPro_Image) {

    }
}
