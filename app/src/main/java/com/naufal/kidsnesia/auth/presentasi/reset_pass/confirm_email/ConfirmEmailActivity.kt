package com.naufal.kidsnesia.auth.presentasi.reset_pass.confirm_email

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
    private lateinit var cloudAnimators: MutableList<AnimatorSet>
    private lateinit var masterAnimator: AnimatorSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
        playAnimation()
        setupCloudAnimations()
        observeEmailResult()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.buttonKonfirm.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            when {
                email.isEmpty() -> {
                    showEmailError("Email harus diisi")
                    return@setOnClickListener
                }
                !isValidEmail(email) -> {
                    showEmailError("Format email tidak valid")
                    return@setOnClickListener
                }
            }
            viewModel.sendEmail(SendEmailRequest(email))
        }

        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    clearEmailError()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeEmailResult() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoadingState(isLoading)
        }

        viewModel.confirmEmailResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoadingState(true)
                    clearAllErrors()
                }
                is Resource.Success -> {
                    showLoadingState(false)
                    clearAllErrors()
                    animateSuccess {
                        showSuccessOverlay {
                            val email = binding.editTextEmail.text.toString().trim()
                            val intent = Intent(this, ResetOtpActivity::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        }
                    }
                    showToast("OTP dikirim ke email")
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    val errorMsg = result.message ?: "Gagal mengirim email"

                    Log.d("ConfirmEmail", "Received error message: $errorMsg")

                    // Tampilkan error di input field
                    showEmailError(errorMsg)
                }
            }
        }
    }

    private fun showEmailError(message: String) {
        Log.d("ConfirmEmail", "Showing email error: $message")

        // Pastikan error message tidak kosong
        val displayMessage = if (message.isBlank()) "Terjadi kesalahan" else message

        binding.inputLayoutEmail.error = displayMessage
        binding.editTextEmail.requestFocus()

        // Shake animation
        shakeView(binding.inputLayoutEmail)

        // Ubah warna border menjadi merah
        try {
            binding.inputLayoutEmail.boxStrokeColor = ContextCompat.getColor(this, android.R.color.holo_red_dark)
        } catch (e: Exception) {
            Log.e("ConfirmEmail", "Error setting box stroke color", e)
        }
    }

    private fun clearEmailError() {
        binding.inputLayoutEmail.error = null
        try {
            binding.inputLayoutEmail.boxStrokeColor = ContextCompat.getColor(this, R.color.pink)
        } catch (e: Exception) {
            Log.e("ConfirmEmail", "Error resetting box stroke color", e)
        }
    }

    private fun clearAllErrors() {
        clearEmailError()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.buttonKonfirm.isEnabled = !isLoading
        binding.editTextEmail.isEnabled = !isLoading
    }

    private fun showSuccessOverlay(onComplete: () -> Unit) {
        binding.successOverlay.visibility = View.VISIBLE
        binding.successOverlay.alpha = 0f
        binding.successOverlay.animate()
            .alpha(1f)
            .setDuration(500)
            .withEndAction {
                Handler(Looper.getMainLooper()).postDelayed({
                    onComplete()
                }, 1500)
            }
            .start()
    }

    private fun isValidEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun animateSuccess(onComplete: () -> Unit) {
        val scaleX = ObjectAnimator.ofFloat(binding.buttonKonfirm, "scaleX", 1f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.buttonKonfirm, "scaleY", 1f, 1.1f, 1f)
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 300
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onComplete()
                }
            })
            start()
        }
    }

    private fun shakeView(view: View) {
        Log.d("ConfirmEmail", "Starting shake animation")

        if (view.visibility != View.VISIBLE) {
            Log.w("ConfirmEmail", "View is not visible, cannot animate")
            return
        }

        // Reset posisi sebelum animasi
        view.translationX = 0f
        view.scaleX = 1f
        view.scaleY = 1f

        // Animasi shake
        val shake = ObjectAnimator.ofFloat(
            view,
            "translationX",
            0f, 25f, -25f, 20f, -20f, 15f, -15f, 10f, -10f, 0f
        ).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
        }

        // Efek scale ringan
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.02f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.02f, 1f)

        val animatorSet = AnimatorSet().apply {
            playTogether(shake, scaleX, scaleY)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    Log.d("ConfirmEmail", "Shake animation started")
                }
                override fun onAnimationEnd(animation: Animator) {
                    Log.d("ConfirmEmail", "Shake animation ended")
                    // Reset posisi setelah animasi
                    view.translationX = 0f
                    view.scaleX = 1f
                    view.scaleY = 1f
                }
            })
        }

        animatorSet.start()
    }

    private fun playAnimation() {
        val logoScale = ObjectAnimator.ofFloat(binding.logoKidsnesia, "scaleX", 0f, 1.2f, 1f)
        val logoScaleY = ObjectAnimator.ofFloat(binding.logoKidsnesia, "scaleY", 0f, 1.2f, 1f)
        val logoAlpha = ObjectAnimator.ofFloat(binding.logoKidsnesia, "alpha", 0f, 1f)
        val logoAnimSet = AnimatorSet().apply {
            playTogether(logoScale, logoScaleY, logoAlpha)
            duration = 800
            interpolator = OvershootInterpolator(1.2f)
        }

        val formViews = listOf(
            binding.greeting,
            binding.inputLayoutEmail,
            binding.buttonKonfirm,
        )

        formViews.forEach { view ->
            view.alpha = 0f
            view.translationY = 50f
        }

        val formAnimators = formViews.mapIndexed { index, view ->
            val translateY = ObjectAnimator.ofFloat(view, "translationY", 50f, 0f)
            val alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            AnimatorSet().apply {
                playTogether(translateY, alpha)
                duration = 400
                startDelay = (index * 100).toLong()
                interpolator = DecelerateInterpolator()
            }
        }

        masterAnimator = AnimatorSet().apply {
            playSequentially(logoAnimSet, *formAnimators.toTypedArray())
        }
        masterAnimator.start()
    }

    private fun setupCloudAnimations() {
        cloudAnimators = mutableListOf()

        val cloud1 = ObjectAnimator.ofFloat(binding.cloud1, "translationY", 0f, -20f, 0f)
        cloud1.duration = 3000
        cloud1.repeatCount = ObjectAnimator.INFINITE
        cloud1.interpolator = AccelerateDecelerateInterpolator()
        cloud1.start()
        cloudAnimators.add(AnimatorSet().apply { play(cloud1) })

        val cloud2 = ObjectAnimator.ofFloat(binding.cloud2, "translationY", 0f, -15f, 0f)
        cloud2.duration = 4000
        cloud2.startDelay = 1000
        cloud2.repeatCount = ObjectAnimator.INFINITE
        cloud2.interpolator = AccelerateDecelerateInterpolator()
        cloud2.start()
        cloudAnimators.add(AnimatorSet().apply { play(cloud2) })

        val cloud3 = ObjectAnimator.ofFloat(binding.cloud3, "translationY", 0f, -10f, 0f)
        cloud3.duration = 5000
        cloud3.startDelay = 2000
        cloud3.repeatCount = ObjectAnimator.INFINITE
        cloud3.interpolator = AccelerateDecelerateInterpolator()
        cloud3.start()
        cloudAnimators.add(AnimatorSet().apply { play(cloud3) })
    }

    override fun onDestroy() {
        if (::masterAnimator.isInitialized) {
            masterAnimator.cancel()
        }
        cloudAnimators.forEach { it.cancel() }
        super.onDestroy()
    }
}



