package com.example.dell.myapplication.ui.product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.PushNotification;
import com.example.dell.myapplication.ui.map.MapsActivity;

import java.util.Locale;

public class ProductDetail extends AppCompatActivity {

    private int mProductImage;
    private String mProductName;
    private int mProductQuantity;
    private Double mProductPrice;
    private String mCompanyName;
    private String mTel;
    private Double mAmount;

    private TextView tvProductName;
    private TextView tvProductPrice;
    private TextView tvOrderedProduct;
    private TextView tvTotalAmount;
    private TextView tvCompanyName;
    private TextView tvTel;
    private TextView tvViewOnMaps;
    private EditText etOrderQuantity;
    private ImageView imgProductImage;
    private ImageView btnAddToCart;
    private ImageView btnRemoveFromCart;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        mProductImage = getIntent().getIntExtra("proImage", 0);
        mProductName = getIntent().getStringExtra("proName");
        mProductPrice = getIntent().getDoubleExtra("proPrice", 0.0);
        mProductQuantity = getIntent().getIntExtra("proQuantity", 0);
        mCompanyName = getIntent().getStringExtra("companyName");
        mTel = getIntent().getStringExtra("tel");

        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvPrice);
        tvOrderedProduct = findViewById(R.id.tvOrderedItem);
        tvTotalAmount = findViewById(R.id.tvTotalAmount1);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvTel = findViewById(R.id.tvTel);
        tvViewOnMaps = findViewById(R.id.tvViewOnMap);
        etOrderQuantity = findViewById(R.id.etOrderedQuantity);
        imgProductImage = findViewById(R.id.imgProductImage);
        btnAddToCart = findViewById(R.id.btnAddToCart1);
        btnRemoveFromCart = findViewById(R.id.btnRemoveFromCart1);
        btnSubmit = findViewById(R.id.btnSubmit1);

        setDataToView();
        setViewListener();
    }

    private void setViewListener() {
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddToCartClicked();
            }
        });

        btnRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemoveFromCartClicked();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClicked();
            }
        });

        tvViewOnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFindLocation();
            }
        });
    }

    private void onFindLocation() {
        Intent intent = new Intent(ProductDetail.this, MapsActivity.class);
        intent.putExtra("companyName", mCompanyName);
        intent.putExtra("lat", 13.465109);
        intent.putExtra("lng", 106.602148);
        startActivity(intent);
    }

    private void onSubmitClicked() {
        new PushNotification(ProductDetail.this, mCompanyName, mProductName);
        Toast.makeText(ProductDetail.this, "Submitted order...", Toast.LENGTH_SHORT).show();
    }

    private void onRemoveFromCartClicked() {
        Toast.makeText(ProductDetail.this, "Removed from cart...", Toast.LENGTH_SHORT).show();
    }

    private void onAddToCartClicked() {
        mProductQuantity += Integer.parseInt(etOrderQuantity.getText().toString());
        mAmount = mProductQuantity * mProductPrice;

        tvOrderedProduct.setText(String.format(Locale.US, "%d", mProductQuantity));
        tvTotalAmount.setText(String.format(Locale.US, "%.2f", mAmount));
        etOrderQuantity.setText("");
    }

    private void setDataToView() {
        double totalAmount = mProductPrice * mProductQuantity;
        tvProductName.setText(mProductName);
        tvProductPrice.setText(String.format(Locale.US, "$" + "%.2f", mProductPrice));
        tvOrderedProduct.setText(String.format(Locale.US, "%d", mProductQuantity));
        tvTotalAmount.setText(String.format(Locale.US, "$" + "%.2f", totalAmount));
        tvCompanyName.setText(mCompanyName);
        tvTel.setText(mTel);
        imgProductImage.setImageResource(mProductImage);
    }
}
