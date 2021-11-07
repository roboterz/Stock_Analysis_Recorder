package com.example.option.data


import androidx.room.*
import com.example.option.data.entities.*
import androidx.room.Transaction

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addStock(stock: Stock)

    @Update
    fun updateStock(vararg stock: Stock)

    @Delete
    fun deleteStock(stock: Stock)

    @Transaction
    @Query("SELECT * FROM Stock")
    fun getStocks(): List<Stock>

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Stock WHERE code = :rID")
    fun getStockByID(rID: String): Stock
}