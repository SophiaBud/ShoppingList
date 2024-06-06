package com.mirea.kt.ribo.PurchaseList.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ShoppingList {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
}
