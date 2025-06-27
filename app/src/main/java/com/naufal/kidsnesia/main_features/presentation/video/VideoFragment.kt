package com.naufal.kidsnesia.main_features.presentation.video

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.FragmentVideoBinding
import com.naufal.kidsnesia.main_features.presentation.detail.detail_video.DetailVideoActivity
import org.koin.android.ext.android.get

class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VideoViewModel = get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = VideoAdapter(emptyList()) { idVideo ->
            val intent = Intent(requireContext(), DetailVideoActivity::class.java)
            intent.putExtra("idVideo", idVideo)
            startActivity(intent)
        }

        binding.rvVideos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVideos.adapter = adapter

        viewModel.fetchVideoList()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchVideoList()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.videoState.collect { state ->
                binding.swipeRefresh.isRefreshing = state is Resource.Loading
                when (state) {
                    is Resource.Success -> {
                        adapter.updateData(state.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


