package com.example.dell.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.myapplication.model.ProductData;
import com.example.dell.myapplication.ui.main.MainMvpPresenter;
import com.example.dell.myapplication.ui.main.MainPresenter;
import com.example.dell.myapplication.ui.product.ProductDetailActivity;
import com.example.dell.myapplication.R;
import com.example.dell.myapplication.model.CompanyInfo;

import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ProductData> productList;
    private AddProductToCartListener onAddClickListener;
    private DeleteProductFromCartListener onDeleteClickListener;
    private Context context;
    private MainMvpPresenter mainMvpPresenter;

    public MyAdapter(Context context, List<ProductData> productList,
                     AddProductToCartListener listener,
                     DeleteProductFromCartListener deleteListener) {
        this.productList = productList;
        this.onAddClickListener = listener;
        this.onDeleteClickListener = deleteListener;
        this.context = context;
        mainMvpPresenter = new MainPresenter(context);
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
        final ProductData product = productList.get(position);
        String productPrice = product.getProductPrice().replace('$',' ');

        viewHolder.productName.setText(product.getProductTitle());
        viewHolder.productPrice.setText(String.format(Locale.US, "%.2f", Double.parseDouble(productPrice)));
        viewHolder.productQuantity.setText(String.format(Locale.US, "%d", 0));
        Glide.with(context)
                .load(product.getProductImage())
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
        viewHolder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productViews = mainMvpPresenter.addProductViewsRecord(product.getProductID());
                String productPrice = product.getProductPrice().replace('$', ' ');
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("proImage", product.getProductImage());
                intent.putExtra("proName", product.getProductTitle());
                intent.putExtra("proPrice", productPrice);
                intent.putExtra("proQuantity", product.getProductQuantity());
                intent.putExtra("companyName", product.getSalerName());
                intent.putExtra("tel", product.getSalerTel());
                intent.putExtra("description", product.getProductDescription());
                intent.putExtra("productID", product.getProductID());
                intent.putExtra("productViews", productViews);
                intent.putExtra("userID", product.getUserID());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, viewHolder.productImage, viewHolder.productImage.getTransitionName());
                context.startActivity(intent, options.toBundle());
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
            btnAddProductToCart = itemView.findViewById(R.id.btnAddToCart1);
        }
    }

    public interface AddProductToCartListener {
        void onClick(ProductData mProduct, TextView productQuantity);
    }

    public interface DeleteProductFromCartListener {
        void onClick(ProductData mProduct, TextView productQuantity);
    }
}
