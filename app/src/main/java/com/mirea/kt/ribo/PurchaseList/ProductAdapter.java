package com.mirea.kt.ribo.PurchaseList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kt.ribo.PurchaseList.database.Product;
import com.mirea.kt.ribo.PurchaseList.database.ProductDao;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<Product> products;
    private final ProductDao productDao;

    public ProductAdapter(List<Product> products, ProductDao productDao) {
        this.products = products;
        this.productDao = productDao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.textViewProductName.setText(product.name);
        Locale Locale = new Locale("ru");
        if (product.unit.equals("кг")) {
            holder.textViewProductQuantity.setText(String.format(Locale, "%.2f %s", product.quantity, product.unit));
        } else {
            holder.textViewProductQuantity.setText(String.format(Locale, "%.0f %s", product.quantity, product.unit));
        }
        holder.textViewProductPrice.setText(String.format(Locale,"%.2f ₽", product.price));
        holder.checkBoxProduct.setChecked(product.isChecked);

        holder.checkBoxProduct.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.isChecked = isChecked;
            productDao.update(product);
        });

        holder.deleteProductButton.setOnClickListener(v -> {
            productDao.delete(product);
            products.remove(holder.getAbsoluteAdapterPosition());
            notifyItemRemoved(holder.getAbsoluteAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBoxProduct;
        public TextView textViewProductName;
        public TextView textViewProductQuantity;
        public TextView textViewProductPrice;
        public ImageButton deleteProductButton;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBoxProduct = itemView.findViewById(R.id.checkBoxProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductQuantity = itemView.findViewById(R.id.textViewProductQuantity);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            deleteProductButton = itemView.findViewById(R.id.deleteProductButton);
        }
    }
}

