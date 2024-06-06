package com.mirea.kt.ribo.PurchaseList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.mirea.kt.ribo.PurchaseList.database.AppDatabase;
import com.mirea.kt.ribo.PurchaseList.database.Product;
import com.mirea.kt.ribo.PurchaseList.database.ShoppingList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText editTextShoppingListName;
    private RecyclerView recyclerViewShoppingLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextShoppingListName = findViewById(R.id.editTextShoppingListName);
        Button buttonAddShoppingList = findViewById(R.id.buttonAddShoppingList);
        Button buttonShareShoppingLists = findViewById(R.id.buttonShareShoppingLists);
        recyclerViewShoppingLists = findViewById(R.id.recyclerViewShoppingLists);

        recyclerViewShoppingLists.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "shopping-list-db").allowMainThreadQueries().build();
        buttonAddShoppingList.setOnClickListener(v -> {
            String listName = editTextShoppingListName.getText().toString();
            if (!listName.isEmpty()) {
                ShoppingList shoppingList = new ShoppingList();
                shoppingList.name = listName;
                db.shoppingListDao().insert(shoppingList);
                updateShoppingLists();
            } else Toast.makeText(this, "Введите название списка", Toast.LENGTH_SHORT).show();
        });
        buttonShareShoppingLists.setOnClickListener(v -> shareShoppingLists());
        updateShoppingLists();
    }
    private void updateShoppingLists() {
        List<ShoppingList> shoppingLists = db.shoppingListDao().getAllShoppingLists();
        ShoppingListAdapter adapter = new ShoppingListAdapter(shoppingLists, db.shoppingListDao(), this);
        recyclerViewShoppingLists.setAdapter(adapter);
    }
    private void shareShoppingLists() {
        List<ShoppingList> shoppingLists = db.shoppingListDao().getAllShoppingLists();
        StringBuilder shoppingListsString = new StringBuilder();
        for (ShoppingList list : shoppingLists) {
            shoppingListsString.append(list.name).append("\n");
            List<Product> products = db.productDao().getProductsByListId(list.id);
            for (Product product : products) {
                shoppingListsString
                        .append(product.name)
                        .append(" - ")
                        .append(product.quantity)
                        .append(" ")
                        .append(product.unit)
                        .append(", Цена: ")
                        .append(product.price)
                        .append("₽\n");
            }
            shoppingListsString.append("\n");
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shoppingListsString.toString());
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }


}