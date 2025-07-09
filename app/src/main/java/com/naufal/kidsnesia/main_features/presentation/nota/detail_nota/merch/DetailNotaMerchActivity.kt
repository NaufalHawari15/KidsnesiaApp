package com.naufal.kidsnesia.main_features.presentation.nota.detail_nota.merch

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.ActivityDetailNotaMerchBinding
import org.koin.android.ext.android.get

class DetailNotaMerchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNotaMerchBinding
    private val viewModel: DetailNotaMerchViewModel = get()
    private val adapter = DetailNotaMerchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNotaMerchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()

        val id = intent.getStringExtra("idPembelianMerch") ?: return
        viewModel.getDetailNotaMerch(id)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        observeData()
    }

    private fun setupRecyclerView() {
        binding.rvDetailMerch.layoutManager = LinearLayoutManager(this)
        binding.rvDetailMerch.adapter = adapter
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.notaMerch.collect { result ->
                when (result) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        val nota = result.data
                        if (nota != null) {
                            binding.tvNama.text = "Nama: ${nota.namaPelanggan ?: "-"}"
                            binding.tvEmail.text = "Email: ${nota.emailPelanggan ?: "-"}"
                            binding.tvTelepon.text = "Telepon: ${nota.teleponPelanggan ?: "-"}"
                            binding.tvTanggal.text = "Tanggal: ${nota.tanggalPembelianMerch ?: "-"}"
                            val status = nota.statusPembayaranMerch ?: "-"
                            binding.tvStatus.text = "Status: $status"

                            when (status.lowercase()) {
                                "berhasil" -> binding.tvStatus.setTextColor(getColor(R.color.hijau_status))
                                "gagal" -> binding.tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))
                                "menunggu verifikasi" -> binding.tvStatus.setTextColor(getColor(R.color.kuning_status))
                                else -> binding.tvStatus.setTextColor(getColor(android.R.color.darker_gray)) // default
                            }
                            binding.tvTotal.text = "Total: Rp${nota.totalPembelianMerch ?: 0}"

                            val listDetail = nota.detailMerch?.filterNotNull() ?: emptyList()
                            adapter.submitList(listDetail)
                        } else {
                            Toast.makeText(this@DetailNotaMerchActivity, "Data nota kosong", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(this@DetailNotaMerchActivity, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}