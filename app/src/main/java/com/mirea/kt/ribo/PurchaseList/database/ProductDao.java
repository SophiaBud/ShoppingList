package com.mirea.kt.ribo.PurchaseList.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM Product WHERE shoppingListId = :shoppingListId")
    List<Product> getProductsByListId(int shoppingListId);

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);
}
