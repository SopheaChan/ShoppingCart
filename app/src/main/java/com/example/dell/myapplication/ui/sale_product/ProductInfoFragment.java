package com.example.dell.myapplication.ui.sale_product;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.custom.DialogMenu;
import com.example.dell.myapplication.listener.OnDialogClickListener;
import com.example.dell.myapplication.model.CompanyInfo;
import com.example.dell.myapplication.model.ProductData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ProductInfoFragment extends Fragment implements View.OnClickListener
, AddProductToStoreMvpView{

    private ImageView imgProductImage;
    private EditText etProductTitle;
    private EditText etProductPrice;
    private EditText etProductQuantity;
    private EditText etProductDescription;
    private Button btnBack;
    private Button btnSubmit;
    private Context context;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 0;
    private static final int CAPTURE_IMAGE = 1;
    private String path = "";

    private String salerName = "";
    private String salerTel = "";
    private String salerEmail = "";

    private AddProductToStoreMvpPresenter addProductToStoreMvpPresenter;
    private DialogDisplayLoadingProgress dialogDisplayLoadingProgress;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String userID = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);
        btnBack = view.findViewById(R.id.btnBack);
        btnSubmit = view.findViewById(R.id.btnSubmitData);
        imgProductImage = view.findViewById(R.id.imgProductImage1);
        etProductTitle = view.findViewById(R.id.etProductTitle);
        etProductPrice = view.findViewById(R.id.etProductPrice);
        etProductQuantity = view.findViewById(R.id.etProductQuantity);
        etProductDescription = view.findViewById(R.id.etProductDescription);
        btnBack.setOnClickListener(this);
        imgProductImage.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        dialogDisplayLoadingProgress = new DialogDisplayLoadingProgress(context);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser.getUid();
        addProductToStoreMvpPresenter =
                new AddProductToStorePresenter(this);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            salerName = bundle.getString("comName");
            salerTel = bundle.getString("comTel");
            salerEmail = bundle.getString("comEmail");
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack: {
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
            }
            case R.id.imgProductImage1: {
                new DialogMenu(getContext(), onDialogClick);
                break;
            }
            case R.id.btnSubmitData: {
                String proTitle = etProductTitle.getText().toString().trim();
                String proPrice = etProductPrice.getText().toString().trim();
                String proQuantity = etProductQuantity.getText().toString().trim();
                String proDescription = etProductDescription.getText().toString().trim();
                if (proTitle.isEmpty() || proPrice.isEmpty() || proQuantity.isEmpty() || path.isEmpty()){
                    if (proTitle.isEmpty()){
                        etProductTitle.setError("required");
                    }
                    if (proPrice.isEmpty()){
                        etProductPrice.setError("required");
                    }
                    if (proQuantity.isEmpty()){
                        etProductQuantity.setError("required");
                    }
                    if (path.isEmpty()){
                        Snackbar.make(btnSubmit, "Please choose an image for your product.", Snackbar.LENGTH_SHORT).show();
                    }
                    return;
                } else {
                    dialogDisplayLoadingProgress.displayLoadingProgress("Saving data to database...");
                    CompanyInfo companyInfo = new CompanyInfo(salerName, salerTel, salerEmail);
                    ProductData productData = new ProductData(path, proTitle, proPrice,
                            proQuantity, proDescription, userID, "", salerName, salerTel, salerEmail);
                    addProductToStoreMvpPresenter.onButtonSubmitListener(context, productData, companyInfo,
                            dialogDisplayLoadingProgress);
                }
                break;
            }
        }
    }

    private OnDialogClickListener onDialogClick = new OnDialogClickListener() {
        @Override
        public void onItemClickListener(int result, View view, Dialog dialog) {
            switch (result) {
                case 1: {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY);
                    break;
                }
                case 2: {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
                        break;
                    }
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            Glide.with(context).load(imageUri).into(imgProductImage);
            path = imageUri.toString();
        } else if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            path = MediaStore.Images.Media.insertImage(context.getContentResolver(), imageBitmap, "profile_picture", null);
            imgProductImage.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void clearDataFromView() {
        etProductTitle.setText("");
        etProductPrice.setText("");
        etProductQuantity.setText("");
        etProductDescription.setText("");
        imgProductImage.setImageResource(R.drawable.ic_image_black_24dp);
        etProductTitle.requestFocus();
    }
}
