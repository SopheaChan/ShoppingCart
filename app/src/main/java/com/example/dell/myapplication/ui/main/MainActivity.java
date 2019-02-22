package com.example.dell.myapplication.ui.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.adapter.MyAdapter;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.custom.DialogMenu;
import com.example.dell.myapplication.listener.OnDialogMenuClickListener;
import com.example.dell.myapplication.model.ProductData;
import com.example.dell.myapplication.model.UserInfo;
import com.example.dell.myapplication.ui.sale_product.AddProductToStoreActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        MainMvpView {
    private MyAdapter myAdapter;

    private List<ProductData> mProductList = new ArrayList<>();

    private TextView tvTotalPrice;
    private CircleImageView profileImage;
    private TextView tvUserName;
    private Button btnSubmit;

    private int backPress = 0;
    private double amount = 0;
    private int orderedQuantity;

    private static final int REQUEST_GALLERY_ACCESS = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;

    private MainMvpPresenter mainMvpPresenter = new MainPresenter(MainActivity.this);
    private MainMvpPresenter mainMvpPresenter1;
    private DialogDisplayLoadingProgress displayLoadingProgress;

    private Locale myLocale;
    String currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBarGradiant(this);
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
        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        tvUserName = headerView.findViewById(R.id.tvProfileName);
        tvTotalPrice = findViewById(R.id.total_price);

        profileImage = headerView.findViewById(R.id.imgProfilePhoto);

        btnSubmit = findViewById(R.id.button_submit);
        btnSubmit.setVisibility(View.INVISIBLE);
        final Button btnCheckout = findViewById(R.id.button_check_out);

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
                onAddProductToCart(mProduct, proQuantity);
            }
        }, new MyAdapter.DeleteProductFromCartListener() {
            @Override
            public void onClick(ProductData mProduct, TextView proQuantity) {
                orderedQuantity = Integer.parseInt(proQuantity.getText().toString());
                onRemoveProductFromCart(mProduct, proQuantity);
            }
        });

        recyclerView.setAdapter(myAdapter);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMvpPresenter.onCheckOutClickedListener(amount, tvTotalPrice, btnSubmit, btnCheckout);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogMenu(MainActivity.this, new OnDialogMenuClickListener() {
                    @Override
                    public void onItemClickListener(final int result, View view, final Dialog dialog) {
                        checkDeviceCamera(view);
                        onProfileImageClicked(result, dialog);
                    }
                });
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMvpPresenter.onButtonConcelClickedListener(btnSubmit, btnCheckout, tvTotalPrice);
            }
        });

        setDataToList();
    }

    private void onAddProductToCart(ProductData mProduct, TextView proQuantity) {
        orderedQuantity = Integer.parseInt(proQuantity.getText().toString());
        final int availableQuantity = Integer.parseInt(mProduct.getProductQuantity());
        if (orderedQuantity < availableQuantity) {
            orderedQuantity++;
            String productUnitPrice = mProduct.getProductPrice().replace('$', ' ');
            amount += Double.parseDouble(productUnitPrice);
            proQuantity.setText(String.format(Locale.US, "%d", orderedQuantity));
            double amountForThisGood = orderedQuantity * Double.parseDouble(productUnitPrice);
            Snackbar.make(proQuantity, "Added " + mProduct.getProductTitle()
                    + " to cart!" + "\n" + "Current order: " + orderedQuantity
                    + "     " + "Amount: " + Double.toString(amountForThisGood), Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "There is only " + availableQuantity + " " + mProduct.getProductTitle() + " " +
                            "available in the stock.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onRemoveProductFromCart(ProductData mProduct, TextView proQuantity) {
        if (orderedQuantity > 0) {
            String productUnitPrice = mProduct.getProductPrice().replace('$', ' ');
            amount -= Double.parseDouble(productUnitPrice);
            orderedQuantity--;
            double amountForThisGood = orderedQuantity * Double.parseDouble(productUnitPrice);
            proQuantity.setText(String.format(Locale.US, "%d", orderedQuantity));
            Snackbar.make(proQuantity, "Removed " + mProduct.getProductTitle() + " from cart!" +
                    "\n" + "Order remained: " + orderedQuantity
                    + "     " + "Amount: " + Double.toString(amountForThisGood), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void onProfileImageClicked(int result, Dialog dialog) {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            backPress = 0;
        } else if (backPress < 1) {
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
            case R.id.setting: {
                Toast.makeText(this, "Navigation menu clicked Setting", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.sale_product: {
                startActivity(new Intent(MainActivity.this, AddProductToStoreActivity.class));
                break;
            }
            case R.id.my_store: {
                break;
            }
            case R.id.sign_out: {
                displayLoadingProgress.displayLoadingProgress("Logging out...");
                mainMvpPresenter.onSignOut(this, displayLoadingProgress.getDialog());
                break;
            }
            case R.id.language_english: {
                setLocale("en");
                menuItem.setChecked(true);
                break;
            }
            case R.id.language_khmer: {
                setLocale("kh");
                menuItem.setChecked(true);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.backgroundColor));
            window.setNavigationBarColor(activity.getResources().getColor(R.color.fui_transparent));
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    //set language to activity
    public void setLocale(String localeName) {
        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.putExtra(currentLang, localeName);
        startActivity(refresh);
    }
}
