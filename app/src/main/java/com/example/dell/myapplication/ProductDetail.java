package com.example.dell.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.example.dell.myapplication.adapter.ItemDetailAdapter;
import com.example.dell.myapplication.model.CompanyInfo;
import com.example.dell.myapplication.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDetail extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemDetailAdapter mAdapter;
    private List<Product> listProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        recyclerView = findViewById(R.id.recycler_product_info);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        listProduct = new ArrayList<>();
        mAdapter = new ItemDetailAdapter(this, listProduct);
        recyclerView.setAdapter(mAdapter);

        setDataToList();
    }

    private void setDataToList() {
        CompanyInfo companyInfo1 = new CompanyInfo("Dream Farm", "+855 16 552 693", "dreamfarm@gmail.com");
        Product product = new Product(R.drawable.black_burger, "Apple Juice", 2.50, 1, companyInfo1);
        CompanyInfo companyInfo2 = new CompanyInfo("SR Healthy Farm", "+855 16 622 666", "srhealthyfarm@gmail.com");
        Product product1 = new Product(R.drawable.black_burger, "Grape Juice", 3.50, 3, companyInfo2);

        listProduct.add(product);
        listProduct.add(product1);
    }
}
