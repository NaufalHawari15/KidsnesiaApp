package com.naufal.kidsnesia.main_features.presentation.detail


import android.content.Intent
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
import com.naufal.kidsnesia.auth.data.source.local.UserPreference
import com.naufal.kidsnesia.databinding.ActivityDetailBinding
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailEvent
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.ItemsEventItem
import kotlinx.coroutines.flow.first

import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idEvent = intent.getStringExtra(EXTRA_EVENT_ID)
        if (idEvent != null) {
            viewModel.getDetailEvents(idEvent)
            observeDetail()
        } else {
            Toast.makeText(this, "Event ID tidak ditemukan", Toast.LENGTH_SHORT).show()
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
            viewModel.eventDetail.collect { resource ->
                when (resource) {
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        showLoading(false)
                        resource.data?.detailEvent?.let { displayEventDetails(it) }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Toast.makeText(this@DetailActivity, "Gagal: ${resource.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun displayEventDetails(event: DetailEvent) {
        binding.apply {
            tvEventName.text = event.namaEvent ?: "Nama tidak tersedia"
            tvEventDescription.text = event.deskripsiEvent ?: "Deskripsi tidak tersedia"
            tvEventDate.text = event.jadwalEvent ?: "Jadwal tidak tersedia"
            tvEventPrice.text = "Rp ${event.hargaEvent ?: 0}"
            tvEventQuota.text = "Kuota: ${event.kuota ?: 0}"

            val firstImage = event.fotoKegiatan?.firstOrNull()

            Glide.with(this@DetailActivity)
                .load(firstImage)
                .into(ivEventImage)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
