package com.naufal.kidsnesia.purchase.presentation.transaksi.membership

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.databinding.ActivityTransaksiMembershipBinding
import com.naufal.kidsnesia.main_features.presentation.video.VideoFragment
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMembershipRequest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class TransaksiMembershipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransaksiMembershipBinding
    private val viewModel: TransaksiMembershipViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaksiMembershipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val bankList = listOf("BNI", "BSI", "DANA", "OVO", "GoPay")
        val adapterBank = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bankList)
        binding.textNamaBank.setAdapter(adapterBank)
        binding.textNamaBank.setOnClickListener { binding.textNamaBank.showDropDown() }

        binding.buttonKonfirmasi.setOnClickListener {
            val namaBank = binding.textNamaBank.text.toString()
            if (namaBank.isEmpty()) {
                Toast.makeText(this, "Pilih bank terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.pilihBankMembership(namaBank)
        }

        viewModel.pilihBankResult.observe(this) { result ->
            result.onSuccess { response ->
                if (response.error == false) {
                    val idPembayaranMembership = response.data?.idPembayaranMembership
                    val intent = Intent(this, UploadMemberActivity::class.java)
                    intent.putExtra("idPembayaranMembership", idPembayaranMembership.toString())
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, response.message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }.onFailure {
                Toast.makeText(this, "Gagal mengirim data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}