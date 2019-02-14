package com.example.dell.myapplication.ui.sign_up;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Patterns;

import com.bumptech.glide.load.engine.Resource;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.DialogChooseImageForSignUp;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.model.UserInfo;
import com.example.dell.myapplication.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class SignUpPresenter implements SignUpMvpPresenter {
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private SignUpMvpView loadDefaultImage;

    public SignUpPresenter(SignUpMvpView loadDefaultImage){
        this.loadDefaultImage = loadDefaultImage;
    }
    @Override
    public void chooseImage(Context context, SignUpMvpView signUpMvpView) {
        new DialogChooseImageForSignUp(context, signUpMvpView).displayDialog();
    }

    @Override
    public String onGetDefaultProfileUrl() {
        FirebaseStorage.getInstance()
                .getReference("profile")
                .child("ic_default_user_profile_picture.png")
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            loadDefaultImage.onDefaultProfileLoadingSuccess(Objects.requireNonNull(task.getResult()).toString());
                        } else {
                            Log.e("Error:", "Failed to load default image url");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Image: ", e.toString());
                    }
                });
        return "";
    }

    @Override
    public void onSignUpWithoutProfilePicture(final Context context, final UserInfo userInfo, String password,
                                              final String imageUri, final DialogDisplayLoadingProgress displayLoadingProgress) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(userInfo.getuEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                final String userID = user.getUid();
                                saveUserInfoToDatabase(userInfo, displayLoadingProgress, context, userID,
                                        imageUri);
                            }
                        } else {
                            Snackbar.make(((Activity)context).findViewById(R.id.btnSkip),
                                    "Action failed...", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onSignUpUser(final Context context, final UserInfo userInfo, String password,
                             final DialogDisplayLoadingProgress displayLoadingProgress) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(userInfo.getuEmail(), password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                final String userID = user.getUid();
                                mStorageRef = FirebaseStorage.getInstance()
                                        .getReference("profile")
                                        .child(userID);
                                StorageTask storageTask = mStorageRef.putFile(Uri.parse(userInfo.getProfileUrl()))
                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                FirebaseStorage.getInstance()
                                                        .getReference("profile")
                                                        .child(userID)
                                                        .getDownloadUrl()
                                                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> task) {
                                                                if (task.isSuccessful()) {
                                                                    saveUserInfoToDatabase(userInfo, displayLoadingProgress,
                                                                            context, userID, Objects.requireNonNull(task.getResult()).toString());
                                                                }
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(((Activity) context).findViewById(R.id.btnSignUp),
                                                        e.toString(), Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Snackbar.make(((Activity) context).findViewById(R.id.btnSignUp),
                                    task.toString(), Snackbar.LENGTH_SHORT).show();
                            displayLoadingProgress.getDialog().dismiss();
                        }
                    }
                });
    }

    private void saveUserInfoToDatabase(UserInfo userInfo, final DialogDisplayLoadingProgress displayLoadingProgress,
                                        final Context context, String userID, String profileUrl) {
        userInfo.setProfileUrl(profileUrl);
        if (userInfo.getuOther().isEmpty()) {
            userInfo.setuOther("N/A");
        }
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = mFirebaseDatabase.getReference().child("user");

        mDatabaseRef.child(userID)
                .setValue(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        displayLoadingProgress.getDialog().dismiss();
                        context.startActivity(new Intent(context, MainActivity.class));
                        ((Activity)context).finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        displayLoadingProgress.getDialog().dismiss();
                        Snackbar.make(((Activity) context).findViewById(R.id.btnSignUp),
                                e.toString(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
