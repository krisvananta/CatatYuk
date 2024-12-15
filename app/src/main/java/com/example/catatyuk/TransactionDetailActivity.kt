package com.example.catatyuk

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.catatyuk.databinding.ActivityTransactionDetailBinding

class TransactionDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTransactionDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Mengambil data dari Intent
        val title = intent.getStringExtra("title")
        val amount = intent.getStringExtra("amount")
        val date = intent.getStringExtra("date")
        val notes = intent.getStringExtra("notes")

        // Menampilkan data di TextView
        binding.tvTransactionTitle.text = title
        binding.tvTransactionAmount.text = "Amount: $amount"
        binding.tvTransactionDate.text = "Date: $date"
        binding.tvTransactionNotes.text = "Notes: $notes"
    }
}