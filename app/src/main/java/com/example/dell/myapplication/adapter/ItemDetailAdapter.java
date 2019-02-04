package com.example.dell.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.model.CompanyInfo;
import com.example.dell.myapplication.model.Product;

import java.util.List;
import java.util.Locale;

public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder> {

    private Context context;
    private List<Product> listProduct;

    public ItemDetailAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.listProduct = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_view_item_detail, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Product product = listProduct.get(position);
        CompanyInfo companyInfo = product.getCompanyInfo();

        viewHolder.tvCompanyName.setText(companyInfo.getCompanyName());
        viewHolder.tvPrice.setText(String.format(Locale.US, "%s", product.getProPrice()));
//        viewHolder.tvDescription.setText(product.getProductDescription());
        viewHolder.tvProductName.setText(product.getProName());
        viewHolder.tvTel.setText(companyInfo.getTel());
        viewHolder.tvEmail.setText(companyInfo.getEmail());

    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCompanyName;
        TextView tvProductName;
        TextView tvPrice;
        TextView tvOrderedItem;
        TextView tvTotalAmount;
        TextView tvDescription;
        TextView tvTel;
        TextView tvEmail;
        ImageView btnAddToCart;
        ImageView btnRemoveFromCart;
        EditText etOrderedQuantity;
        Button btnSubmit;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOrderedItem = itemView.findViewById(R.id.tvOrderedItem);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTel = itemView.findViewById(R.id.tvTel);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart1);
            btnRemoveFromCart = itemView.findViewById(R.id.btnRemoveFromCart1);
            etOrderedQuantity = itemView.findViewById(R.id.etOrderedQuantity);
            btnSubmit = itemView.findViewById(R.id.btnSubmit);
        }
    }
}
