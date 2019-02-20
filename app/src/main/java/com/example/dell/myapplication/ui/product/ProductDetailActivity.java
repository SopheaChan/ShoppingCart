package com.example.dell.myapplication.ui.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.PushNotification;
import com.example.dell.myapplication.ui.map.MapsActivity;

import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private String mProductImage;
    private String mProductName;
    private int mProductQuantity;
    private double mProductPrice;
    private String mCompanyName;
    private String mTel;
    private String mDescription;

    private TextView tvProductName;
    private TextView tvProductPrice;
    private TextView tvOrderedProduct;
    private TextView tvTotalAmount;
    private TextView tvCompanyName;
    private TextView tvTel;
    private TextView tvViewOnMaps;
    private TextView tvDescription;
    private EditText etOrderQuantity;
    private ImageView imgProductImage;
    private ImageView btnAddToCart;
    private ImageView btnRemoveFromCart;
    private Button btnSubmit;

    private ProductDetailMvpPresenter productDetailMvpPresenter = new ProductDetailPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        mProductImage = getIntent().getStringExtra("proImage");
        mProductName = getIntent().getStringExtra("proName");
        mProductPrice = Double.parseDouble(getIntent().getStringExtra("proPrice"));
        mProductQuantity = Integer.parseInt(getIntent().getStringExtra("proQuantity"));
        mCompanyName = getIntent().getStringExtra("companyName");
        mTel = getIntent().getStringExtra("tel");
        mDescription = getIntent().getStringExtra("description");

        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvPrice);
        tvOrderedProduct = findViewById(R.id.tvOrderedItem);
        tvTotalAmount = findViewById(R.id.tvTotalAmount1);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvTel = findViewById(R.id.tvSalerTel);
        tvDescription = findViewById(R.id.tvDescription);
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
        Intent intent = new Intent(ProductDetailActivity.this, MapsActivity.class);
        intent.putExtra("companyName", mCompanyName);
        intent.putExtra("lat", 13.465109);
        intent.putExtra("lng", 106.602148);
        startActivity(intent);
    }

    private void onSubmitClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new PushNotification(ProductDetailActivity.this, mCompanyName, mProductName);
        } else {
            Toast.makeText(ProductDetailActivity.this, "Push notification function has been block" +
                    "with this version...", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(ProductDetailActivity.this, "Submitted order...", Toast.LENGTH_SHORT).show();
    }

    private void onRemoveFromCartClicked() {
        Toast.makeText(ProductDetailActivity.this, "Removed from cart...", Toast.LENGTH_SHORT).show();
    }

    private void onAddToCartClicked() {
        productDetailMvpPresenter.onAddToCart(mProductQuantity, mProductPrice, tvOrderedProduct,
                tvTotalAmount, etOrderQuantity);
    }

    private void setDataToView() {
        double totalAmount = mProductPrice * mProductQuantity;
        tvProductName.setText(mProductName);
        tvProductPrice.setText(String.format(Locale.US, "$" + "%.2f", mProductPrice));
        tvOrderedProduct.setText(String.format(Locale.US, "%d", mProductQuantity));
        tvTotalAmount.setText(String.format(Locale.US, "$" + "%.2f", totalAmount));
        tvCompanyName.setText(mCompanyName);
        tvTel.setText(mTel);
        tvDescription.setText(mDescription);
        Glide.with(this).load(Uri.parse(mProductImage)).into(imgProductImage);
    }
}
