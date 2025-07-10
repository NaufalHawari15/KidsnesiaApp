package com.naufal.kidsnesia.auth.presentasi.reset_pass.new_pass

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.ResetPassRequest
import com.naufal.kidsnesia.auth.presentasi.login.LoginActivity
import com.naufal.kidsnesia.databinding.ActivityResetPassBinding
import org.koin.android.ext.android.get

class ResetPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPassBinding
    private val viewModel: ResetPassViewModel = get()
    private lateinit var tokenReset: String
    private lateinit var email: String
    private lateinit var masterAnimator: AnimatorSet
    private val cloudAnimators = mutableListOf<Animator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenReset = intent.getStringExtra("tokenReset").orEmpty()
        email = intent.getStringExtra("email").orEmpty()

        setupView()
        setupAction()
        setupObservers()
        setupTextWatchers()
        playAnimation()
        setupCloudAnimations()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.buttonResetPassword.setOnClickListener {
            val password = binding.editTextPassword.text?.toString()?.trim() ?: ""
            val confirmPassword = binding.editTextConfirmPassword.text?.toString()?.trim() ?: ""

            when {
                password.isEmpty() -> {
                    binding.inputLayoutPassword.error = "Kata sandi harus diisi"
                    binding.editTextPassword.requestFocus()
                }
                password.length < 6 -> {
                    binding.inputLayoutPassword.error = "Kata sandi minimal 6 karakter"
                    binding.editTextPassword.requestFocus()
                }
                confirmPassword.isEmpty() -> {
                    binding.inputLayoutConfirmPassword.error = "Konfirmasi kata sandi harus diisi"
                    binding.editTextConfirmPassword.requestFocus()
                }
                password != confirmPassword -> {
                    binding.inputLayoutConfirmPassword.error = "Konfirmasi kata sandi tidak sama"
                    binding.editTextConfirmPassword.requestFocus()
                    shakeView(binding.inputLayoutConfirmPassword)
                }
                else -> {
                    clearAllErrors()
                    binding.buttonResetPassword.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                    viewModel.resetPassword(password, confirmPassword, tokenReset)
                }
            }
        }

        binding.textBackToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    private fun setupTextWatchers() {
        binding.editTextPassword.addTextChangedListener {
            binding.inputLayoutPassword.error = null
            validatePasswordStrength(it.toString())
        }

        binding.editTextConfirmPassword.addTextChangedListener {
            binding.inputLayoutConfirmPassword.error = null
            validatePasswordMatch()
        }
    }

    private fun validatePasswordStrength(password: String?) {
        val pwd = password?.trim() ?: ""
        if (pwd.isNotEmpty() && pwd.length < 6) {
            binding.inputLayoutPassword.error = "Kata sandi minimal 6 karakter"
        }
    }

    private fun validatePasswordMatch() {
        val password = binding.editTextPassword.text?.toString()?.trim() ?: ""
        val confirmPassword = binding.editTextConfirmPassword.text?.toString()?.trim() ?: ""

        if (confirmPassword.isNotEmpty() && password != confirmPassword) {
            binding.inputLayoutConfirmPassword.error = "Konfirmasi kata sandi tidak sama"
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { showLoadingState(it) }

        viewModel.resetResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoadingState(true)
                is Resource.Success -> {
                    showLoadingState(false)
                    clearAllErrors()
                    animateSuccess {
                        showSuccessOverlay {
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this, LoginActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                startActivity(intent)
                                finish()
                            }, 2000)
                        }
                    }
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    handleError(result.message)
                }
            }
        }

        viewModel.validationErrors.observe(this) { errors ->
            errors["password"]?.let { binding.inputLayoutPassword.error = it }
            errors["confirmPassword"]?.let { binding.inputLayoutConfirmPassword.error = it }
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.buttonResetPassword.isEnabled = !isLoading
        binding.editTextPassword.isEnabled = !isLoading
        binding.editTextConfirmPassword.isEnabled = !isLoading
    }

    private fun clearAllErrors() {
        binding.inputLayoutPassword.error = null
        binding.inputLayoutConfirmPassword.error = null
    }

    private fun handleError(message: String?) {
        val errorMessage = message ?: "Terjadi kesalahan"
        showErrorOverlay(errorMessage)
        shakeView(binding.rootLayout)
    }

    private fun showSuccessOverlay(onComplete: () -> Unit) {
        binding.successOverlay.visibility = View.VISIBLE
        binding.successMessage.text = "Kata sandi berhasil diubah!"

        Handler(Looper.getMainLooper()).postDelayed({
            onComplete()
        }, 1500)
    }

    private fun showErrorOverlay(message: String) {
        binding.errorOverlay.visibility = View.VISIBLE
        binding.errorMessage.text = message

        Handler(Looper.getMainLooper()).postDelayed({
            binding.errorOverlay.visibility = View.GONE
        }, 3000)
    }

    private fun animateSuccess(onComplete: () -> Unit) {
        val scaleX = ObjectAnimator.ofFloat(binding.buttonResetPassword, "scaleX", 1f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.buttonResetPassword, "scaleY", 1f, 1.1f, 1f)
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 300
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) = onComplete()
            })
            start()
        }
    }

    private fun shakeView(view: View) {
        ObjectAnimator.ofFloat(view, "translationX", 0f, 25f, -25f, 0f).apply {
            duration = 500
            start()
        }
    }

    private fun playAnimation() {
        val logoAnim = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.logoKidsnesia, "scaleX", 0f, 1.2f, 1f),
                ObjectAnimator.ofFloat(binding.logoKidsnesia, "scaleY", 0f, 1.2f, 1f),
                ObjectAnimator.ofFloat(binding.logoKidsnesia, "alpha", 0f, 1f)
            )
            duration = 800
            interpolator = OvershootInterpolator(1.2f)
        }

        val formViews = listOf(
            binding.greeting,
            binding.nextText,
            binding.inputLayoutPassword,
            binding.inputLayoutConfirmPassword,
            binding.passwordRequirements,
            binding.buttonResetPassword,
            binding.textBackToLogin
        )

        val animators = formViews.mapIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 50f
            AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f),
                    ObjectAnimator.ofFloat(view, "translationY", 50f, 0f)
                )
                startDelay = 100 + index * 25L
                duration = 200
                interpolator = DecelerateInterpolator()
            }
        }

        masterAnimator = AnimatorSet().apply {
            play(logoAnim).before(AnimatorSet().apply {
                playSequentially(animators)
            })
            start()
        }
    }

    private fun setupCloudAnimations() {
        val cloud1Animator = ObjectAnimator.ofFloat(binding.cloud1, "translationY", 0f, -20f, 0f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        val cloud2Animator = ObjectAnimator.ofFloat(binding.cloud2, "translationY", 0f, -15f, 0f).apply {
            duration = 4000
            startDelay = 1000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        val cloud3Animator = ObjectAnimator.ofFloat(binding.cloud3, "translationY", 0f, -10f, 0f).apply {
            duration = 5000
            startDelay = 2000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        cloudAnimators.addAll(listOf(cloud1Animator, cloud2Animator, cloud3Animator))
    }

    override fun onDestroy() {
        if (::masterAnimator.isInitialized) {
            masterAnimator.cancel()
        }
        cloudAnimators.forEach { it.cancel() }
        super.onDestroy()
    }
}