package com.naufal.kidsnesia.main_features.presentation.detail.detail_video

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.ActivityDetailVideoBinding
import org.koin.android.ext.android.get
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailVideo
import androidx.core.net.toUri

class DetailVideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailVideoBinding
    private val viewModel: DetailVideoViewModel = get()
    private var currentVideoData: DetailVideo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val idVideo = intent.getStringExtra("idVideo") ?: return

        viewModel.fetchDetailVideo(idVideo)

        lifecycleScope.launchWhenStarted {
            viewModel.detailVideoState.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.scrollContent.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.scrollContent.visibility = View.VISIBLE

                        val video = state.data
                        currentVideoData = video

                        binding.tvTitle.text = video?.judulVideo
                        binding.tvDescription.text = video?.deskripsiVideo

                        // Load thumbnail dengan play overlay
                        Glide.with(this@DetailVideoActivity)
                            .load(video?.thumbnail)
                            .placeholder(R.drawable.ic_maskot_kidsnesia) // Tambahkan placeholder jika ada
                            .error(R.drawable.ic_image_not_supported) // Tambahkan error drawable jika ada
                            .into(binding.ivThumbnail)

                        // Setup click listener untuk thumbnail
                        setupThumbnailClickListener()
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@DetailVideoActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        setupView()
    }

    private fun setupThumbnailClickListener() {
        binding.ivThumbnail.setOnClickListener {
            currentVideoData?.let { video ->
                video.filePath?.let { videoUrl ->
                    playVideo(videoUrl, video.judulVideo ?: "Video")
                } ?: run {
                    Toast.makeText(this, "URL video tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Tambahkan visual indicator bahwa thumbnail bisa diklik
        binding.ivThumbnail.apply {
            isClickable = true
            isFocusable = true
            // Tambahkan play button overlay jika diperlukan
            foreground = ContextCompat.getDrawable(this@DetailVideoActivity, R.drawable.play_button_overlay)
        }
    }

    private fun playVideo(videoUrl: String, videoTitle: String) {
        val intent = VideoPlayerActivity.newIntent(this, videoUrl, videoTitle)
        startActivity(intent)
    }

    private fun setupView() {
        // Tampilkan status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        // Sembunyikan action bar saja (jika ada)
        supportActionBar?.hide()
    }
}

