package com.example.catatyuk.database

//import androidx.lifecycle.LiveData
//import com.example.catatyuk.model.Transaksi
//
//@Dao
//interface TransaksiDao {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insert(transaksi: Transaksi)
//
//    @Update
//    fun update(transaksi: Transaksi)
//
//    @Delete
//    fun delete(transaksi: Transaksi)
//
//    @get:Query("SELECT * FROM transaksi")
//    val allTransaction: LiveData<List<Transaksi>>
//
//    @Query("SELECT SUM(amount) FROM transaksi WHERE type = 'income'")
//    suspend fun getTotalIncome(): Double
//
//    @Query("SELECT SUM(amount) FROM transaksi WHERE type = 'expense'")
//    suspend fun getTotalExpense(): Double
//}