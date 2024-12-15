package com.example.catatyuk

import android.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catatyuk.databinding.FragmentHomeBinding
import com.example.catatyuk.model.Transaksi
//import com.example.catatyuk.model.Transaksi
import com.example.catatyuk.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var transaksiAdapter: TransaksiAdapter
    private val transaksiList = ArrayList<Transaksi>()

    val client = ApiClient.getInstance()
    val response = client.getTransaksi()
//    val transaksiList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        transaksiAdapter = TransaksiAdapter(transaksiList, { transaksi ->
            // Intent untuk detail transaksi
            val intent = Intent(requireContext(), TransactionDetailActivity::class.java).apply {
                putExtra("title", transaksi.title)
                putExtra("amount", transaksi.amount.toString())
                putExtra("date", transaksi.date)
                putExtra("notes", transaksi.notes)
            }
            startActivity(intent)
        }, { transaksi ->
            // Fungsi untuk menghapus transaksi
            deleteTransaksi(transaksi)
        })

        binding.rvTransaksi.apply {
            adapter = transaksiAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Mengambil data dari API
        val client = ApiClient.getInstance()
        val call = client.getTransaksi()

        call.enqueue(object : Callback<List<Transaksi>> {
            override fun onResponse(call: Call<List<Transaksi>>, response: Response<List<Transaksi>>) {
                if (response.isSuccessful) {
                    response.body()?.let { transactions ->
                        transaksiList.clear()
                        transaksiList.addAll(transactions)
                        transaksiAdapter.notifyDataSetChanged() // Notify adapter to refresh the data

                        // Menghitung total pemasukan dan pengeluaran
                        val totalIncome = transactions.filter { it.type == "income" }.sumOf { it.amount }
                        val totalExpense = transactions.filter { it.type == "expense" }.sumOf { it.amount }

                        // Menampilkan total pemasukan dan pengeluaran di TextView
                        binding.totalIncome.text = "Rp $totalIncome"
                        binding.totalExpense.text = "Rp $totalExpense"
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Transaksi>>, t: Throwable) {
                Toast.makeText(requireContext(), "Koneksi error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun deleteTransaksi(transaksi: Transaksi) {
        val client = ApiClient.getInstance()
        val call = client.deleteTransaksi(transaksi.id) // Ganti dengan ID transaksi yang sesuai

        call.enqueue(object : Callback<Transaksi> {
            override fun onResponse(call: Call<Transaksi>, response: Response<Transaksi>) {
                if (response.isSuccessful) {
                    transaksiList.remove(transaksi)
                    transaksiAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Transaksi dihapus", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Gagal menghapus transaksi", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Transaksi>, t: Throwable) {
                Toast.makeText(requireContext(), "Koneksi error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

//    private fun fetchTransactionData() {
//        val client = ApiClient.getInstance()
//        val call = client.getTransaksi()
//
//        call.enqueue(object : Callback<List<Transaksi>> {
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun onResponse(
//                call: Call<List<Transaksi>>,
//                response: Response<List<Transaksi>>
//            ) {
//                if (response.isSuccessful) {
//                    val transactions = response.body() ?: return
//
//                    // Dapatkan bulan dan tahun saat ini
//                    val currentMonth = java.time.LocalDate.now().monthValue
//                    val currentYear = java.time.LocalDate.now().year
//
//                    // Filter transaksi berdasarkan bulan dan tahun ini
//                    val filteredTransactions = transactions.filter {
//                        val transactionDate = it.date.split("-")
//                        val transactionMonth = transactionDate[1].toInt()
//                        val transactionYear = transactionDate[2].toInt()
//
//                        transactionMonth == currentMonth && transactionYear == currentYear
//                    }
//
//                    val totalIncome = filteredTransactions.filter { it.type == "income" }.sumOf { it.amount }
//                    val totalExpense = filteredTransactions.filter { it.type == "expense" }.sumOf { it.amount }
//
//                    binding.totalIncome.text = "Rp $totalIncome"
//                    binding.totalExpense.text = "Rp $totalExpense"
//                }
//            }
//
//            override fun onFailure(call: Call<List<Transaksi>>, t: Throwable) {
//                binding.totalIncome.text = "Error"
//                binding.totalExpense.text = "Error"
//            }
//        })
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}