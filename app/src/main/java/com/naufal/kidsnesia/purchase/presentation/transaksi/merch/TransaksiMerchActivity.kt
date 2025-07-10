package com.naufal.kidsnesia.purchase.presentation.transaksi.merch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.databinding.ActivityTransaksiMerchBinding
import com.naufal.kidsnesia.purchase.presentation.cart.CartActivity
import org.koin.android.ext.android.get

class TransaksiMerchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransaksiMerchBinding
    private val viewModel: TransaksiMerchViewModel = get()

    private var selectedBuktiUri: Uri? = null // ⬅️ GLOBAL VARIABLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaksiMerchBinding.inflate(layoutInflater)
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
            val idPembelianMerch = intent.getStringExtra("idPembelianMerch") ?: ""

            if (namaBank.isBlank() || selectedBuktiUri == null) {
                Toast.makeText(this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.konfirmasiPembayaran(
                    idPembayaranMerch = idPembelianMerch,
                    namaBank = namaBank,
                    buktiUri = selectedBuktiUri!!,
                    context = this
                )
            }
        }

        // 4. Observe loading & success state
        viewModel.loadingState.observe(this) { isLoading ->
            binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonKonfirmasi.isEnabled = !isLoading
        }

        viewModel.success.observe(this) { success ->
            if (success) {
                binding.successOverlay.visibility = View.VISIBLE

                // Delay agar user sempat lihat overlay
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, CartActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                    finish()
                }, 2000)
            } else {
                Toast.makeText(this, "Gagal memproses pembelian", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 5. Tangkap gambar hasil upload
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            selectedBuktiUri = data?.data
            if (selectedBuktiUri != null) {
                binding.cardPreview.visibility = View.VISIBLE
                binding.imagePreview.setImageURI(selectedBuktiUri)
                Toast.makeText(this, "Bukti transfer dipilih", Toast.LENGTH_SHORT).show()
            }
        }
    }
}