package com.example.catatyuk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.catatyuk.databinding.ItemTransaksiBinding
import com.example.catatyuk.model.Transaksi

typealias OnClickTransaksi = (Transaksi) -> Unit

class TransaksiAdapter(private val transaksiList: List<Transaksi>, private val onClickTransaksi: OnClickTransaksi) : RecyclerView.Adapter<TransaksiAdapter.ItemTransaksiViewHolder>() {

    inner class ItemTransaksiViewHolder(private val binding: ItemTransaksiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Transaksi) {
            with(binding) {
                tvTransaksiName.text = data.title
                tvTransaksiAmount.text = "Rp ${data.amount}" // Menambahkan "Rp" sebelum jumlah

                // Set warna amount berdasarkan tipe transaksi
                if (data.type == "income") {
                    tvTransaksiAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                } else if (data.type == "expense") {
                    tvTransaksiAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                }

                itemView.setOnClickListener {
                    onClickTransaksi(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTransaksiViewHolder {
        val binding = ItemTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemTransaksiViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return transaksiList.size
    }

    override fun onBindViewHolder(holder: ItemTransaksiViewHolder, position: Int) {
        holder.bind(transaksiList[position])
    }


}