package com.naufal.kidsnesia.main_features.presentation.detail.detail_video

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.naufal.kidsnesia.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private var exoPlayer: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L

    companion object {
        const val EXTRA_VIDEO_URL = "extra_video_url"
        const val EXTRA_VIDEO_TITLE = "extra_video_title"
        private const val KEY_PLAYBACK_POSITION = "playbackPosition"
        private const val KEY_CURRENT_ITEM = "currentItem"
        private const val KEY_PLAY_WHEN_READY = "playWhenReady"

        fun newIntent(context: Context, videoUrl: String, videoTitle: String): Intent {
            return Intent(context, VideoPlayerActivity::class.java).apply {
                putExtra(EXTRA_VIDEO_URL, videoUrl)
                putExtra(EXTRA_VIDEO_TITLE, videoTitle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUrl = intent.getStringExtra(EXTRA_VIDEO_URL) ?: return finish()
        val videoTitle = intent.getStringExtra(EXTRA_VIDEO_TITLE) ?: "Video"

        // Restore playback state if available
        savedInstanceState?.let {
            playbackPosition = it.getLong(KEY_PLAYBACK_POSITION, 0L)
            currentItem = it.getInt(KEY_CURRENT_ITEM, 0)
            playWhenReady = it.getBoolean(KEY_PLAY_WHEN_READY, true)
        }

        setupView()
        binding.tvVideoTitle.text = videoTitle
        initializePlayer(videoUrl)
        setupClickListeners()
    }

    private fun setupView() {
        supportActionBar?.hide()

        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Selalu sembunyikan status bar
            if (isLandscape) {
                // Landscape: sembunyikan status + navigation bar
                window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            } else {
                // Portrait: sembunyikan hanya status bar
                window.insetsController?.hide(WindowInsets.Type.statusBars())
                window.insetsController?.show(WindowInsets.Type.navigationBars())
            }
        } else {
            // Android < R (Android 11)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = if (isLandscape) {
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            } else {
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer(videoUrl: String) {
        val dataSourceFactory = DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)

        exoPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .also { player ->
                binding.playerView.player = player

                // Auto-hide control setelah 3 detik
                binding.playerView.controllerShowTimeoutMs = 3000

                // Kontrol elemen custom ikut bersembunyi
                binding.playerView.setControllerVisibilityListener(
                    PlayerView.ControllerVisibilityListener { visibility ->
                        val isVisible = visibility == View.VISIBLE
                        val alphaValue = if (isVisible) 1f else 0f

                        binding.tvVideoTitle.animate()
                            .alpha(alphaValue)
                            .setDuration(200)
                            .withEndAction {
                                binding.tvVideoTitle.visibility = if (isVisible) View.VISIBLE else View.GONE
                            }
                            .start()

                        binding.btnClose.animate()
                            .alpha(alphaValue)
                            .setDuration(200)
                            .withEndAction {
                                binding.btnClose.visibility = if (isVisible) View.VISIBLE else View.GONE
                            }
                            .start()

                        binding.btnFullscreen.animate()
                            .alpha(alphaValue)
                            .setDuration(200)
                            .withEndAction {
                                binding.btnFullscreen.visibility = if (isVisible) View.VISIBLE else View.GONE
                            }
                            .start()
                    }
                )

//                // Tap layar untuk tampilkan controller
//                binding.playerView.setOnClickListener {
//                    binding.playerView.showController()
//                }

                val mediaItem = MediaItem.fromUri(videoUrl)
                player.setMediaItem(mediaItem)
                player.playWhenReady = playWhenReady
                player.seekTo(currentItem, playbackPosition)

                player.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_BUFFERING -> binding.progressBar.visibility = View.VISIBLE
                            Player.STATE_READY,
                            Player.STATE_IDLE -> binding.progressBar.visibility = View.GONE
                            Player.STATE_ENDED -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@VideoPlayerActivity,
                                    "Video selesai",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@VideoPlayerActivity,
                            "Error: ${error.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

                player.prepare()
            }
    }

    private fun setupClickListeners() {
        binding.btnClose.setOnClickListener { finish() }
        binding.btnFullscreen.setOnClickListener { toggleFullscreen() }
    }

    private fun toggleFullscreen() {
        requestedOrientation = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        else
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        exoPlayer?.let { player ->
            outState.putLong(KEY_PLAYBACK_POSITION, player.currentPosition)
            outState.putInt(KEY_CURRENT_ITEM, player.currentMediaItemIndex)
            outState.putBoolean(KEY_PLAY_WHEN_READY, player.playWhenReady)
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT > 23 && exoPlayer == null) {
            val videoUrl = intent.getStringExtra(EXTRA_VIDEO_URL) ?: return
            initializePlayer(videoUrl)
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT <= 23 || exoPlayer == null) {
            val videoUrl = intent.getStringExtra(EXTRA_VIDEO_URL) ?: return
            initializePlayer(videoUrl)
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            currentItem = player.currentMediaItemIndex
            playWhenReady = player.playWhenReady
            player.release()
        }
        exoPlayer = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        setupView() // ← tambahkan ini agar immersive mode aktif lagi setelah rotasi

        binding.tvVideoTitle.visibility =
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) View.VISIBLE else View.GONE

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Hanya sembunyikan judul, tombol tetap ditampilkan
            binding.tvVideoTitle.visibility = View.VISIBLE
        } else {
            binding.tvVideoTitle.visibility = View.VISIBLE
        }
    }
}
