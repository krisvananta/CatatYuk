package com.example.catatyuk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.catatyuk.databinding.FragmentTransactionBinding
import com.example.catatyuk.model.Transaksi
import com.example.catatyuk.network.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            val type = if (binding.radioIncome.isChecked) "income" else "expense"
            val title = binding.edtTitle.text.toString().trim()
            val amountText = binding.edtAmount.text.toString().trim()
            val date = binding.edtDate.text.toString().trim()
            val notes = binding.edtNotes.text.toString().trim()

            // Validasi input
            if (title.isEmpty() || amountText.isEmpty() || date.isEmpty()) {
                Toast.makeText(requireContext(), "Isi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Konversi amount ke integer dengan error handling
            val amount = try {
                amountText.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Jumlah harus berupa angka!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buat JSON request body
            val jsonObject = JSONObject().apply {
                put("type", type)
                put("title", title)
                put("amount", amount) // Pastikan amount adalah integer
                put("date", date)
                put("notes", notes)
            }

            val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val client = ApiClient.getInstance()
            client.addTransaksi(requestBody).enqueue(object : Callback<Transaksi> {
                override fun onResponse(call: Call<Transaksi>, response: Response<Transaksi>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        // kosongkan input setelah sukses
                        with(binding) {
                            edtTitle.text?.clear()
                            edtAmount.text?.clear()
                            edtDate.text?.clear()
                            edtNotes.text?.clear()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Gagal menambahkan transaksi", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Transaksi>, t: Throwable) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan koneksi", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

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
         * @return A new instance of fragment TransactionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}