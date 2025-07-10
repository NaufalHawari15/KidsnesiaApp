package com.naufal.kidsnesia.auth.presentasi.otp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

        setupView()
        moveOtpFocus()
        observeViewModel()

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
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun observeViewModel() {
        viewModel.otpResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(false)
                    binding.successOverlay.visibility = View.VISIBLE
                    binding.successMessage.text = result.data?.message ?: "Verifikasi berhasil!"
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }, 2000)
                }

                is Resource.Error -> {
                    showLoading(false)
                    when (result.message) {
                        "Kode OTP salah." -> {
                            binding.errorOverlay.visibility = View.VISIBLE
                            binding.errorMessage.text = result.message
                            hideOverlayAfter(binding.errorOverlay)
                        }

                        "Kode OTP sudah kadaluarsa." -> {
                            binding.error2Overlay.visibility = View.VISIBLE
                            binding.error2Message.text = result.message
                            hideOverlayAfter(binding.error2Overlay)
                        }

                        else -> {
                            Toast.makeText(this, result.message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        viewModel.resendResult.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.resendResult?.tokenVerifikasi?.let {
                        tokenVerifikasi = it
                    }
                    Toast.makeText(this, "Kode OTP dikirim ulang", Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    Toast.makeText(this, "Resend gagal: ${result.message}", Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }
        }
    }

    private fun moveOtpFocus() {
        val otpFields = listOf(
            binding.otp1, binding.otp2, binding.otp3,
            binding.otp4, binding.otp5, binding.otp6
        )

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

    private fun showLoading(isLoading: Boolean) {
        binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.button.isEnabled = !isLoading
    }

    private fun hideOverlayAfter(view: View, delay: Long = 3000L) {
        Handler(Looper.getMainLooper()).postDelayed({
            view.visibility = View.GONE
        }, delay)
    }
}

