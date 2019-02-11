package com.example.dell.myapplication.ui.sign_up;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, SignUpMvpView {
    private ImageView imgUserProfile;
    private TextInputLayout etName;
    private TextInputLayout etGender;
    private TextInputLayout etTel;
    private TextInputLayout etEmail;
    private TextInputLayout etOtherContact;
    private Button btnSignUp;

    private static final int REQUEST_EXTERNAL_STORAGE_ACCESS = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;

    private SignUpMvpPresenter signUpMvpPresenter = new SignUpPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imgUserProfile = findViewById(R.id.imgSignUpProfile);
        etName = findViewById(R.id.etName);
        etGender = findViewById(R.id.etGender);
        etTel = findViewById(R.id.etTel);
        etEmail = findViewById(R.id.etEmail);
        etOtherContact = findViewById(R.id.etOtherContact);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);
        imgUserProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        switch (buttonID) {
            case R.id.imgSignUpProfile: {
                signUpMvpPresenter.chooseImage(SignUpActivity.this);
                break;
            }
            case R.id.btnSignUp: {
//                String profileUri = imgUserProfile.
                String name = etName.getEditText().getText().toString();
                String gender = etGender.getEditText().getText().toString();
                String tel = etTel.getEditText().getText().toString();
                String email = etEmail.getEditText().getText().toString();
                String otherContact = etOtherContact.getEditText().getText().toString();
                break;
            }
        }
    }

    @Override
    public void chooseImageFromGallery() {
        if (requestGalleryAccessPermission()){
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY);
    }

    private boolean requestGalleryAccessPermission() {
        if (ActivityCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_ACCESS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE_ACCESS){
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            Glide.with(SignUpActivity.this).load(imageUri).into(imgUserProfile);
        }
    }
}
