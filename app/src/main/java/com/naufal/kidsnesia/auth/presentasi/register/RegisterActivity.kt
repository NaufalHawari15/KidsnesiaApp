package com.naufal.kidsnesia.auth.presentasi.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.presentasi.login.LoginActivity
import com.naufal.kidsnesia.auth.presentasi.otp.OtpActivity
import com.naufal.kidsnesia.databinding.ActivityRegisterBinding
import com.naufal.kidsnesia.ui.welcome.WelcomeActivity
import org.koin.android.ext.android.get

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel = get()
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fullText = "Sudah punya akun? Masuk"
        val spannable = SpannableString(fullText)

        // Membuat teks "Masuk" bisa diklik
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE // Warna teks biru
                ds.isUnderlineText = false // Hilangkan garis bawah
            }
        }

        // Menambahkan efek klik pada kata "Masuk"
        val startIndex = fullText.indexOf("Masuk")
        val endIndex = startIndex + "Masuk".length
        spannable.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Pastikan TextView bisa menangani klik
        binding.textView6.movementMethod = LinkMovementMethod.getInstance()
        binding.textView6.text = spannable

        setupView()
        setupAction()
        setupObserver()
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
        binding.buttonRegist.setOnClickListener {
            val name = binding.editTextNama.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val kataSandi = binding.editTextKataSandi.text.toString().trim()
            val noHp = binding.editTextHp.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || kataSandi.isEmpty() || noHp.isEmpty()) {
                Toast.makeText(this, "Semua Form Harus Terisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(name, email, kataSandi, noHp)
        }
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonRegist.isEnabled = !isLoading
        }

        viewModel.registerResult.observe(this) { result ->
            if (!isFinishing) {
                alertDialog?.dismiss()

                if (result is Resource.Success) {
                    val intent = Intent(this@RegisterActivity, OtpActivity::class.java).apply {
                        putExtra("email", result.data?.registerResult?.email)
                        putExtra("token_verifikasi", result.data?.registerResult?.tokenVerifikasi)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.greeting, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA, 1f).setDuration(100)
        val nameEdit = ObjectAnimator.ofFloat(binding.textNama, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.editTextNama, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.textEmail, View.ALPHA, 1f).setDuration(100)
        val pass = ObjectAnimator.ofFloat(binding.editTextEmail, View.ALPHA, 1f).setDuration(100)
        val passEdit = ObjectAnimator.ofFloat(binding.textKataSandi, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.editTextKataSandi, View.ALPHA, 1f).setDuration(100)
        val noHp = ObjectAnimator.ofFloat(binding.textHp, View.ALPHA, 1f).setDuration(100)
        val editHp = ObjectAnimator.ofFloat(binding.editTextHp, View.ALPHA, 1f).setDuration(100)
        val btnRegis = ObjectAnimator.ofFloat(binding.buttonRegist, View.ALPHA, 1f).setDuration(100)
        val textView = ObjectAnimator.ofFloat(binding.textView6, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, name, nameEdit, email, emailEdit, pass, passEdit, signup, noHp, editHp, btnRegis, textView)
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