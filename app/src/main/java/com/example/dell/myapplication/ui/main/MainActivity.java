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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.adapter.MyAdapter;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.custom.DialogMenu;
import com.example.dell.myapplication.listener.OnDialogClickListener;
import com.example.dell.myapplication.model.Product;
import com.example.dell.myapplication.model.ProductData;
import com.example.dell.myapplication.model.UserInfo;
import com.example.dell.myapplication.ui.sale_product.AddProductToStoreActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
    MainMvpView{
    private MyAdapter myAdapter;

    private List<ProductData> mProductList = new ArrayList<>();

    private TextView tvTotalPrice;

    private CircleImageView profileImage;
    private TextView tvUserName;

    private int backPress = 0;

    private static final int REQUEST_GALLERY_ACCESS = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;

    private MainMvpPresenter mainMvpPresenter = new MainPresenter(MainActivity.this);
    private MainMvpPresenter mainMvpPresenter1;
    private DialogDisplayLoadingProgress displayLoadingProgress;

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
        displayLoadingProgress.displayLoadingProgress("Loading...");

        mainMvpPresenter1 = new MainPresenter(MainActivity.this, this, displayLoadingProgress);

        myAdapter = new MyAdapter(this, mProductList, new MyAdapter.AddProductToCartListener() {
            @Override
            public void onClick(ProductData mProduct, TextView proQuantity) {
                int productQuantity = Integer.parseInt(proQuantity.getText().toString());
                if (productQuantity < Integer.parseInt(mProduct.getProductQuantity())) {
                    productQuantity++;
//                    mProduct.setProductQuantity(Integer.toString(productQuantity));
                    proQuantity.setText(String.format(Locale.US, "%d", productQuantity));
                    Snackbar.make(proQuantity, "Added " + mProduct.getProductTitle()
                            + " to cart!" + "\n" + "Current order: " + productQuantity
                            + "     " + "Amount: " + Double.toString(
                            Double.parseDouble(mProduct.getProductPrice().replace('$', ' '))
                                    * productQuantity), Snackbar.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You cannot make an order for 20 burgers a time.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new MyAdapter.DeleteProductFromCartListener() {
            @Override
            public void onClick(ProductData mProduct, TextView proQuantity) {
                int productQuantity = Integer.parseInt(proQuantity.getText().toString());
                if (productQuantity > 0) {
                    productQuantity--;
                    mProduct.setProductQuantity(Integer.toString(productQuantity));
                    proQuantity.setText(String.format(Locale.US, "%d", productQuantity));
                    Snackbar.make(proQuantity, "Removed " + mProduct.getProductTitle() + " from cart!" +
                            "\n" + "Order remained: " + productQuantity
                            + "     " + "Amount: " + Double.toString(Double.parseDouble(mProduct.getProductPrice().replace('$', ' '))
                            * productQuantity), Snackbar.LENGTH_SHORT).show();
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
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
            if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
                backPress = 0;
            } else if (backPress <1){
                Toast.makeText(MainActivity.this, "Press back again to exit!", Toast.LENGTH_SHORT).show();
                backPress++;
            } else {
                finish();
            }
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
            final Uri imageUri = data.getData();
            mainMvpPresenter.onUploadProfile(MainActivity.this, imageUri, displayLoadingProgress);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "profile_picture", null);
            mainMvpPresenter.onUploadProfile(MainActivity.this, Uri.parse(path), displayLoadingProgress);
        } else {
            Toast.makeText(MainActivity.this, "Nothing to do...", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataToList() {
        mainMvpPresenter.onLoadUserInfo(MainActivity.this, tvUserName, profileImage);
        mainMvpPresenter.setDataToList(myAdapter, mProductList, displayLoadingProgress);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemID = menuItem.getItemId();
        switch (itemID) {
            case R.id.profile: {
                mainMvpPresenter1.onViewProfileClicked();
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
            case R.id.sale_product: {
                startActivity(new Intent(MainActivity.this, AddProductToStoreActivity.class));
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

    @Override
    public void onUpdateUserInfoSuccess(UserInfo userInfo, EditText etName, EditText etGender,
                                        EditText etTel, EditText etOther, Button btnDone) {
        mainMvpPresenter.onUpdateUserInfo(userInfo, displayLoadingProgress, etName, etGender, etTel,
                etOther, btnDone);
    }

    @Override
    protected void onStop() {
        super.onStop();
        backPress = 0;
    }
}
