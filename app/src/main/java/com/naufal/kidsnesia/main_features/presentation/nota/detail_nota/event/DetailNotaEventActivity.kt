package com.naufal.kidsnesia.main_features.presentation.nota.detail_nota.event

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
import com.naufal.kidsnesia.databinding.ActivityDetailNotaEventBinding
import org.koin.android.ext.android.get

class DetailNotaEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNotaEventBinding
    private val viewModel: DetailNotaEventViewModel = get()
    private val adapter = DetailNotaEventAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNotaEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()

        val id = intent.getStringExtra("idPembelianEvent") ?: return
        viewModel.getDetailNotaEvent(id)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        observeData()
    }

    private fun setupRecyclerView() {
        binding.rvDetailEvent.layoutManager = LinearLayoutManager(this)
        binding.rvDetailEvent.adapter = adapter
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.notaEvent.collect { result ->
                when (result) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        val nota = result.data
                        if (nota != null) {
                            binding.tvNama.text = "Nama: ${nota.namaPelanggan ?: "-"}"
                            binding.tvEmail.text = "Email: ${nota.emailPelanggan ?: "-"}"
                            binding.tvTelepon.text = "Telepon: ${nota.teleponPelanggan ?: "-"}"
                            binding.tvTanggal.text = "Tanggal: ${nota.tanggalPembelianEvent ?: "-"}"
                            val status = nota.statusPembayaranEvent ?: "-"
                            binding.tvStatus.text = "Status: $status"

                            when (status.lowercase()) {
                                "berhasil" -> binding.tvStatus.setTextColor(getColor(R.color.hijau_status))
                                "gagal" -> binding.tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))
                                "menunggu verifikasi" -> binding.tvStatus.setTextColor(getColor(R.color.kuning_status))
                                else -> binding.tvStatus.setTextColor(getColor(android.R.color.darker_gray)) // default
                            }
                            binding.tvTotal.text = "Total: Rp${nota.totalPembelianEvent ?: 0}"

                            val listDetail = nota.detailEvent?.filterNotNull() ?: emptyList()
                            adapter.submitList(listDetail)
                        } else {
                            Toast.makeText(this@DetailNotaEventActivity, "Data nota kosong", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(this@DetailNotaEventActivity, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}