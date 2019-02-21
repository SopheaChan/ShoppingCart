package com.example.dell.myapplication.ui.product;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.model.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Permission;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailPresenter implements ProductDetailMvpPresenter {
    private static final int REQUEST_PHONE_CALL_PERMISSION = 1;

    @Override
    public void onAddToCart(int proQuantity, double proPrice, TextView tvOrderedProduct,
                            TextView tvTotalAmount, EditText etOrderedQuantity) {
        String orderQuantity = etOrderedQuantity.getText().toString();
        if (!orderQuantity.isEmpty()) {
            proQuantity += Integer.parseInt(etOrderedQuantity.getText().toString());
            double mAmount = proQuantity * proPrice;

            tvOrderedProduct.setText(String.format(Locale.US, "%d", proQuantity));
            tvTotalAmount.setText(String.format(Locale.US, "%.2f", mAmount));
            etOrderedQuantity.setText("");
        }
    }


    @Override
    public void onPhoneNumberClickedListener(Activity activity) {
        Toast.makeText(activity.getBaseContext(), "Clicked...", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(activity.getBaseContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_PHONE_CALL_PERMISSION);
        }
    }

    @Override
    public void getProductViews(String productID, final TextView productViewsCounter) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("product_views")
                .child(productID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String productViews = dataSnapshot.getValue(String.class);
                productViewsCounter.setText(" "+productViews+" views");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getSalerProfile(String userID, final Context context, final CircleImageView imgSalerProfile) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("user")
                .child(userID)
                .child("profileUrl");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String salerProfileUrl = dataSnapshot.getValue(String.class);
                        Glide.with(context).load(Uri.parse(salerProfileUrl)).into(imgSalerProfile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
