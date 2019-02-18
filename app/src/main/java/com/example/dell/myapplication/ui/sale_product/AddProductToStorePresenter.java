package com.example.dell.myapplication.ui.sale_product;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.custom.DialogDisplayLoadingProgress;
import com.example.dell.myapplication.model.CompanyInfo;
import com.example.dell.myapplication.model.Product;
import com.example.dell.myapplication.model.ProductData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AddProductToStorePresenter implements AddProductToStoreMvpPresenter {
    private CompanyInfo companyInfo;
    private Product product;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DialogDisplayLoadingProgress displayLoadingProgress;
    private StorageReference storageReference;

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
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            String userID = firebaseUser.getUid();
            companyInfo = new CompanyInfo(comName, comTel, comEmail);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("company").child(userID).child(comName).setValue(companyInfo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Bundle bundle = new Bundle();
                                bundle.putString("comName", comName);
                                bundle.putString("comTel", comTel);
                                bundle.putString("comEmail", comEmail);
                                Fragment fragment = new ProductInfoFragment();
                                fragment.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_frame, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            } else {
                                Toast.makeText(context, "Fail to save data...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onButtonSubmitListener(final Context context, final ProductData productData, final CompanyInfo companyInfo) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("product");
        storageReference.child(companyInfo.getCompanyName()).child(productData.getProductTitle());
        StorageTask storageTask = storageReference.putFile(Uri.parse(productData.getProductImage()))
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            FirebaseStorage.getInstance().getReference("product")
                                    .child(companyInfo.getCompanyName()).child(productData.getProductTitle()).getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                                final String userID = firebaseUser.getUid();
                                                final String proID = databaseReference.push().getKey();
                                                productData.setProductImage(task.getResult().toString());
                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                                        .getReference("company");
                                                databaseReference.child(userID)
                                                        .child(companyInfo.getCompanyName())
                                                .child("product").child(proID);
                                                databaseReference.setValue(productData)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(context, "Successfully saved data...", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                            }
                                        }
                                    });

                        }
                    }
                });
    }

    @Override
    public void uploadProductPicture(Uri imageUri, final String comName, final String proName) {
        storageReference = FirebaseStorage.getInstance().getReference("product")
                .child(comName).child(proName);
        StorageTask storageTask = storageReference.putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            FirebaseStorage.getInstance().getReference("product")
                                    .child(comName).child(proName).getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });

                        }
                    }
                });
    }
}
