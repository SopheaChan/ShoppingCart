package com.example.dell.myapplication.ui.sale_product;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.dell.myapplication.adapter.ViewPagerAdapter;

public interface AddProductToStoreMvpPresenter {
    void addFragment(ViewPager viewPager, ViewPagerAdapter viewPagerAdapter, TabLayout tabLayout);
}
