package com.example.dell.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myapplication.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    private List<Product> mProductList = new ArrayList<>();

    private TextView tvTotalPrice;

//    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        Button btnCheckout = findViewById(R.id.button_check_out);
        tvTotalPrice = findViewById(R.id.total_price);

        recyclerView = findViewById(R.id.my_recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new MyAdapter(this, mProductList, new MyAdapter.AddProductToCartListener() {
            @Override
            public void onClick(Product mProduct, TextView proQuantity) {
                int productQuantity = mProduct.getProQuantity();
                if (productQuantity < 20) {
                    productQuantity++;
                    mProduct.setProQuantity(productQuantity);
                    proQuantity.setText(String.format(Locale.US, "%d", productQuantity));
                    Toast.makeText(getApplicationContext(), "Added " + mProduct.getProName()
                            + " to cart!" + "\n" + "Current order: " + mProduct.getProQuantity()
                            + "\n" + "Amount: " + Double.toString(
                                    mProduct.getProPrice() * productQuantity), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You cannot make an order for 20 burgers a time.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new MyAdapter.DeleteProductFromCartListener() {
            @Override
            public void onClick(Product mProduct, TextView proQuantity) {
                int productQuantity = mProduct.getProQuantity();
                if (productQuantity > 0) {
                    productQuantity--;
                    mProduct.setProQuantity(productQuantity);
                    proQuantity.setText(String.format(Locale.US, "%d", productQuantity));
                    Toast.makeText(getApplicationContext(), "Remove " + mProduct.getProName() + " from cart!" +
                            "\n" + "Order remained: " + mProduct.getProQuantity()
                            + "\n" + "Amount: " + Double.toString(
                                    mProduct.getProPrice() * productQuantity), Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(myAdapter);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckout(mProductList);
            }
        });
//        profileImage.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                if (hasCamera()) {
//                    takePhoto();
//                } else {
//                    Toast.makeText(MainActivity.this, "No access for using camera", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        setDataToList();
    }

//    private void takePhoto() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//    }
//
//    private boolean hasCamera(){
//        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
//            Bundle extras = data.getExtras();
//            Bitmap photo = (Bitmap) extras.get("data");
////            profileImage.setImageBitmap(photo);
//        }
//    }

    //Called when checkout button is clicked...
    private void onCheckout(@org.jetbrains.annotations.NotNull List<Product> proList) {
        double totalAmount = 0.0;
        String dollarSymbol = "$";
        for (int i = 0; i < proList.size(); i++) {
            Product product = proList.get(i);
            totalAmount += product.getProPrice() * product.getProQuantity();
        }
        //Split the number after the dot of total amount, only two characters will be taken
        DecimalFormat df = new DecimalFormat("#.##");
        String dx = df.format(totalAmount);
        totalAmount = Double.valueOf(dx);
        tvTotalPrice.setText(String.format(Locale.US, dollarSymbol + "%f", totalAmount));
    }

    private void setDataToList() {
        Product product1 = new Product(R.drawable.black_burger, "Black Pork Burger", 5.6, 1);
        Product product2 = new Product(R.drawable.black_burger, "Black Pork Burger", 7.8, 2);
        Product product3 = new Product(R.drawable.black_burger, "Black Pork Burger", 4.00, 3);
        Product product4 = new Product(R.drawable.black_burger, "Black Pork Burger", 6.2, 4);
        Product product5 = new Product(R.drawable.black_burger, "Black Pork Burger", 3.1, 5);
        Product product6 = new Product(R.drawable.black_burger, "Black Pork Burger", 9.01, 6);
        mProductList.add(product1);
        mProductList.add(product2);
        mProductList.add(product3);
        mProductList.add(product4);
        mProductList.add(product5);
        mProductList.add(product6);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemID = menuItem.getItemId();
        switch (itemID) {
            case R.id.profile: {
                Toast.makeText(this, "Navigation menu clicked Profile", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.activity: {
                Toast.makeText(this, "Navigation menu clicked Activity", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.setting: {
                Toast.makeText(this, "Navigation menu clicked Setting", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.ordered_list: {
                Toast.makeText(this, "Navigation menu clicked Ordered list", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.sign_out: {
                Toast.makeText(this, "Navigation menu clicked Sign out", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;
        }
        return true;
    }
}
