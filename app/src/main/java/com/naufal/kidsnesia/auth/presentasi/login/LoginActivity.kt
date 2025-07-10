package com.naufal.kidsnesia.auth.presentasi.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import android.util.Patterns
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.naufal.kidsnesia.MainActivity
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.presentasi.otp.OtpActivity
import com.naufal.kidsnesia.auth.presentasi.register.RegisterActivity
import com.naufal.kidsnesia.auth.presentasi.reset_pass.confirm_email.ConfirmEmailActivity
import com.naufal.kidsnesia.databinding.ActivityLoginBinding
import org.koin.android.ext.android.get

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel = get()
    private var alertDialog: AlertDialog? = null
    private lateinit var masterAnimator: AnimatorSet
    private val cloudAnimators = mutableListOf<Animator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupClickableText()
        setupAction()
        setupObservers()
        setupTextWatchers()
        playAnimation()
        setupCloudAnimations()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupClickableText() {
        val fullText = "Belum punya akun? Daftar"
        val spannable = SpannableString(fullText)
        val startIndex = fullText.indexOf("Daftar")
        val endIndex = startIndex + "Daftar".length

        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = Color.BLUE
                ds.isUnderlineText = false
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.textView6.apply {
            text = spannable
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun setupAction() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    binding.inputLayoutEmail.error = "Email harus diisi"
                    binding.editTextEmail.requestFocus()
                }
                !isValidEmail(email) -> {
                    binding.inputLayoutEmail.error = "Format email tidak valid"
                    binding.editTextEmail.requestFocus()
                }
                password.isEmpty() -> {
                    binding.inputLayoutPassword.error = "Kata sandi harus diisi"
                    binding.editTextPassword.requestFocus()
                }
                else -> {
                    clearAllErrors()
                    binding.buttonLogin.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                    viewModel.login(email, password)
                }
            }
        }

        binding.textLupaPassword.setOnClickListener {
            val intent = Intent(this, ConfirmEmailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupTextWatchers() {
        binding.editTextEmail.addTextChangedListener {
            binding.inputLayoutEmail.error = null
        }
        binding.editTextPassword.addTextChangedListener {
            binding.inputLayoutPassword.error = null
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { showLoadingState(it) }

        viewModel.loginResult.observe(this) { result ->
            alertDialog?.dismiss()
            when (result) {
                is Resource.Loading -> showLoadingState(true)

                is Resource.Success -> {
                    showLoadingState(false)
                    clearAllErrors()
                    animateSuccess {
                        alertDialog = AlertDialog.Builder(this).apply {
                            setTitle("Login Berhasil")
                            setMessage(result.data?.message ?: "Login berhasil.")
                            setPositiveButton("OK") { _, _ ->
                                val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                startActivity(intent)
                                finish()
                            }
                        }.create()
                        alertDialog?.show()
                    }
                }

                is Resource.Error -> {
                    showLoadingState(false)
                    handleError(result.message)
                }
            }
        }

        viewModel.validationErrors.observe(this) { errors ->
            errors["email"]?.let { binding.inputLayoutEmail.error = it }
            errors["password"]?.let { binding.inputLayoutPassword.error = it }
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.buttonLogin.isEnabled = !isLoading
        binding.editTextEmail.isEnabled = !isLoading
        binding.editTextPassword.isEnabled = !isLoading
    }

    private fun clearAllErrors() {
        binding.inputLayoutEmail.error = null
        binding.inputLayoutPassword.error = null
    }

    private fun isValidEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun handleError(message: String?) {
        val errorMessage = message ?: "Terjadi kesalahan"

        val alertBuilder = AlertDialog.Builder(this)
            .setTitle("Login Gagal")
            .setPositiveButton("OK", null)

        when {
            errorMessage.contains("email", true) && errorMessage.contains("password", true) -> {
                binding.inputLayoutEmail.error = null
                binding.inputLayoutPassword.error = null
                alertBuilder.setMessage("Email atau kata sandi salah")
            }
            errorMessage.contains("password", true) -> {
                binding.inputLayoutPassword.error = null
                alertBuilder.setMessage("Kata sandi salah")
            }
            errorMessage.contains("email", true) -> {
                binding.inputLayoutEmail.error = null
                alertBuilder.setMessage("Email tidak ditemukan")
            }
            else -> {
                alertBuilder.setMessage(errorMessage)
            }
        }

        shakeView(binding.rootLayout)
        alertDialog = alertBuilder.create()
        alertDialog?.show()
    }

    private fun animateSuccess(onComplete: () -> Unit) {
        val scaleX = ObjectAnimator.ofFloat(binding.buttonLogin, "scaleX", 1f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.buttonLogin, "scaleY", 1f, 1.1f, 1f)
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
            binding.inputLayoutEmail,
            binding.inputLayoutPassword,
            binding.textLupaPassword,
            binding.buttonLogin,
            binding.textView6
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
        masterAnimator.cancel()
        cloudAnimators.forEach { it.cancel() }
        alertDialog?.dismiss()
        alertDialog = null
        super.onDestroy()
    }
}



//private fun showErrorSnackbar(message: String) {
//        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).apply {
//            setBackgroundTint(ContextCompat.getColor(context, R.color.error_red))
//            setTextColor(ContextCompat.getColor(context, R.color.white))
//        }.show()
//    }