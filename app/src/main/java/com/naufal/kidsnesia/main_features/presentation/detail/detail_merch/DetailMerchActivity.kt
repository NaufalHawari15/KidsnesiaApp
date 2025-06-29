package com.naufal.kidsnesia.main_features.presentation.detail.detail_merch

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.ActivityDetailMerchBinding
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailMerchandise
import com.naufal.kidsnesia.ui.bottomsheet.merch.MerchBottomSheet
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

        lifecycleScope.launch {
            viewModel.loadingState.collect { isLoading ->
                binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        setupView()
    }

    private fun setupView() {
        // Tampilkan status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        // Sembunyikan action bar saja (jika ada)
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

        binding.buttonBeli.setOnClickListener {
            val currentMerch = viewModel.productDetail.value
            if (currentMerch is Resource.Success) {
                val merch = currentMerch.data?.detailMerchandise
                if (merch != null) {
                    val bottomSheet = MerchBottomSheet(
                        imageUrl = merch.fotoMerchandise,
                        maxMerch = merch.stok ?: 0
                    ) { jumlahProduk, onDone, onError ->
                        viewModel.createCartMerch(
                            idMerch = merch.idMerchandise ?: 0,
                            jumlahProduk = jumlahProduk,
                            onSuccess = {
                                Toast.makeText(this, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                                onDone()
                            },
                            onError = { err ->
                                onError(err)
                            }
                        )
                    }
                    bottomSheet.show(supportFragmentManager, "MerchBottomSheet")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }
}