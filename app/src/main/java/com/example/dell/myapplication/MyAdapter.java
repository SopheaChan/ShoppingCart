package com.example.dell.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.myapplication.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Product> productList;
    private AddProductToCartListener onAddClickListener;
    private DeleteProductFromCartListener onDeleteClickListener;
    private Context context;

    public MyAdapter (Context context, List<Product> productList, AddProductToCartListener listener, DeleteProductFromCartListener deleteListener){
        this.productList = productList;
        this.onAddClickListener = listener;
        this.onDeleteClickListener = deleteListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recycler_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final Product product = productList.get(position);

        viewHolder.productName.setText(product.getProName());
        viewHolder.productPrice.setText(product.getProPrice());
        viewHolder.productQuantity.setText(Integer.toString(product.getProQuantity()));
        Glide.with(context)
                .load(product.getProImage())
                .into(viewHolder.productImage);
        viewHolder.btnAddProductToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClickListener.onClick(product, viewHolder.productQuantity);

            }
        });
        viewHolder.btnDeleteProductFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onClick(product, viewHolder.productQuantity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private TextView btnDeleteProductFromCart;
        private TextView btnAddProductToCart;
        private TextView productQuantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.tvProductName);
            productPrice = itemView.findViewById(R.id.tvProductPrice);
            productQuantity = itemView.findViewById(R.id.tvProductQuantity);
            btnDeleteProductFromCart = itemView.findViewById(R.id.btnDeleteFromCartList);
            btnAddProductToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }

    interface AddProductToCartListener{
        void onClick(Product mProduct, TextView productQuantity);
    }

    interface DeleteProductFromCartListener{
        void onClick(Product mProduct, TextView productQuantity);
    }
}
