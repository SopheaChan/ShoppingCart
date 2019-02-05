package com.example.dell.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.adapter.MyAdapter;
import com.example.dell.myapplication.custom.DialogItemDetail;
import com.example.dell.myapplication.custom.DialogMenu;
import com.example.dell.myapplication.custom.ProfileImageView;
import com.example.dell.myapplication.listener.OnDialogClickListener;
import com.example.dell.myapplication.listener.ProfileImageViewOnClickListener;
import com.example.dell.myapplication.model.CompanyInfo;
import com.example.dell.myapplication.model.Product;

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

    private NavigationView navigationView;
    private View headerView;
    private CircleImageView profileImage;
    private OnDialogClickListener onDialogClick;
    private ProfileImageView profileImageView;
    private ProfileImageViewOnClickListener tvProfileImageViewOnClickListener;

    private Uri mProfileUri;
    private Bitmap mProfileBitmap;

    private static final int REQUEST_GALLERY_ACCESS = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private DialogMenu dialogMenu;
    private DialogItemDetail dialogItemDetail;

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
                    Toast.makeText(getApplicationContext(), "Removed " + mProduct.getProName() + " from cart!" +
                            "\n" + "Order remained: " + mProduct.getProQuantity()
                            + "\n" + "Amount: " + Double.toString(
                            mProduct.getProPrice() * productQuantity), Toast.LENGTH_SHORT).show();
                }
            }
        }, new MyAdapter.ViewItemDetailListener() {
            @Override
            public void onClick() {
                dialogItemDetail = new DialogItemDetail(MainActivity.this);
            }
        });

        recyclerView.setAdapter(myAdapter);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckout(mProductList);
            }
        });
        navigationView = findViewById(R.id.navigation_view);

        headerView = navigationView.getHeaderView(0);
        profileImage = headerView.findViewById(R.id.imgProfilePhoto);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMenu = new DialogMenu(MainActivity.this, onDialogClick = new OnDialogClickListener() {
                    @Override
                    public void onItemClickListener(final int result, View view, final Dialog dialog) {
                        checkDeviceCamera(view);
                        switch (result) {
                            case 1: {
                                openGallery(dialog);
                                break;
                            }
                            case 2: {
                                dispatchTakePictureIntent(dialog);
                                break;
                            }
                            case 3: {
                                profileImageView = new ProfileImageView(MainActivity.this, mProfileUri, mProfileBitmap,
                                        tvProfileImageViewOnClickListener = new ProfileImageViewOnClickListener() {
                                            @Override
                                            public void onClickListener(int resultCode, View v, Dialog dialog1) {
                                                if (resultCode == 1) {
                                                    dialog1.dismiss();
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Have done nothing...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                dialog.dismiss();
                                break;
                            }
                            default: {
                                Toast.makeText(MainActivity.this, "Nothing clicked...", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                });
            }
        });

        setDataToList();
    }

    private void checkDeviceCamera(View view) {
        PackageManager packageManager = MainActivity.this.getPackageManager();
        final boolean deviceHasCameraFlag = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!deviceHasCameraFlag) {
            Toast.makeText(MainActivity.this, "Device has no camera", Toast.LENGTH_LONG).show();
            view.findViewById(R.id.tvTakePhoto).setEnabled(false);
        }
    }

    private void dispatchTakePictureIntent(Dialog dialog) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            dialog.dismiss();
        }
    }

    private void openGallery(Dialog dialog) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_ACCESS);
        dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_ACCESS && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            mProfileUri = imageUri;
            Log.d("ProfileBitmap", String.valueOf(mProfileBitmap));
            Log.d("ProfileUri", String.valueOf(mProfileUri));
            Glide.with(MainActivity.this).load(imageUri).into(profileImage);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mProfileBitmap = imageBitmap;
            Log.d("ProfileBitmap", String.valueOf(mProfileBitmap));
            Log.d("ProfileUri", String.valueOf(mProfileUri));
            profileImage.setImageBitmap(imageBitmap);
        } else {
            Toast.makeText(MainActivity.this, "Nothing to do...", Toast.LENGTH_SHORT).show();
        }
    }

    //Called when checkout button is clicked...
    private void onCheckout(@org.jetbrains.annotations.NotNull List<Product> proList) {
        double totalAmount = 0.0;
        String dollarSymbol = "$";
        for (int i = 0; i < proList.size(); i++) {
            Product product = proList.get(i);
            totalAmount += product.getProPrice() * product.getProQuantity();
        }
        //Split the number after the dot of total amount, only two characters will be taken
        tvTotalPrice.setText(String.format(Locale.US, dollarSymbol + "%.2f", totalAmount));
    }

    private void setDataToList() {
        CompanyInfo companyInfo1 = new CompanyInfo("Dream Farm", "+855 16 552 693", "dreamfarm@gmail.com");
        Product product1 = new Product(R.drawable.black_burger, "Black Burger", 5.6, 0, companyInfo1);
        CompanyInfo companyInfo2 = new CompanyInfo("SR Healthy Farm", "+855 16 622 666", "srhealthyfarm@gmail.com");
        Product product2 = new Product(R.drawable.fresh_milk1, "Fresh Milk", 7.8, 0, companyInfo2);
        CompanyInfo companyInfo3 = new CompanyInfo("Eco-famFarm", "+855 12 222 999", "eco-famfarm@gmail.com");
        Product product3 = new Product(R.drawable.fresh_pork, "High-Quality Fresh Pork", 4.00, 0, companyInfo3);
        CompanyInfo companyInfo4 = new CompanyInfo("Cambo-Farm", "+855 23 777 722", "cambo-farm@gmail.com");
        Product product4 = new Product(R.drawable.giant_lobster, "Khmer Giant Lobster", 6.2, 0, companyInfo4);
        CompanyInfo companyInfo5 = new CompanyInfo("HC Community Farm", "+855 23 556 666", "hccommunityfarm@gmail.com");
        Product product5 = new Product(R.drawable.khmer_chicken, "Khmer Chicken", 3.1, 0, companyInfo5);
        CompanyInfo companyInfo6 = new CompanyInfo("Cambo Natural Farm", "+855 23 444 422", "cambonaturalfarm@gmail.com");
        Product product6 = new Product(R.drawable.khmer_banana, "Banana", 9.01, 0, companyInfo6);
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
            case R.id.location_and_maps: {
                Toast.makeText(this, "Navigation menu clicked Location", Toast.LENGTH_SHORT).show();
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
