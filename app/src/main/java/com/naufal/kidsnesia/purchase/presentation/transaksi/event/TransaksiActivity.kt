package com.naufal.kidsnesia.purchase.presentation.transaksi.event

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.naufal.kidsnesia.MainActivity
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.ActivityTransaksiBinding
import org.koin.android.ext.android.get
import java.io.File

class TransaksiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransaksiBinding
    private val viewModel: TransaksiViewModel = get()
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val idPembelianEvent = intent.getStringExtra("idPembelianEvent") ?: return

        binding.buttonKonfirmasi.setOnClickListener {
            val namaBank = binding.textNamaBank.text.toString()
            if (namaBank.isBlank()) {
                Toast.makeText(this, "Nama bank harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (imageFile == null) {
                Toast.makeText(this, "Upload bukti pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.konfirmasiPembayaran(idPembelianEvent, namaBank, imageFile!!)
        }

        viewModel.result.observe(this) { result ->
            when (result) {
                is Resource.Loading -> binding.buttonKonfirmasi.isEnabled = false
                is Resource.Success -> {
                    Toast.makeText(this, "Pembayaran berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java)) // asumsi dashboard di MainActivity
                    finish()
                }
                is Resource.Error -> {
                    binding.buttonKonfirmasi.isEnabled = true
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
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


}