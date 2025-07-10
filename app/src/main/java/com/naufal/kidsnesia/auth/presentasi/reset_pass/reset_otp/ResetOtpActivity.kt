package com.naufal.kidsnesia.auth.presentasi.reset_pass.reset_otp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.VerifyOtpRequest
import com.naufal.kidsnesia.auth.presentasi.reset_pass.new_pass.ResetPassActivity
import com.naufal.kidsnesia.databinding.ActivityResetOtpBinding
import org.koin.android.ext.android.get

class ResetOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetOtpBinding
    private lateinit var email: String
    private val viewModel: ResetOtpViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra("email") ?: ""

        setupView()
        moveOtpFocus()
        observeViewModel()

        binding.tvResend.setOnClickListener {
            viewModel.resendOtp(SendEmailRequest(email))
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
                viewModel.verifyOtp(VerifyOtpRequest(otp, email))
            } else {
                // Tampilkan error di overlay, bukan Toast
                showErrorOverlay("Lengkapi semua angka OTP")
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
        // Tampilkan email di UI jika ada TextView untuk itu
        binding.showEmail?.text = "Kode OTP telah dikirim ke $email"
    }

    private fun observeViewModel() {
        viewModel.verifyOtpResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    binding.successOverlay.visibility = View.VISIBLE
                    binding.successMessage.text = result.data?.message ?: "Verifikasi berhasil!"

                    Handler(Looper.getMainLooper()).postDelayed({
                        val token = result.data?.resetResult?.tokenReset.orEmpty()
                        val email = result.data?.resetResult?.email.orEmpty()
                        val intent = Intent(this, ResetPassActivity::class.java)
                        intent.putExtra("tokenReset", token)
                        intent.putExtra("email", email)
                        startActivity(intent)
                        finish()
                    }, 2000)
                }
                is Resource.Error -> {
                    showLoading(false)
                    val errorMessage = result.message ?: "Terjadi kesalahan"

                    when (errorMessage) {
                        "Kode OTP salah." -> {
                            showErrorOverlay(errorMessage, isExpiredError = false)
                        }
                        "Kode OTP sudah kadaluarsa." -> {
                            showErrorOverlay(errorMessage, isExpiredError = true)
                        }
                        else -> {
                            // Semua error lainnya juga ditampilkan di overlay
                            showErrorOverlay(errorMessage, isExpiredError = false)
                        }
                    }
                }
            }
        }

        viewModel.resendResult.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    showSuccessOverlay("Kode OTP dikirim ulang ke $email")
                }
                is Resource.Error -> {
                    val errorMessage = result.message ?: "Gagal mengirim ulang OTP"
                    showErrorOverlay("Resend gagal: $errorMessage")
                }
                is Resource.Loading -> {
                    // Optional: bisa tambahkan loading untuk resend
                }
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
        binding.tvResend.isEnabled = !isLoading
    }

    private fun showErrorOverlay(message: String, isExpiredError: Boolean = false) {
        if (isExpiredError) {
            // Gunakan error2Overlay untuk OTP expired
            binding.error2Overlay.visibility = View.VISIBLE
            binding.error2Message.text = message
            hideOverlayAfter(binding.error2Overlay)
        } else {
            // Gunakan errorOverlay untuk error biasa
            binding.errorOverlay.visibility = View.VISIBLE
            binding.errorMessage.text = message
            hideOverlayAfter(binding.errorOverlay)
        }
    }

    private fun showSuccessOverlay(message: String) {
        binding.successOverlay.visibility = View.VISIBLE
        binding.successMessage.text = message
        hideOverlayAfter(binding.successOverlay, 2000L)
    }

    private fun hideOverlayAfter(view: View, delay: Long = 3000L) {
        Handler(Looper.getMainLooper()).postDelayed({
            view.visibility = View.GONE
        }, delay)
    }
}

