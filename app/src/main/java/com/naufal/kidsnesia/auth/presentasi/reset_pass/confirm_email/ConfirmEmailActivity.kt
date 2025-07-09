package com.naufal.kidsnesia.auth.presentasi.reset_pass.confirm_email

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailRequest
import com.naufal.kidsnesia.auth.presentasi.reset_pass.reset_otp.ResetOtpActivity
import com.naufal.kidsnesia.databinding.ActivityConfirmEmailBinding
import org.koin.android.ext.android.get

class ConfirmEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmEmailBinding
    private val viewModel: ConfirmEmailViewModel = get()
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
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

    private fun setupAction() {
        binding.buttonKonfirm.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            if (email.isEmpty()) {
                showToast("Email tidak boleh kosong")
                return@setOnClickListener
            }

            viewModel.sendEmail(SendEmailRequest(email))
            observeEmailResult(email)
        }
    }

    private fun observeEmailResult(email: String) {
        viewModel.confirmEmailResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    showToast("OTP dikirim ke email")
                    val intent = Intent(this, ResetOtpActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }
                is Resource.Error -> {
                    showLoading(false)
                    showToast(result.message ?: "Gagal mengirim email")
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.buttonKonfirm.isEnabled = !isLoading
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.greeting, View.ALPHA, 1f).setDuration(100)
        val nameEdit = ObjectAnimator.ofFloat(binding.textEmail, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.editTextEmail, View.ALPHA, 1f).setDuration(100)
        val buttonConfirm = ObjectAnimator.ofFloat(binding.buttonKonfirm, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, nameEdit, email, buttonConfirm)
            startDelay = 100
            start()
        }
    }

    override fun onDestroy() {
        alertDialog?.dismiss()
        alertDialog = null
        super.onDestroy()
    }
}