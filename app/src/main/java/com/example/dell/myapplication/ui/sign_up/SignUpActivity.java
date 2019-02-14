package com.example.dell.myapplication.ui.sign_up;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.DialogAlertMessage;
import com.example.dell.myapplication.custom.DialogChooseImageForSignUp;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.text.Format;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, SignUpMvpView {
    private ImageView imgUserProfile;
    private EditText etName;
    private EditText etGender;
    private EditText etTel;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etOtherContact;
    private Button btnSignUp;

    private Uri imageUri;
    private UserInfo userInfo;

    private static final int REQUEST_EXTERNAL_STORAGE_ACCESS = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;

    private SignUpMvpPresenter signUpMvpPresenter = new SignUpPresenter(this);
    private DialogDisplayLoadingProgress displayLoadingProgress;
    DialogAlertMessage dialogAlertMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imgUserProfile = findViewById(R.id.imgSignUpProfile);
        etName = findViewById(R.id.etName);
        etGender = findViewById(R.id.etGender);
        etTel = findViewById(R.id.etTel);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etOtherContact = findViewById(R.id.etOtherContact);
        btnSignUp = findViewById(R.id.btnSignUp);
        displayLoadingProgress = new DialogDisplayLoadingProgress(this);
        dialogAlertMessage = new DialogAlertMessage(this, onSkipListener);

        btnSignUp.setOnClickListener(this);
        imgUserProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        switch (buttonID) {
            case R.id.imgSignUpProfile: {
                signUpMvpPresenter.chooseImage(SignUpActivity.this, this);
                break;
            }
            case R.id.btnSignUp: {
                Uri imgProfilePicture = imageUri;
                String name = etName.getText().toString();
                String gender = etGender.getText().toString();
                String tel = etTel.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String otherContact = etOtherContact.getText().toString();
                onSignUpValidation(imgProfilePicture, name, gender, tel, email, password, otherContact);
                break;
            }
        }
    }

    @Override
    public void chooseImageFromGallery() {
        if (requestGalleryAccessPermission()) {
            openGallery();
        }
    }

    @Override
    public void onDefaultProfileLoadingSuccess(String imageUri) {
        String name = etName.getText().toString();
        String gender = etGender.getText().toString();
        String tel = etTel.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String otherContact = etOtherContact.getText().toString();
        UserInfo userInfo = new UserInfo(imageUri, name, gender, tel, email, otherContact);
        signUpMvpPresenter.onSignUpWithoutProfilePicture(SignUpActivity.this, userInfo, password, imageUri, displayLoadingProgress);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY);
    }

    private boolean requestGalleryAccessPermission() {
        if (ActivityCompat.checkSelfPermission(SignUpActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_ACCESS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE_ACCESS) {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            imageUri = data.getData();
            Glide.with(SignUpActivity.this).load(imageUri).into(imgUserProfile);
        }
    }

    //Validate data before process sign up progress
    private void onSignUpValidation(final Uri profileImageUri, final String name, final String gender,
                                    final String tel, final String email, final String password, String otherContact) {
        if (name.isEmpty() || gender.isEmpty() || tel.isEmpty() || email.isEmpty() || (!Patterns.EMAIL_ADDRESS.matcher(email).matches())) {

            if (name.isEmpty()) {
                etName.setError("Name cannot be blank!");
            }
            if (gender.isEmpty()) {
                etGender.setError("Gender cannot be blank!");
            }
            if (tel.isEmpty()) {
                etTel.setError("Phone number cannot be blank!");
            }
            if (password.length() < 6) {
                etPassword.setError("Password cannot be less than 6 characters!");
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Invalid email address!");
            }
            return;
        } else {
            if (profileImageUri == null) {
                dialogAlertMessage.onDisplayAlertMessage("Alert",
                        "You have not choose your profile picture yet.\n" +
                                "Click on the photo area to browse your profile picture.\n" +
                                "Click skip if you want to continue sign up without your profile picture.");
            } else {
                displayLoadingProgress.displayLoadingProgress("Registering...");
                userInfo = new UserInfo(profileImageUri.toString(), name, gender, tel, email, otherContact);
                signUpMvpPresenter.onSignUpUser(SignUpActivity.this, userInfo, password, displayLoadingProgress);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private DialogAlertMessage.ButtonSkipListener onSkipListener = new DialogAlertMessage.ButtonSkipListener() {
        @Override
        public void onSkipListener(Dialog dialog) {
            dialog.dismiss();
            displayLoadingProgress.displayLoadingProgress("Signing up...");
            signUpMvpPresenter.onGetDefaultProfileUrl();
        }
    };
}
