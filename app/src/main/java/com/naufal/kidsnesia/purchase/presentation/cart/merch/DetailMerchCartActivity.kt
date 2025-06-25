package com.naufal.kidsnesia.purchase.presentation.cart.merch

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.ActivityDetailMerchCartBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.get

class DetailMerchCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMerchCartBinding
    private val viewModel: DetailMerchCartViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMerchCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val idPembelianMerch = intent.getStringExtra("idPembelianMerch") ?: return
        viewModel.getDetailCartMerch(idPembelianMerch)

        viewModel.detailMerchCart.onEach { resource ->
            when(resource) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val cartItem = resource.data?.cartMerchItem?.firstOrNull()

                    binding.tvMerchName.text = cartItem?.namaMerch ?: "-"
                    binding.tvMerchPriceValue.text = "Rp ${cartItem?.hargaMerch ?: 0}"
                    binding.tvMerchJumlahValue.text = "${cartItem?.jumlahMerch ?: 0} tiket"
                    binding.tvMerchTotalValue.text = "Rp ${cartItem?.subtotalMerch ?: 0}"

                    Glide.with(this)
                        .load(cartItem?.fotoMerchandise)
                        .into(binding.ivMerchImage)

                    binding.toolbar.setNavigationOnClickListener {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Gagal: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(lifecycleScope)
    }
}