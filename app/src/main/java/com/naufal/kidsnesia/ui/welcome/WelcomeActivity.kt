package com.naufal.kidsnesia.ui.welcome

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.naufal.kidsnesia.MainActivity
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.presentasi.login.LoginActivity
import com.naufal.kidsnesia.auth.presentasi.register.RegisterActivity
import com.naufal.kidsnesia.main_features.presentation.dashboard.DashboardViewModel
import com.naufal.kidsnesia.databinding.ActivityWelcomeBinding
import org.koin.android.ext.android.get

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val viewModel: DashboardViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        checkSession()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        binding.textDescProgram.text = Html.fromHtml(getString(R.string.desc_program), Html.FROM_HTML_MODE_LEGACY)
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.buttonLanjut.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun checkSession() {
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                navigateToMainActivity()
            } else {
                setupAction() // Tampilkan tombol login & register jika belum login
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Hapus WelcomeActivity dari stack agar tidak bisa kembali ke sini
    }
}