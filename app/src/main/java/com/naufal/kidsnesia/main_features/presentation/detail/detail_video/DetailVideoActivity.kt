package com.naufal.kidsnesia.main_features.presentation.detail.detail_video

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


class DetailVideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailVideoBinding
    private val viewModel: DetailVideoViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                        binding.tvTitle.text = video?.judulVideo
                        binding.tvDescription.text = video?.deskripsiVideo

                        Glide.with(this@DetailVideoActivity)
                            .load(video?.thumbnail)
                            .into(binding.ivThumbnail)
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

