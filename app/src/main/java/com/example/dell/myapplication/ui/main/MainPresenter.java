package com.example.dell.myapplication.ui.main;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.custom.DisplayProfileInfo;
import com.example.dell.myapplication.custom.ProfileImageView;
import com.example.dell.myapplication.listener.ProfileImageViewOnClickListener;
import com.example.dell.myapplication.model.Product;
import com.example.dell.myapplication.model.UserInfo;
import com.example.dell.myapplication.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPresenter implements MainMvpPresenter{
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseRef;
    private Context context;
    private ProfileImageView profileImageView;

    private static final int REQUEST_GALLERY_ACCESS = 0;

    public MainPresenter(Context context){
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        String userID = mFirebaseUser.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("user").child(userID);
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
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                if (userInfo != null) {
                    DisplayProfileInfo displayProfileInfo = new DisplayProfileInfo(context, userInfo);
                    displayProfileInfo.viewUserInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    public void onCheckOutClickedListener(List<Product> productList, TextView tvTotalPrice) {
        double totalAmount = 0.0;
        String dollarSymbol = "$";
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            totalAmount += product.getProPrice() * product.getProQuantity();
        }
        //Split the number after the dot of total amount, only two characters will be taken
        tvTotalPrice.setText(String.format(Locale.US, dollarSymbol + "%.2f", totalAmount));
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
                if (buttonTitle.equalsIgnoreCase("done")){
                    dialog.dismiss();
                }
            }
        });
    }
}
