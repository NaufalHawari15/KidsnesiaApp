package com.naufal.kidsnesia.purchase.presentation.cart.event

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.ActivityDetailEventCartBinding
import com.naufal.kidsnesia.purchase.data.source.remote.response.ItemsEvent
import com.naufal.kidsnesia.purchase.presentation.transaksi.event.TransaksiActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.get

class DetailEventCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventCartBinding
    private val viewModel: DetailEventCartViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val idPembelianEvent = intent.getStringExtra("idPembelianEvent") ?: return

        // Get detail cart by ID
        viewModel.getDetailCart(idPembelianEvent)

        // Untuk menyimpan daftar itemsEvent yang akan dikirim ke checkout
        var itemsEvent: List<ItemsEvent> = emptyList()

        // Observe detail cart
        viewModel.detailCart.onEach { resource ->
            when (resource) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val cartItem = resource.data?.cartEventItem?.firstOrNull()

                    binding.tvEventName.text = cartItem?.namaEvent ?: "-"
                    binding.tvEventDateValue.text = "${cartItem?.jadwalEvent ?: "-"} WIB"
                    binding.tvEventPriceValue.text = "Rp ${cartItem?.hargaEvent ?: 0}"
                    binding.tvEventJumlahValue.text = "${cartItem?.jumlahTiket ?: 0} tiket"
                    binding.tvEventTotalValue.text = "Rp ${cartItem?.subtotalEvent ?: 0}"

                    Glide.with(this)
                        .load(cartItem?.fotoEvent)
                        .into(binding.ivEventImage)

                    // Siapkan list item untuk dikirimkan saat checkout
                    itemsEvent = resource.data?.cartEventItem?.mapNotNull {
                        if (it?.idEvent != null && it.jumlahTiket != null) {
                            ItemsEvent(it.idEvent, it.jumlahTiket)
                        } else null
                    } ?: emptyList()

                    // Tombol kembali
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

        // Observe hasil checkout
        viewModel.checkoutResult.onEach { result ->
            when (result) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Checkout berhasil!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, TransaksiActivity::class.java).apply {
                        putExtra("idPembelianEvent", result.data?.pembelianEventResponse?.idPembelianEvent.toString())
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
            if (itemsEvent.isNotEmpty()) {
                viewModel.createCheckout(idPembelianEvent, itemsEvent)
            } else {
                Toast.makeText(this, "Data keranjang tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
