package com.naufal.kidsnesia.auth.presentasi.register

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.presentasi.login.LoginActivity
import com.naufal.kidsnesia.auth.presentasi.otp.OtpActivity
import com.naufal.kidsnesia.databinding.ActivityRegisterBinding
import org.koin.android.ext.android.get

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel = get()
    private var alertDialog: AlertDialog? = null
    private lateinit var masterAnimator: AnimatorSet
    private val cloudAnimators = mutableListOf<AnimatorSet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupLoginLink()
        setupAction()
        setupObserver()
        playAnimation()
        setupCloudAnimations()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupLoginLink() {
        val fullText = "Sudah punya akun? Masuk"
        val spannable = SpannableString(fullText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@RegisterActivity, R.color.pink)
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }

        val startIndex = fullText.indexOf("Masuk")
        val endIndex = startIndex + "Masuk".length
        spannable.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.textView6.movementMethod = LinkMovementMethod.getInstance()
        binding.textView6.text = spannable
    }

    private fun setupAction() {
        binding.buttonRegist.setOnClickListener {
            val name = binding.editTextNama.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val kataSandi = binding.editTextKataSandi.text.toString().trim()
            val noHp = binding.editTextHp.text.toString().trim()

            // Enhanced validation
            when {
                name.isEmpty() -> {
                    binding.inputLayoutNama.error = "Nama lengkap harus diisi"
                    binding.editTextNama.requestFocus()
                    return@setOnClickListener
                }
                name.length < 2 -> {
                    binding.inputLayoutNama.error = "Nama minimal 2 karakter"
                    binding.editTextNama.requestFocus()
                    return@setOnClickListener
                }
                email.isEmpty() -> {
                    binding.inputLayoutEmail.error = "Email harus diisi"
                    binding.editTextEmail.requestFocus()
                    return@setOnClickListener
                }
                !isValidEmail(email) -> {
                    binding.inputLayoutEmail.error = "Format email tidak valid"
                    binding.editTextEmail.requestFocus()
                    return@setOnClickListener
                }
                kataSandi.isEmpty() -> {
                    binding.inputLayoutPassword.error = "Kata sandi harus diisi"
                    binding.editTextKataSandi.requestFocus()
                    return@setOnClickListener
                }
                kataSandi.length < 6 -> {
                    binding.inputLayoutPassword.error = "Kata sandi minimal 6 karakter"
                    binding.editTextKataSandi.requestFocus()
                    return@setOnClickListener
                }
                noHp.isEmpty() -> {
                    binding.inputLayoutPhone.error = "Nomor telepon harus diisi"
                    binding.editTextHp.requestFocus()
                    return@setOnClickListener
                }
                noHp.length < 10 -> {
                    binding.inputLayoutPhone.error = "Nomor telepon minimal 10 digit"
                    binding.editTextHp.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    // Clear all errors
                    clearAllErrors()
                    // Add haptic feedback
                    binding.buttonRegist.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                    // Start registration
                    viewModel.register(name, email, kataSandi, noHp)
                }
            }
        }

        // Add text change listeners to clear errors
        binding.editTextNama.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) binding.inputLayoutNama.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) binding.inputLayoutEmail.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editTextKataSandi.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) binding.inputLayoutPassword.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editTextHp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) binding.inputLayoutPhone.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoadingState(isLoading)
        }

        viewModel.registerResult.observe(this) { result ->
            if (!isFinishing) {
                alertDialog?.dismiss()

                when (result) {
                    is Resource.Loading -> {
                        showLoadingState(true)
                        clearAllErrors()
                    }

                    is Resource.Success -> {
                        showLoadingState(false)
                        clearAllErrors()

                        // Success animation
                        animateSuccess {
                            // Show success message
                            showSuccessOverlay {
                                // pindah halaman setelah animasi sukses selesai (Navigate to OTP activity)
                                val intent = Intent(this@RegisterActivity, OtpActivity::class.java).apply {
                                    putExtra("email", result.data?.registerResult?.email)
                                    putExtra("token_verifikasi", result.data?.registerResult?.tokenVerifikasi)
                                }
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                finish()
                            }

                        }
                    }

                    is Resource.Error -> {
                        showLoadingState(false)
                        handleError(result.message)
                    }
                }
            }
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.buttonRegist.isEnabled = !isLoading

        // Disable all input fields during loading
        binding.editTextNama.isEnabled = !isLoading
        binding.editTextEmail.isEnabled = !isLoading
        binding.editTextKataSandi.isEnabled = !isLoading
        binding.editTextHp.isEnabled = !isLoading

        // Change button text during loading
        binding.buttonRegist.text = if (isLoading) "Mendaftar..." else getString(R.string.daftar)
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
                }, 1500) // biarkan success tampil 1,5 detik sebelum pindah
            }
            .start()
    }

    private fun handleError(message: String?) {
        val errorMessage = message ?: "Terjadi kesalahan"

        when {
            errorMessage.contains("Email sudah dipakai", ignoreCase = true) ||
                    errorMessage.contains("email already", ignoreCase = true) -> {
                binding.inputLayoutEmail.error = "Email sudah terdaftar"
                binding.editTextEmail.requestFocus()
            }
            errorMessage.contains("phone", ignoreCase = true) ||
                    errorMessage.contains("nomor", ignoreCase = true) -> {
                binding.inputLayoutPhone.error = "Nomor telepon sudah terdaftar"
                binding.editTextHp.requestFocus()
            }
            else -> {
                showErrorSnackbar(errorMessage)
            }
        }

        // Add shake animation for error
        shakeView(binding.mainContentCard)
    }

    private fun clearAllErrors() {
        binding.inputLayoutNama.error = null
        binding.inputLayoutEmail.error = null
        binding.inputLayoutPassword.error = null
        binding.inputLayoutPhone.error = null
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showErrorSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.error_red))
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    private fun animateSuccess(onComplete: () -> Unit) {
        val scaleX = ObjectAnimator.ofFloat(binding.buttonRegist, "scaleX", 1f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.buttonRegist, "scaleY", 1f, 1.1f, 1f)

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
        val shake = ObjectAnimator.ofFloat(view, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
        shake.duration = 600
        shake.start()
    }

    private fun setupCloudAnimations() {
        // Animate clouds with subtle floating effect
        val cloud1Animator = ObjectAnimator.ofFloat(binding.cloud1, "translationY", 0f, -20f, 0f)
        cloud1Animator.duration = 3000
        cloud1Animator.repeatCount = ObjectAnimator.INFINITE
        cloud1Animator.interpolator = AccelerateDecelerateInterpolator()
        cloud1Animator.start()

        val cloud2Animator = ObjectAnimator.ofFloat(binding.cloud2, "translationY", 0f, -15f, 0f)
        cloud2Animator.duration = 4000
        cloud2Animator.repeatCount = ObjectAnimator.INFINITE
        cloud2Animator.interpolator = AccelerateDecelerateInterpolator()
        cloud2Animator.startDelay = 1000
        cloud2Animator.start()

        val cloud3Animator = ObjectAnimator.ofFloat(binding.cloud3, "translationY", 0f, -10f, 0f)
        cloud3Animator.duration = 5000
        cloud3Animator.repeatCount = ObjectAnimator.INFINITE
        cloud3Animator.interpolator = AccelerateDecelerateInterpolator()
        cloud3Animator.startDelay = 2000
        cloud3Animator.start()

        // Add subtle alpha animation to clouds
        val cloud1Alpha = ObjectAnimator.ofFloat(binding.cloud1, "alpha", 0.3f, 0.6f, 0.3f)
        cloud1Alpha.duration = 6000
        cloud1Alpha.repeatCount = ObjectAnimator.INFINITE
        cloud1Alpha.start()

        val cloud2Alpha = ObjectAnimator.ofFloat(binding.cloud2, "alpha", 0.4f, 0.7f, 0.4f)
        cloud2Alpha.duration = 7000
        cloud2Alpha.repeatCount = ObjectAnimator.INFINITE
        cloud2Alpha.startDelay = 1500
        cloud2Alpha.start()

        val cloud3Alpha = ObjectAnimator.ofFloat(binding.cloud3, "alpha", 0.2f, 0.5f, 0.2f)
        cloud3Alpha.duration = 8000
        cloud3Alpha.repeatCount = ObjectAnimator.INFINITE
        cloud3Alpha.startDelay = 3000
        cloud3Alpha.start()
    }

    private fun playAnimation() {
        // Logo entrance animation
        val logoScale = ObjectAnimator.ofFloat(binding.logoKidsnesia, "scaleX", 0f, 1.2f, 1f)
        val logoScaleY = ObjectAnimator.ofFloat(binding.logoKidsnesia, "scaleY", 0f, 1.2f, 1f)
        val logoAlpha = ObjectAnimator.ofFloat(binding.logoKidsnesia, "alpha", 0f, 1f)

        val logoAnimSet = AnimatorSet().apply {
            playTogether(logoScale, logoScaleY, logoAlpha)
            duration = 800
            interpolator = OvershootInterpolator(1.2f)
        }

        // Card entrance animation
        val cardTranslation = ObjectAnimator.ofFloat(binding.mainContentCard, "translationY", 300f, 0f)
        val cardAlpha = ObjectAnimator.ofFloat(binding.mainContentCard, "alpha", 0f, 1f)

        val cardAnimSet = AnimatorSet().apply {
            playTogether(cardTranslation, cardAlpha)
            duration = 600
            interpolator = DecelerateInterpolator()
        }

        // Form fields stagger animation
        val formViews = listOf(
            binding.greeting,
            binding.subtitle,
            binding.inputLayoutNama,
            binding.inputLayoutEmail,
            binding.inputLayoutPassword,
            binding.inputLayoutPhone,
            binding.buttonRegist,
            binding.textView6
        )

        // ⬇️ Sembunyikan dulu sebelum animasi
        formViews.forEach { view ->
            view.alpha = 0f
            view.translationY = 50f
        }

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

        // Play all animations in sequence
        masterAnimator = AnimatorSet().apply {
            play(logoAnimSet).before(AnimatorSet().apply {
                playSequentially(animators)
            })
            start()
        }

        masterAnimator.start()

        // Simpan dan jalankan cloudAnimators
        cloudAnimators.clear()
        cloudAnimators.addAll(
            listOf(
                binding.cloud1 to 0L,
                binding.cloud2 to 300L,
                binding.cloud3 to 600L
            ).map { (cloud, delay) ->
                val alpha = ObjectAnimator.ofFloat(cloud, "alpha", 0f, cloud.alpha)
                val scaleX = ObjectAnimator.ofFloat(cloud, "scaleX", 0.5f, 1f)
                val scaleY = ObjectAnimator.ofFloat(cloud, "scaleY", 0.5f, 1f)

                AnimatorSet().apply {
                    playTogether(alpha, scaleX, scaleY)
                    duration = 1000
                    startDelay = delay
                    interpolator = OvershootInterpolator(0.8f)
                }
            }
        )

        // Jalankan cloud animators
        cloudAnimators.forEach { it.start() }
    }

    override fun onDestroy() {
        masterAnimator.cancel()
        cloudAnimators.forEach { it.cancel() }
        alertDialog?.dismiss()
        alertDialog = null
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

//    private fun showSuccessSnackbar(message: String) {
//        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
//        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.success_green))
//        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white))
//        snackbar.show()
//    }