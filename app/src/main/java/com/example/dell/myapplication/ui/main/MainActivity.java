package com.example.dell.myapplication.ui.main;

import android.Manifest;
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.adapter.MyAdapter;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.custom.DialogMenu;
import com.example.dell.myapplication.custom.DisplayProfileInfo;
import com.example.dell.myapplication.custom.ProfileImageView;
import com.example.dell.myapplication.listener.OnDialogClickListener;
import com.example.dell.myapplication.listener.ProfileImageViewOnClickListener;
import com.example.dell.myapplication.model.CompanyInfo;
import com.example.dell.myapplication.model.Product;
import com.example.dell.myapplication.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private MyAdapter myAdapter;

    private List<Product> mProductList = new ArrayList<>();

    private TextView tvTotalPrice;

    private CircleImageView profileImage;
    private TextView tvUserName;
    private Uri mProfileUri;
    private Bitmap mProfileBitmap;

    private static final int REQUEST_GALLERY_ACCESS = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;

    private MainMvpPresenter mainMvpPresenter = new MainPresenter(MainActivity.this);
    private DialogDisplayLoadingProgress displayLoadingProgress;
    private ProfileImageView profileImageView;

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

        RecyclerView recyclerView = findViewById(R.id.my_recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        displayLoadingProgress = new DialogDisplayLoadingProgress(MainActivity.this);

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
        });

        recyclerView.setAdapter(myAdapter);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMvpPresenter.onCheckOutClickedListener(mProductList, tvTotalPrice);
            }
        });
        navigationView = findViewById(R.id.navigation_view);

        View headerView = navigationView.getHeaderView(0);
        tvUserName = headerView.findViewById(R.id.tvProfileName);
        profileImage = headerView.findViewById(R.id.imgProfilePhoto);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogMenu(MainActivity.this, new OnDialogClickListener() {
                    @Override
                    public void onItemClickListener(final int result, View view, final Dialog dialog) {
                        checkDeviceCamera(view);
                        switch (result) {
                            case 1: {
                                mainMvpPresenter.onOpenGallery(MainActivity.this, dialog);
                                break;
                            }
                            case 2: {
                                if (checkCameraPermission()) {
                                    dispatchTakePictureIntent();
                                }
                                break;
                            }
                            case 3: {
                                mainMvpPresenter.onViewProfileImage();
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

        @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void checkDeviceCamera(View view) {
        PackageManager packageManager = MainActivity.this.getPackageManager();
        final boolean deviceHasCameraFlag = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!deviceHasCameraFlag) {
            Toast.makeText(MainActivity.this, "Device has no camera", Toast.LENGTH_LONG).show();
            view.findViewById(R.id.tvTakePhoto).setEnabled(false);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            dialog.dismiss();
        }
    }

//    private void openGallery(Dialog dialog) {
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, REQUEST_GALLERY_ACCESS);
//        dialog.dismiss();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            dispatchTakePictureIntent();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_ACCESS && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            profileImageView = new ProfileImageView(MainActivity.this, imageUri, "Upload");
            profileImageView.onDisplayProfilePicture(new ProfileImageViewOnClickListener() {
                @Override
                public void onClickListener(String buttonTitle, Dialog dialog) {
                    if (buttonTitle.equalsIgnoreCase("upload")){
                        Toast.makeText(MainActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Glide.with(MainActivity.this).load(imageUri).into(profileImage);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mProfileUri = null;
            mProfileBitmap = imageBitmap;
            Log.d("ProfileBitmap", String.valueOf(mProfileBitmap));
            Log.d("ProfileUri", String.valueOf(mProfileUri));
            profileImage.setImageBitmap(imageBitmap);
        } else {
            Toast.makeText(MainActivity.this, "Nothing to do...", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataToList() {
        mainMvpPresenter.onLoadUserInfo(MainActivity.this, tvUserName, profileImage);
        CompanyInfo companyInfo1 = new CompanyInfo("Dream Farm", "+855 16 552 693", "dreamfarm@gmail.com");
        Product product1 = new Product(R.drawable.black_burger, "Black Burger", 5.6, 0, companyInfo1);
        CompanyInfo companyInfo2 = new CompanyInfo("SR Healthy Farm", "+855 16 622 666", "srhealthyfarm@gmail.com");
        Product product2 = new Product(R.drawable.fresh_milk1, "Fresh Milk", 7.8, 0, companyInfo2);
        CompanyInfo companyInfo3 = new CompanyInfo("Eco-famFarm", "+855 12 222 999", "eco-famfarm@gmail.com");
        Product product3 = new Product(R.drawable.fresh_pork, "Fresh Pork", 4.00, 0, companyInfo3);
//        CompanyInfo companyInfo4 = new CompanyInfo("Cambo-Farm", "+855 23 777 722", "cambo-farm@gmail.com");
//        Product product4 = new Product(R.drawable.giant_lobster, "Khmer Giant Lobster", 6.2, 0, companyInfo4);
        CompanyInfo companyInfo5 = new CompanyInfo("HC Community Farm", "+855 23 556 666", "hccommunityfarm@gmail.com");
        Product product5 = new Product(R.drawable.khmer_chicken, "Khmer Chicken", 3.1, 0, companyInfo5);
        CompanyInfo companyInfo6 = new CompanyInfo("Cambo Natural Farm", "+855 23 444 422", "cambonaturalfarm@gmail.com");
        Product product6 = new Product(R.drawable.khmer_banana, "Banana", 9.01, 0, companyInfo6);
        mProductList.add(product1);
        mProductList.add(product2);
        mProductList.add(product3);
//        mProductList.add(product4);
        mProductList.add(product5);
        mProductList.add(product6);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemID = menuItem.getItemId();
        switch (itemID) {
            case R.id.profile: {
                mainMvpPresenter.onViewProfileClicked();
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
                displayLoadingProgress.displayLoadingProgress("Logging out...");
                mainMvpPresenter.onSignOut(this, displayLoadingProgress.getDialog());
                break;
            }
            default:
                break;
        }
        return true;
    }

    private boolean checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return false;
        }

        return true;
    }

}