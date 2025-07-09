package com.naufal.kidsnesia.auth.presentasi.reset_pass.reset_otp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.button.setOnClickListener {
            val otp = listOf(
                binding.otp1.text.toString(),
                binding.otp2.text.toString(),
                binding.otp3.text.toString(),
                binding.otp4.text.toString(),
                binding.otp5.text.toString(),
                binding.otp6.text.toString()
            ).joinToString("")

            if (otp.length != 6) {
                showToast("OTP harus 6 digit")
                return@setOnClickListener
            }

            viewModel.verifyOtp(VerifyOtpRequest(otp, email))
            observeVerifyOtp()
        }

        binding.tvResend.setOnClickListener {
            viewModel.resendOtp(SendEmailRequest(email))
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

    private fun observeVerifyOtp() {
        viewModel.verifyOtpResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    val token = result.data?.resetResult?.tokenReset.orEmpty()
                    val email = result.data?.resetResult?.email.orEmpty()

                    val intent = Intent(this, ResetPassActivity::class.java)
                    intent.putExtra("tokenReset", token)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                }
                is Resource.Error -> {
                    showLoading(false)
                    showToast(result.message ?: "OTP salah")
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.button.isEnabled = !isLoading
    }
}