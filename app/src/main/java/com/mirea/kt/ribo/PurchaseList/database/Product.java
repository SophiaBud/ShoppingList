package com.mirea.kt.ribo.PurchaseList.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = ShoppingList.class,
        parentColumns = "id",
        childColumns = "shoppingListId",
        onDelete = ForeignKey.CASCADE))
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public double quantity;
    public double price;
    public boolean isChecked;
    public String unit;
    public int shoppingListId;

}
