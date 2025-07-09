package com.naufal.kidsnesia.auth.presentasi.otp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.presentasi.login.LoginActivity
import com.naufal.kidsnesia.databinding.ActivityOtpBinding
import org.koin.android.ext.android.get

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private val viewModel: OtpViewModel = get()
    private lateinit var tokenVerifikasi: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenVerifikasi = intent.getStringExtra("token_verifikasi") ?: ""

        binding.tvResend.setOnClickListener {
            viewModel.resendOtp(tokenVerifikasi)
        }

        binding.button.setOnClickListener {
            val otp = listOf(
                binding.otp1.text.toString(),
                binding.otp2.text.toString(),
                binding.otp3.text.toString(),
                binding.otp4.text.toString(),
                binding.otp5.text.toString(),
                binding.otp6.text.toString()
            ).joinToString("")

            if (otp.length == 6) {
                viewModel.verifyOtp(otp, tokenVerifikasi)
            } else {
                Toast.makeText(this, "Lengkapi semua angka OTP", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.otpResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.button.isEnabled = false
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.button.isEnabled = true
                    Toast.makeText(this, "OTP berhasil diverifikasi", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.button.isEnabled = true

                    val errorMessage = result.message ?: "Terjadi kesalahan saat verifikasi"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.resendResult.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.resendResult?.tokenVerifikasi?.let { newToken ->
                        tokenVerifikasi = newToken // <- Update token agar verify pakai yang baru
                    }
                    Toast.makeText(this, "Kode OTP dikirim ulang", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    Toast.makeText(this, "Resend gagal: ${result.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        setupView()
        moveOtpFocus()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun moveOtpFocus() {
        val otpFields = listOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4, binding.otp5, binding.otp6)
        for (i in otpFields.indices) {
            otpFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < otpFields.size - 1) {
                        otpFields[i + 1].requestFocus()
                    } else if (s?.isEmpty() == true && i > 0) {
                        otpFields[i - 1].requestFocus()
                    }
                }
            })
        }
    }
}