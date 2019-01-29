package com.example.dell.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dell.myapplication.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    private List<Product> mProductList = new ArrayList<>();


    private NavigationView navigationView;
    private android.support.v7.widget.Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Button btnCheckout;
    private TextView tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        btnCheckout = findViewById(R.id.button_check_out);
        tvTotalPrice = findViewById(R.id.total_price);

        recyclerView = findViewById(R.id.my_recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new MyAdapter(this, mProductList, new MyAdapter.AddProductToCartListener() {
            @Override
            public void onClick(Product mProduct, TextView proQuantity) {
                int productQuantity = mProduct.getProQuantity();
                if (productQuantity < 20) {
                    productQuantity++;
                    mProduct.setProQuantity(productQuantity);
                    proQuantity.setText(Integer.toString(productQuantity));
                    Toast.makeText(getApplicationContext(), "Added " + mProduct.getProName()
                            + " to cart!" + "\n" + "Current order: " + mProduct.getProQuantity()
                            + "\n" + "Amount: " + Double.toString(Double.parseDouble(mProduct.getProPrice()) * productQuantity), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You cannot make an order for 20 burgers a time.", Toast.LENGTH_LONG).show();
                }
            }
        }, new MyAdapter.DeleteProductFromCartListener() {
            @Override
            public void onClick(Product mProduct, TextView proQuantity) {
                int productQuantity = mProduct.getProQuantity();
                if (productQuantity > 0) {
                    productQuantity--;
                    mProduct.setProQuantity(productQuantity);
                    proQuantity.setText(Integer.toString(productQuantity));
                    Toast.makeText(getApplicationContext(), "Remove " + mProduct.getProName() + " from cart!" +
                            "\n" + "Order remained: " + mProduct.getProQuantity()
                            + "\n" + "Amount: " + Double.toString(Double.parseDouble(mProduct.getProPrice()) * productQuantity), Toast.LENGTH_LONG).show();
                } else {
                    return;
                }
            }
        });

        recyclerView.setAdapter(myAdapter);

        setDataToList();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckout(mProductList);
            }
        });
    }

    private void onCheckout(List<Product> proList) {
        Double totalAmount = 0.0;
        for (int i = 0; i < proList.size(); i++) {
            Product product = proList.get(i);
            totalAmount += Double.parseDouble(product.getProPrice()) * product.getProQuantity();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String dx = df.format(totalAmount);
        totalAmount = Double.valueOf(dx);
        tvTotalPrice.setText("$ " + Double.toString(totalAmount));
    }

    private void setDataToList() {
        Product product1 = new Product(R.drawable.black_burger, "Black Pork Burger", "15.4", 1);
        Product product2 = new Product(R.drawable.black_burger, "Black Pork Burger", "15.4", 2);
        Product product3 = new Product(R.drawable.black_burger, "Black Pork Burger", "15.4", 3);
        Product product4 = new Product(R.drawable.black_burger, "Black Pork Burger", "15.4", 4);
        Product product5 = new Product(R.drawable.black_burger, "Black Pork Burger", "15.4", 5);
        Product product6 = new Product(R.drawable.black_burger, "Black Pork Burger", "15.4", 6);
        mProductList.add(product1);
        mProductList.add(product2);
        mProductList.add(product3);
        mProductList.add(product4);
        mProductList.add(product5);
        mProductList.add(product6);
        myAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemID = menuItem.getItemId();
        switch (itemID) {
            case R.id.sent: {
                Toast.makeText(getApplicationContext(), "Navigation menu clicked Sent", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.draft: {
                Toast.makeText(getApplicationContext(), "Navigation menu clicked Draft", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.starred: {
                Toast.makeText(getApplicationContext(), "Navigation menu clicked Starred", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.all_mail: {
                Toast.makeText(getApplicationContext(), "Navigation menu clicked All-mail", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.delete: {
                Toast.makeText(getApplicationContext(), "Navigation menu clicked Delete", Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }

        return true;
    }
}
