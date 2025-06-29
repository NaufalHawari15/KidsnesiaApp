package com.naufal.kidsnesia.purchase.presentation.transaksi.membership

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.naufal.kidsnesia.MainActivity
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.databinding.ActivityUploadMemberBinding
import com.naufal.kidsnesia.main_features.presentation.video.VideoFragment
import com.naufal.kidsnesia.util.FileUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.get
import java.io.File

class UploadMemberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadMemberBinding
    private val viewModel: UploadMemberViewModel = get()

    private var selectedImageFile: File? = null
    private lateinit var idPembayaranMembership: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        idPembayaranMembership = intent.getStringExtra("idPembayaranMembership") ?: ""

        binding.buttonUploadBukti.setOnClickListener {
            pickImage()
        }

        binding.buttonKonfirmasi.setOnClickListener {
            selectedImageFile?.let { file ->
                viewModel.uploadBuktiMembership(idPembayaranMembership, file)
            } ?: Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            binding.buttonKonfirmasi.isEnabled = !isLoading
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.uploadResult.observe(this) { response ->
            if (response?.error == false) {
                Toast.makeText(this, "Upload berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("navigateTo", "video")
                startActivity(intent)
                finishAffinity()
            } else {
                Toast.makeText(this, "Upload gagal: ${response?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            val uri = data.data ?: return
            selectedImageFile = FileUtil.from(this, uri)
            binding.imagePreview.apply {
                setImageURI(uri)
                visibility = View.VISIBLE
            }
        }
    }
}