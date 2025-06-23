package com.naufal.kidsnesia.main_features.presentation.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.ActivityDetailMerchBinding
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailMerchandise
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class DetailMerchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMerchBinding
    private val viewModel: DetailMerchViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMerchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idMerch = intent.getStringExtra(EXTRA_PRODUCT_ID)
        if (idMerch != null) {
            viewModel.getDetailProduct(idMerch)
            observeDetail()
        }
        else {
            Toast.makeText(this, "Product ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
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

    private fun observeDetail() {
        lifecycleScope.launch {
            viewModel.productDetail.collect { resource ->
                when (resource) {
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        showLoading(false)
                        resource.data?.detailMerchandise?.let { displayProductDetail(it) }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Toast.makeText(this@DetailMerchActivity, "Gagal: ${resource.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun displayProductDetail(product: DetailMerchandise) {
        binding.apply {
            tvMerchName.text = product.namaMerchandise ?: "Nama Produk tidak tersedia"
            tvMerchPrice.text = "Rp ${product.hargaMerchandise ?: 0}"
            tvMerchStock.text = "Stok: ${product.stok ?: 0}"
            tvMerchDescription.text = product.deskripsiMerchandise ?: "Deskripsi Produk tidak tersedia"

            Glide.with(this@DetailMerchActivity)
                .load(product.fotoMerchandise)
                .into(ivMerchImage)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }
}