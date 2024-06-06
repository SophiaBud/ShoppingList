package com.mirea.kt.ribo.PurchaseList.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class, ShoppingList.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract ShoppingListDao shoppingListDao();
}
