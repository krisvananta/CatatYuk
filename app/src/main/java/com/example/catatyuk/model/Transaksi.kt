package com.example.catatyuk.model

import com.google.gson.annotations.SerializedName

data class Transaksi(
    @SerializedName("_id")
    val id: String = "",

    @SerializedName("type")
    val type: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("amount")
    val amount: Int,

    @SerializedName("date")
    val date: String,

    @SerializedName("notes")
    val notes: String
)
