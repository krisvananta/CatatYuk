package com.example.catatyuk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.catatyuk.databinding.ActivityLoginBinding
import com.example.catatyuk.model.User
import com.example.catatyuk.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefManager = PrefManager.getInstance(this)

        checkLoginStatus()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val client = ApiClient.getInstance()

        with(binding) {
            loginBtn.setOnClickListener {
                val usernameInput = username.text.toString()
                val passwordInput = password.text.toString()
                if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Username dan password harus diisi", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val response = client.getAllUsers()
                response.enqueue(object : Callback<List<User>> {
                    override fun onResponse(
                        call: Call<List<User>>,
                        response: Response<List<User>>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val user = response.body()?.find { user ->
                                user.username == usernameInput && user.password == passwordInput
                            }

                            if (user != null) {
                                prefManager.setLoggedIn(true)
                                prefManager.saveUsername(usernameInput)
                                prefManager.savePassword(passwordInput)
                                prefManager.saveUserId(user.id)
                                checkLoginStatus()
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Koneksi error ${t.toString()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }

            registerRedirect.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    fun checkLoginStatus() {
        if (prefManager.isLoggedIn()) {
            // Arahkan ke halaman utama setelah login
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}