package com.example.catatyuk.network

import com.example.catatyuk.model.Transaksi
import com.example.catatyuk.model.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("transaksi")
    fun getTransaksi(): Call<List<Transaksi>>

    @GET("user")
    fun getAllUsers(): Call<List<User>>

    @POST("transaksi")
    fun addTransaksi(@Body rawJson: RequestBody): Call<Transaksi>

    @POST("user")
    fun addUser(@Body rawJson: RequestBody): Call<User>

    @POST("transaksi/{id}")
    fun updateTransaksi(@Path("id") transaksiId: String, @Body rawJson: RequestBody): Call<Transaksi>

    @DELETE("transaksi/{id}")
    fun deleteTransaksi(@Path("id") transaksiId: String): Call<Transaksi>
}