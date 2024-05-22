package com.example.homemanagement.data.database.shoppingItem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.homemanagement.data.database.shoppingItem.ShoppingItem

@Dao
interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_items")
    fun getAllShoppingItems(): List<ShoppingItem>

    @Insert
    fun insertShoppingItem(shoppingItem: ShoppingItem)

    @Delete
    fun deleteShoppingItem(shoppingItem: ShoppingItem)
}
