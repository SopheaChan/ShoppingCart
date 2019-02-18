package com.example.dell.myapplication.ui.sale_product;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.DialogMenu;
import com.example.dell.myapplication.listener.Interactor;
import com.example.dell.myapplication.listener.OnDialogClickListener;
import com.example.dell.myapplication.model.CompanyInfo;
import com.example.dell.myapplication.model.Product;
import com.example.dell.myapplication.model.ProductData;
import com.example.dell.myapplication.ui.main.MainActivity;
import com.example.dell.myapplication.ui.main.MainMvpPresenter;
import com.example.dell.myapplication.ui.main.MainPresenter;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

public class ProductInfoFragment extends Fragment implements View.OnClickListener {

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
    private AddProductToStoreMvpPresenter addProductToStoreMvpPresenter =
            new AddProductToStorePresenter();

    private String comName = "";
    private String comTel = "";
    private String comEmail = "";

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

        Bundle bundle = this.getArguments();
        if (bundle != null){
            comName = bundle.getString("comName");
            comTel = bundle.getString("comTel");
            comEmail = bundle.getString("comEmail");
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
                CompanyInfo companyInfo = new CompanyInfo(comName, comTel, comEmail);
                ProductData productData = new ProductData(path, proTitle, proPrice,
                        proQuantity, proDescription);
                addProductToStoreMvpPresenter.onButtonSubmitListener(context, productData, companyInfo);
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
}
