package com.naufal.kidsnesia.purchase.presentation.transaksi.event

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.naufal.kidsnesia.MainActivity
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.ActivityTransaksiBinding
import com.naufal.kidsnesia.purchase.presentation.cart.CartActivity
import com.naufal.kidsnesia.util.FileUtil
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.get
import java.io.File

class TransaksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransaksiBinding
    private val viewModel: TransaksiViewModel = get()

    private var selectedBuktiUri: Uri? = null // ⬅️ GLOBAL VARIABLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // 1. Dropdown bank
        val bankList = listOf("BNI", "BSI", "DANA", "OVO", "GoPay")
        val adapterBank = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bankList)
        binding.textNamaBank.setAdapter(adapterBank)
        binding.textNamaBank.setOnClickListener {
            binding.textNamaBank.showDropDown()
        }

        // 2. Upload bukti transfer
        binding.buttonUploadBukti.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, 1001)
        }

        // 3. Tombol konfirmasi
        binding.buttonKonfirmasi.setOnClickListener {
            val namaBank = binding.textNamaBank.text.toString()
            val idPembelianEvent = intent.getStringExtra("idPembelianEvent") ?: ""

            if (namaBank.isBlank() || selectedBuktiUri == null) {
                Toast.makeText(this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.konfirmasiPembayaran(
                    idPembayaranEvent = idPembelianEvent,
                    namaBank = namaBank,
                    buktiUri = selectedBuktiUri!!,
                    context = this
                )
            }
        }

        // 4. Observe loading & success state
        viewModel.loadingState.observe(this) { isLoading ->
            binding.buttonKonfirmasi.isEnabled = !isLoading
        }

        viewModel.success.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Pembayaran berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CartActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Gagal melakukan konfirmasi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 5. Tangkap gambar hasil upload
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            selectedBuktiUri = data?.data
            if (selectedBuktiUri != null) {
                binding.imagePreview.visibility = View.VISIBLE
                binding.imagePreview.setImageURI(selectedBuktiUri)
                Toast.makeText(this, "Bukti transfer dipilih", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

