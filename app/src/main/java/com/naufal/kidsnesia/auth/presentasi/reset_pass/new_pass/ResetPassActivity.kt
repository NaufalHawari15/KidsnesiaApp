package com.naufal.kidsnesia.auth.presentasi.reset_pass.new_pass

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.ResetPassRequest
import com.naufal.kidsnesia.auth.presentasi.login.LoginActivity
import com.naufal.kidsnesia.databinding.ActivityResetPassBinding
import org.koin.android.ext.android.get

class ResetPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPassBinding
    private val viewModel: ResetPassViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tokenReset = intent.getStringExtra("tokenReset").orEmpty()
        binding.buttonConfirm.setOnClickListener {
            val pass = binding.editTextPass.text.toString()
            val confirmPass = binding.editTextConfirmPass.text.toString()

            if (pass.length < 6 || confirmPass.length < 6) {
                showToast("Password minimal 6 karakter")
                return@setOnClickListener
            }
            if (pass != confirmPass) {
                showToast("Konfirmasi password tidak sama")
                return@setOnClickListener
            }

            viewModel.resetPassword(ResetPassRequest(pass, confirmPass), tokenReset)
            observeResetResult()
        }

        setupView()
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

    private fun observeResetResult() {
        viewModel.resetResult.observe(this) {
            when (it) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    showToast("Password berhasil diubah")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
                is Resource.Error -> {
                    showLoading(false)
                    showToast(it.message ?: "Gagal reset password")
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.buttonConfirm.isEnabled = !isLoading
    }
}