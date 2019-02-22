package com.example.dell.myapplication.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.adapter.MyAdapter;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.custom.DisplayProfileInfo;
import com.example.dell.myapplication.custom.ProfileImageView;
import com.example.dell.myapplication.listener.ProfileImageViewOnClickListener;
import com.example.dell.myapplication.model.ProductData;
import com.example.dell.myapplication.model.UserInfo;
import com.example.dell.myapplication.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPresenter implements MainMvpPresenter {
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRefProduct;
    private String userID;
    private Context context;
    private ProfileImageView profileImageView;
    private MainMvpView mainMvpView;
    private DialogDisplayLoadingProgress dialogDisplayLoadingProgress;

    private static final int REQUEST_GALLERY_ACCESS = 0;
    private int newProductViews;

    public MainPresenter(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        userID = mFirebaseUser.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("user").child(userID);
        mDatabaseRefProduct = FirebaseDatabase.getInstance().getReference("product");
    }

    public MainPresenter(Context context, MainMvpView mainMvpView,
                         DialogDisplayLoadingProgress loadingProgress) {
        this.context = context;
        this.mainMvpView = mainMvpView;
        this.dialogDisplayLoadingProgress = loadingProgress;

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        userID = mFirebaseUser.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("user").child(userID);
        mDatabaseRefProduct = FirebaseDatabase.getInstance().getReference("product");
    }

    @Override
    public void onSignOut(final Activity activity, final Dialog dialog) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                context.startActivity(new Intent(context, LoginActivity.class));
                activity.finish();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onViewProfileClicked() {
        mDatabaseRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
            if (userInfo != null) {
                DisplayProfileInfo displayProfileInfo =
                        new DisplayProfileInfo(context, mainMvpView, dialogDisplayLoadingProgress, userInfo);
                displayProfileInfo.viewUserInfo();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public void onUpdateUserInfo(final UserInfo userInfo, final DialogDisplayLoadingProgress displayLoadingProgress,
                                 final EditText etName, final EditText etGender,
                                 final EditText etTel, final EditText etOther, final Button btnEditProfile) {
        try {
            mDatabaseRef.child("uEmail").setValue(userInfo.getuEmail());
            mDatabaseRef.child("uName").setValue(userInfo.getuName());
            mDatabaseRef.child("uTel").setValue(userInfo.getuTel());
            mDatabaseRef.child("uGender").setValue(userInfo.getuGender());
            mDatabaseRef.child("uOther").setValue(userInfo.getuOther());
            mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    etName.setEnabled(false);
                    etGender.setEnabled(false);
                    etTel.setEnabled(false);
                    etOther.setEnabled(false);
                    btnEditProfile.setText("Edit");
                    displayLoadingProgress.getDialog().dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    displayLoadingProgress.getDialog().dismiss();
                    Toast.makeText(context, "Error updating user data...", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e("OnUpdatUserInfo: ", e.toString());
        }
    }

    @Override
    public void onLoadUserInfo(final Activity activity, final TextView tvUserName,
                               final CircleImageView imgUserProfile) {

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                tvUserName.setText(userInfo.getuName());
                Glide.with(context).load(Uri.parse(userInfo.getProfileUrl())).into(imgUserProfile);
                profileImageView = new ProfileImageView(context, Uri.parse(userInfo.getProfileUrl()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCheckOutClickedListener(double totalAmount, TextView tvTotalPrice, Button btnSubmit, Button btnCheckout) {
        String dollarSymbol = "$";
        //Split the number after the dot of total amount, only two characters will be taken
        tvTotalPrice.setText(String.format(Locale.US, dollarSymbol + "%.2f", totalAmount));
        btnCheckout.setVisibility(View.INVISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onOpenGallery(Activity activity, Dialog dialog) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_GALLERY_ACCESS);
        dialog.dismiss();
    }

    @Override
    public void onViewProfileImage() {
        profileImageView.onDisplayProfilePicture(new ProfileImageViewOnClickListener() {
            @Override
            public void onClickListener(String buttonTitle, Dialog dialog) {
                if (buttonTitle.equalsIgnoreCase("done")) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onUploadProfile(final Context context, final Uri imageUri, final DialogDisplayLoadingProgress displayLoadingProgress) {

        final ProfileImageView profileImageView = new ProfileImageView(context, imageUri, "Upload");
        profileImageView.onDisplayProfilePicture(new ProfileImageViewOnClickListener() {
            @Override
            public void onClickListener(final String buttonTitle, final Dialog dialog) {
                if (buttonTitle.equalsIgnoreCase("upload")) {
                    displayLoadingProgress.displayLoadingProgress("Uploading...");
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference("profile")
                            .child(userID);
                    StorageTask storageTask = storageReference.putFile(imageUri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseStorage.getInstance()
                                                .getReference("profile")
                                                .child(userID)
                                                .getDownloadUrl()
                                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Uri> task) {
                                                        mDatabaseRef.child("profileUrl").setValue(Objects.requireNonNull(task.getResult()).toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            displayLoadingProgress.getDialog().dismiss();
                                                                            profileImageView.setButtonTitle("done");
                                                                            Toast.makeText(context, "Upload success...", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            displayLoadingProgress.getDialog().dismiss();
                                                                            Toast.makeText(context, "Upload failed...", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                }
                            });
                } else if (buttonTitle.equalsIgnoreCase("done")) {
                    profileImageView.getProfileImageView().dismiss();
                }
            }
        });
    }

    @Override
    public void setDataToList(final MyAdapter myAdapter, final List<ProductData> mProductList,
                              final DialogDisplayLoadingProgress displayLoadingProgress) {
        mDatabaseRefProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProductList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ProductData productData = dataSnapshot1.getValue(ProductData.class);
                    mProductList.add(productData);
                    displayLoadingProgress.getDialog().dismiss();
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                displayLoadingProgress.getDialog().dismiss();
            }
        });
    }

    @Override
    public String addProductViewsRecord(final String productID) {

        mDatabaseRef = FirebaseDatabase.getInstance()
                .getReference("product_views")
                .child(productID);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String productViews = dataSnapshot.getValue(String.class);
                if (productViews != null) {
                    newProductViews = Integer.parseInt(productViews) + 1;
                    FirebaseDatabase.getInstance().getReference("product_views")
                            .child(productID)
                            .setValue(Integer.toString(newProductViews));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return Integer.toString(newProductViews);
    }

    @Override
    public void onButtonConcelClickedListener(Button btnSubmit, Button btnCheckout,
                                              TextView tvTotalPrice) {
        btnSubmit.setVisibility(View.INVISIBLE);
        btnCheckout.setVisibility(View.VISIBLE);
        tvTotalPrice.setText("$0.00");
    }
}
