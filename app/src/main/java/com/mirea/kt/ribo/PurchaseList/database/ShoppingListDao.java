package com.mirea.kt.ribo.PurchaseList.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingListDao {
    @Query("SELECT * FROM ShoppingList")
    List<ShoppingList> getAllShoppingLists();

    @Insert
    void insert(ShoppingList shoppingList);

    @Update
    void update(ShoppingList shoppingList);

    @Delete
    void delete(ShoppingList shoppingList);
}
