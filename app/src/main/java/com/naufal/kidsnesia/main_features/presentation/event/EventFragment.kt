package com.naufal.kidsnesia.main_features.presentation.event

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.FragmentEventBinding
import com.naufal.kidsnesia.main_features.presentation.detail.DetailActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventViewModel = get()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeEvents()

        // Panggil fungsi untuk mengambil data event
        viewModel.getEvents()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(emptyList()) { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_EVENT_ID, event.idEvent.toString()) // Konversi ke String
            }
            startActivity(intent)
        }

        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewModel.eventList.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE

                        val events = resource.data?.listEvent?.filterNotNull() ?: emptyList()
                        eventAdapter = EventAdapter(events) { event ->
                            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                                putExtra(DetailActivity.EXTRA_EVENT_ID, event.idEvent.toString()) // Konversi ke String
                            }
                            startActivity(intent)
                        }
                        binding.rvEvents.adapter = eventAdapter
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.apply {
                            visibility = View.VISIBLE
                            text = resource.message ?: "Terjadi kesalahan"
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

