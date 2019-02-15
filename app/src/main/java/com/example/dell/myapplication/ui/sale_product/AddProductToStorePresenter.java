package com.example.dell.myapplication.ui.sale_product;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.dell.myapplication.adapter.ViewPagerAdapter;

public class AddProductToStorePresenter implements AddProductToStoreMvpPresenter {
    @Override
    public void addFragment(ViewPager viewPager, ViewPagerAdapter viewPagerAdapter, TabLayout tabLayout) {
        viewPagerAdapter.addFragment(new CompanyInfoFragment());
        viewPagerAdapter.addFragment(new ProductInfoFragment());

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
