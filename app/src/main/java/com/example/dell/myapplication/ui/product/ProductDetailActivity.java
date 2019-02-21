package com.example.dell.myapplication.ui.product;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.PushNotification;
import com.example.dell.myapplication.ui.map.MapsActivity;

import java.security.Permission;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class ProductDetailActivity extends AppCompatActivity{

    private String mProductImage;
    private String mProductName;
    private int mProductQuantity;
    private double mProductPrice;
    private String mCompanyName;
    private String mTel;
    private String mDescription;
    private String mProductID;
    private String mUserID;

    private TextView tvProductName;
    private TextView tvProductPrice;
    private TextView tvOrderedProduct;
    private TextView tvTotalAmount;
    private TextView tvCompanyName;
    private TextView tvTel;
    private TextView tvViewOnMaps;
    private TextView tvDescription;
    private TextView tvProductViewsCount;
    private EditText etOrderQuantity;
    private ImageView imgProductImage;
    private ImageView btnAddToCart;
    private ImageView btnRemoveFromCart;
    private Button btnSubmit;
    private CircleImageView imgSalerProfile;
    private android.support.v7.widget.Toolbar toolbar;

    private static final int REQUEST_PHONE_CALL_PERMISSION = 1;

    private ProductDetailMvpPresenter productDetailMvpPresenter = new ProductDetailPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        toolbar = findViewById(R.id.productDetailToolbar);
        setSupportActionBar(toolbar);

        mProductImage = getIntent().getStringExtra("proImage");
        mProductName = getIntent().getStringExtra("proName");
        mProductPrice = Double.parseDouble(getIntent().getStringExtra("proPrice"));
        mProductQuantity = Integer.parseInt(getIntent().getStringExtra("proQuantity"));
        mCompanyName = getIntent().getStringExtra("companyName");
        mTel = getIntent().getStringExtra("tel");
        mDescription = getIntent().getStringExtra("description");
        mProductID = getIntent().getStringExtra("productID");
        mUserID = getIntent().getStringExtra("userID");

        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvPrice);
        tvOrderedProduct = findViewById(R.id.tvOrderedItem);
        tvTotalAmount = findViewById(R.id.tvTotalAmount1);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvTel = findViewById(R.id.tvSalerTel);
        tvDescription = findViewById(R.id.tvDescription);
        tvViewOnMaps = findViewById(R.id.tvViewOnMap);
        tvProductViewsCount = findViewById(R.id.tvViewTimes);
        etOrderQuantity = findViewById(R.id.etOrderedQuantity);
        imgProductImage = findViewById(R.id.imgProductImage);
        imgSalerProfile = findViewById(R.id.imgSalerProfile);
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

        tvTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestPhoneCallPermission()) {
                    startPhoneCall();
                }
            }
        });
    }

    private void startPhoneCall() {
            try {
                Intent startPhoneCall = new Intent(Intent.ACTION_DIAL);
                if (startPhoneCall.resolveActivity(getPackageManager()) != null) {
                    startPhoneCall.setPackage("com.android.phone");
                    startPhoneCall.setData(Uri.parse("Tel: " + mTel));
                    startActivity(startPhoneCall);
                }
            } catch (ActivityNotFoundException e){
                Snackbar.make(tvTel, "Error: " + e.toString(), Snackbar.LENGTH_SHORT).show();
            }
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
//        tvProductViewsCount.setText(" " + mProductViews + " views");
        productDetailMvpPresenter.getProductViews(mProductID, tvProductViewsCount);
        Glide.with(this).load(Uri.parse(mProductImage)).into(imgProductImage);
        productDetailMvpPresenter.getSalerProfile(mUserID, this, imgSalerProfile);
    }

    private boolean requestPhoneCallPermission() {
        if (ActivityCompat.checkSelfPermission(ProductDetailActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProductDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_PHONE_CALL_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                startPhoneCall();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_language, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.khmer_language: {
                break;
            }
            case R.id.english_language: {
                break;
            }
            default: break;
        }
        return true;
    }
}
