package com.mirea.kt.ribo.PurchaseList;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.mirea.kt.ribo.PurchaseList.database.AppDatabase;
import com.mirea.kt.ribo.PurchaseList.database.Product;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText editTextProductName;
    private EditText editTextProductQuantity;
    private EditText editTextProductPrice;
    private Spinner spinnerUnit;
    private RecyclerView recyclerViewProducts;
    private int shoppingListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductQuantity = findViewById(R.id.editTextProductQuantity);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        Button buttonAddProduct = findViewById(R.id.buttonAddProduct);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "shopping-list-db").allowMainThreadQueries().build();

        shoppingListId = getIntent().getIntExtra("SHOPPING_LIST_ID", -1);
        if (shoppingListId == -1) {
            throw new IllegalArgumentException("Invalid shopping list ID.");
        }

        buttonAddProduct.setOnClickListener(v -> {
            String productName = editTextProductName.getText().toString();
            String productQuantityStr = editTextProductQuantity.getText().toString();
            String productPriceStr = editTextProductPrice.getText().toString();
            String unit = spinnerUnit.getSelectedItem().toString();

            if (!productName.isEmpty()) {
                double productQuantity;
                double productPrice;
                try {
                    productQuantityStr = productQuantityStr.replace(',', '.');
                    productQuantity = productQuantityStr.isEmpty() ? 0.0 : Double.parseDouble(productQuantityStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(ProductListActivity.this, "Неправильный формат количества", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    productPriceStr = productPriceStr.replace(',', '.');
                    productPrice = productPriceStr.isEmpty() ? 0.0 : Double.parseDouble(productPriceStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(ProductListActivity.this, "Неправильный формат цены", Toast.LENGTH_SHORT).show();
                    return;
                }

                Product product = new Product();
                product.name = productName;
                product.quantity = productQuantity;
                product.price = productPrice;
                product.unit = unit;
                product.shoppingListId = shoppingListId;
                db.productDao().insert(product);
                updateProductList();
                editTextProductName.setText("");
                editTextProductQuantity.setText("");
                editTextProductPrice.setText("");
            } else Toast.makeText(this, "Введите название продукта", Toast.LENGTH_SHORT).show();
        });

        updateProductList();
    }

    private void updateProductList() {
        List<Product> products = db.productDao().getProductsByListId(shoppingListId);
        ProductAdapter adapter = new ProductAdapter(products, db.productDao());
        recyclerViewProducts.setAdapter(adapter);
    }
}

