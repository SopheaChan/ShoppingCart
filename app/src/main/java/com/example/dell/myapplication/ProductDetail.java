package com.example.dell.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dell.myapplication.adapter.ItemDetailAdapter;
import com.example.dell.myapplication.model.CompanyInfo;
import com.example.dell.myapplication.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDetail extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemDetailAdapter mAdapter;
    private List<Product> listProduct;
    private List<CompanyInfo> listCompanyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        recyclerView = findViewById(R.id.recycler_product_info);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        listProduct = new ArrayList<>();
        listCompanyInfo = new ArrayList<>();
        mAdapter = new ItemDetailAdapter(this, listProduct, listCompanyInfo);
        recyclerView.setAdapter(mAdapter);

        setDataToList();
    }

    private void setDataToList() {
        Product product = new Product(R.drawable.black_burger, "Apple Juice", 2.50, 1);
        Product product1 = new Product(R.drawable.black_burger, "Grape Juice", 3.50, 3);

        CompanyInfo companyInfo = new CompanyInfo("Apple World", "+001 159 365", "appleworld@gmail.com");
        CompanyInfo companyInfo1 = new CompanyInfo("Grape World", "+002 260 576", "appleworld@gmail.com");

        listProduct.add(product);
        listProduct.add(product1);
        listCompanyInfo.add(companyInfo);
        listCompanyInfo.add(companyInfo1);
    }
}
