package com.example.catatyuk.database

//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.catatyuk.model.Transaksi
//
//@Database(entities = [Transaksi::class], version = 1, exportSchema = false)
//abstract class DatabaseInstance:RoomDatabase() {
//    abstract fun transaksiDao():TransaksiDao?
//
//    companion object{
//        private var INSTANCE:DatabaseInstance?=null
//        fun getDatabase(context: Context):DatabaseInstance?{
//            if (INSTANCE == null){
//                synchronized(DatabaseInstance::class.java){
//                    INSTANCE = Room.databaseBuilder(
//                        context.applicationContext,
//                        DatabaseInstance::class.java,"transaksi_db"
//                    ).build()
//                }
//            }
//            return INSTANCE
//        }
//    }
//}