package com.naufal.kidsnesia.auth.presentasi.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.naufal.kidsnesia.MainActivity
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.presentasi.register.RegisterActivity
import com.naufal.kidsnesia.auth.presentasi.reset_pass.confirm_email.ConfirmEmailActivity
import com.naufal.kidsnesia.databinding.ActivityLoginBinding
import org.koin.android.ext.android.get

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel = get()
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fullText = "Belum punya akun? Daftar"
        val spannable = SpannableString(fullText)

        // Membuat teks "Masuk" bisa diklik
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE // Warna teks biru
                ds.isUnderlineText = false // Hilangkan garis bawah
            }
        }

        // Menambahkan efek klik pada kata "Masuk"
        val startIndex = fullText.indexOf("Daftar")
        val endIndex = startIndex + "Daftar".length
        spannable.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Pastikan TextView bisa menangani klik
        binding.textView6.movementMethod = LinkMovementMethod.getInstance()
        binding.textView6.text = spannable

        setupView()
        setupAction()
        setupObserver()
        setupPasswordValidation()
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
        binding.textLupaPassword.setOnClickListener {
            startActivity(Intent(this, ConfirmEmailActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua Form Harus Terisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonLogin.isEnabled = !isLoading
        }

        viewModel.loginResult.observe(this) { result ->
            if (!isFinishing) {
                alertDialog?.dismiss()

                if (result is Resource.Success) {
                    alertDialog = AlertDialog.Builder(this).apply {
                        setTitle("Login Berhasil")
                        setMessage(result.data?.message?: "Login telah berhasil.") // Pastikan ini String
                        setPositiveButton("OK") { _, _ ->
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }.create()
                    alertDialog?.show()
                } else if (result is Resource.Error) {
                    alertDialog = AlertDialog.Builder(this).apply {
                        setTitle("Login Gagal")
                        setMessage(result.message ?: "Terjadi kesalahan, silakan coba lagi.") // Pastikan ini String
                        setPositiveButton("OK", null)
                    }.create()
                    alertDialog?.show()
                }
            }
        }
    }

    private fun setupPasswordValidation() {
        val passwordEditText = findViewById<TextInputEditText>(R.id.editTextPassword)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordLayout)

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()

                val error = when {
                    password.isEmpty() -> "Password tidak boleh kosong"
                    password.length < 12 -> "Minimal 12 karakter"
                    !password.matches(Regex(".*[A-Z].*")) -> "Harus ada huruf besar"
                    !password.matches(Regex(".*[0-9].*")) -> "Harus ada angka"
                    !password.matches(Regex(".*[!@#\$%^&*(),.?\":{}|<>].*")) -> "Harus ada karakter spesial"
                    else -> null
                }

                passwordLayout.error = error
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.greeting, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA, 1f).setDuration(100)
        val nameEdit = ObjectAnimator.ofFloat(binding.textEmail, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.editTextEmail, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.textSandi, View.ALPHA, 1f).setDuration(100)
        val pass = ObjectAnimator.ofFloat(binding.editTextPassword, View.ALPHA, 1f).setDuration(100)
        val passEdit = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.textView6, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, name, nameEdit, email, emailEdit, pass, passEdit, signup)
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