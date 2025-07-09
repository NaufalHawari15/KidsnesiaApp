package com.naufal.kidsnesia.main_features.presentation.nota.nota_event

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
import com.naufal.kidsnesia.databinding.FragmentNotaEventBinding
import com.naufal.kidsnesia.main_features.presentation.nota.NotaViewModel
import com.naufal.kidsnesia.main_features.presentation.nota.detail_nota.event.DetailNotaEventActivity
import org.koin.android.ext.android.get

class NotaEventFragment : Fragment() {

    private var _binding: FragmentNotaEventBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotaViewModel = get()
    private lateinit var adapter: NotaEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotaEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NotaEventAdapter { item ->
            val intent = Intent(requireContext(), DetailNotaEventActivity::class.java)
            intent.putExtra("idPembelianEvent", item.idPembelianEvent?.toString() ?: return@NotaEventAdapter)
            startActivity(intent)
        }

        // Pull-to-refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getListNotaMerch()
        }

        binding.rvNotaEvent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotaEvent.adapter = adapter

        observeData()
        viewModel.getListNotaEvent()
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.notaEvent.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefreshLayout.isRefreshing = false
                        adapter.submitList(result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefreshLayout.isRefreshing = false
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun refreshCart() {
        viewModel.getListNotaEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

