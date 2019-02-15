package com.example.dell.myapplication.ui.sale_product;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.adapter.ViewPagerAdapter;

public class AddProductToStoreActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private AddProductToStoreMvpPresenter addProductToStoreMvpPresenter =
            new AddProductToStorePresenter();
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_store);

//        viewPager = new ViewPager(this);
//        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        viewPagerAdapter.addFragment(new ProductInfoFragment());
//        viewPagerAdapter.addFragment(new CompanyInfoFragment());
//
//        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);
//        setFragment();
    }

//    private void setFragment() {
//        addProductToStoreMvpPresenter.addFragment(viewPager, viewPagerAdapter, tabLayout);
//    }
}
