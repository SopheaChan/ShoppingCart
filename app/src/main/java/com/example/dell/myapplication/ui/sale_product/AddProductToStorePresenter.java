package com.example.dell.myapplication.ui.sale_product;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.model.CompanyInfo;
import com.example.dell.myapplication.model.ProductData;
import com.example.dell.myapplication.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class AddProductToStorePresenter implements AddProductToStoreMvpPresenter {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceUser;
    private DatabaseReference databaseReferenceProduct;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private EditText etSaler;
    private EditText etTel;
    private EditText etEmail;
    private AddProductToStoreMvpView addProductToStoreMvpView;
    private String userID;

    public AddProductToStorePresenter(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("user").child(userID);
        databaseReferenceProduct = firebaseDatabase.getReference("product");
        storageReference = FirebaseStorage.getInstance().getReference("product");
    }

    public AddProductToStorePresenter(AddProductToStoreMvpView addProductToStoreMvpView){
        this.addProductToStoreMvpView = addProductToStoreMvpView;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("user").child(userID);
        databaseReferenceProduct = firebaseDatabase.getReference("product");
        storageReference = FirebaseStorage.getInstance().getReference("product");
    }

    public AddProductToStorePresenter(EditText etSaler, EditText etTel, EditText etEmail){
        this.etSaler = etSaler;
        this.etTel = etTel;
        this.etEmail = etEmail;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("user").child(userID);
        databaseReferenceProduct = firebaseDatabase.getReference("product");
        storageReference = FirebaseStorage.getInstance().getReference("product");
    }

    @Override
    public void setButtonNextListener(final String comName, final String comTel, final String comEmail,
                                      EditText etComName, EditText etComTel, EditText etComEmail,
                                      final Context context, final FragmentManager fragmentManager) {
        if (comName.isEmpty() || comTel.isEmpty() ||
                (!Patterns.EMAIL_ADDRESS.matcher(comEmail).matches())) {
            if (comName.isEmpty()) {
                etComName.setError("required");
            }
            if (comTel.isEmpty()) {
                etComTel.setError("required");
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(comEmail).matches()) {
                etComEmail.setError("invalid email address");
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("comName", comName);
            bundle.putString("comTel", comTel);
            bundle.putString("comEmail", comEmail);
            Fragment fragment = new AddProductInfoFragment();
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onButtonSubmitListener(final Context context, final ProductData productData,
                                       final CompanyInfo companyInfo, final DialogDisplayLoadingProgress displayLoadingProgress) {
        Uri imageUri = Uri.parse(productData.getProductImage());
        String companyName = companyInfo.getCompanyName();
        String productTitle = productData.getProductTitle();

        storageReference.child(companyName).child(productTitle);
        StorageTask storageTask = storageReference.putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            storageReference.child(companyInfo.getCompanyName()).child(productData.getProductTitle()).getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                productData.setProductImage(task.getResult().toString());
                                                final String productID = databaseReferenceProduct.push().getKey();
                                                productData.setProductID(productID);
                                                        databaseReferenceProduct.child(productID)
                                                        .setValue(productData)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                                                            .getReference("product_views")
                                                                            .child(productID);
                                                                    databaseReference.setValue("1")
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()){
                                                                                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                                                                                                .getReference("my_product")
                                                                                                .child(userID);
                                                                                        String id = databaseReference.push().getKey();
                                                                                        databaseReference1.child(id)
                                                                                                .setValue(productID)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()){
                                                                                                            displayLoadingProgress.getDialog().dismiss();
                                                                                                            addProductToStoreMvpView.clearDataFromView();
                                                                                                            Toast.makeText(context, "Successfully saved data...", Toast.LENGTH_LONG).show();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });
                                                                } else {
                                                                    displayLoadingProgress.getDialog().dismiss();
                                                                }
                                                            }
                                                        });
                                            } else {
                                                displayLoadingProgress.getDialog().dismiss();
                                                Toast.makeText(context, "There is a problem while saving data to database..."
                                                        , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            displayLoadingProgress.getDialog().dismiss();
                            Toast.makeText(context, "Process failed...", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        displayLoadingProgress.getDialog().dismiss();
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void loadUserData() {
        databaseReferenceUser.addListenerForSingleValueEvent(valueEventListener);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
            if (userInfo != null){
                etSaler.setText(userInfo.getuName());
                etTel.setText(userInfo.getuTel());
                etEmail.setText(userInfo.getuEmail());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
