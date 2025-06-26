package com.naufal.kidsnesia.purchase.presentation.cart.merch

import android.content.Intent
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
import com.naufal.kidsnesia.purchase.data.source.remote.response.ItemsEvent
import com.naufal.kidsnesia.purchase.data.source.remote.response.ItemsMerch
import com.naufal.kidsnesia.purchase.presentation.transaksi.event.TransaksiActivity
import com.naufal.kidsnesia.purchase.presentation.transaksi.merch.TransaksiMerchActivity
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

        var itemsMerch: List<ItemsMerch> = emptyList()

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

                    itemsMerch = resource.data?.cartMerchItem?.mapNotNull {
                        if (it?.idMerch != null && it.jumlahMerch != null) {
                            ItemsMerch(it.idMerch, it.jumlahMerch)
                        } else null
                    } ?: emptyList()

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

        viewModel.checkoutMerchResult.onEach { result ->
            when (result) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Checkout berhasil!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, TransaksiMerchActivity::class.java).apply {
                        putExtra("idPembelianMerch", result.data?.pembelianMerchResponse?.idPembelianMerch.toString())
                    }
                    startActivity(intent)
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Checkout gagal: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(lifecycleScope)

        // Tombol Lanjutkan Pemesanan
        binding.buttonBeli.setOnClickListener {
            if (itemsMerch.isNotEmpty()) {
                viewModel.createCheckoutMerch(idPembelianMerch, itemsMerch)
            } else {
                Toast.makeText(this, "Data keranjang tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }
}