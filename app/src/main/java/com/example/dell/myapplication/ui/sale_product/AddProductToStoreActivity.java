package com.example.dell.myapplication.ui.sale_product;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.adapter.ViewPagerAdapter;

public class AddProductToStoreActivity extends AppCompatActivity{
    private static final int REQUEST_GALLERY_ACCESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_store);

        addFragment();
    }

    public void addFragment(){
        Fragment fragment = new AddCompanyInfoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_frame, fragment);
        fragmentTransaction.commit();
    }
}
