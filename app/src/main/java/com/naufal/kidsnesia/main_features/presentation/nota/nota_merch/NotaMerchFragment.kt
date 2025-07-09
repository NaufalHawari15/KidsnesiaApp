package com.naufal.kidsnesia.main_features.presentation.nota.nota_merch

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
import com.naufal.kidsnesia.databinding.FragmentNotaMerchBinding
import com.naufal.kidsnesia.main_features.presentation.nota.NotaViewModel
import com.naufal.kidsnesia.main_features.presentation.nota.detail_nota.event.DetailNotaEventActivity
import com.naufal.kidsnesia.main_features.presentation.nota.detail_nota.merch.DetailNotaMerchActivity
import com.naufal.kidsnesia.main_features.presentation.nota.nota_event.NotaEventAdapter
import org.koin.android.ext.android.get

class NotaMerchFragment : Fragment() {

    private var _binding: FragmentNotaMerchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotaViewModel = get()
    private lateinit var adapter: NotaMerchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotaMerchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NotaMerchAdapter { item ->
            val intent = Intent(requireContext(), DetailNotaMerchActivity::class.java)
            intent.putExtra("idPembelianMerch", item.idPembelianMerch?.toString() ?: return@NotaMerchAdapter)
            startActivity(intent)
        }

        // Pull-to-refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getListNotaMerch()
        }

        binding.rvNotaMerch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotaMerch.adapter = adapter

        observeData()
        viewModel.getListNotaMerch()
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.notaMerch.collect { result ->
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
        viewModel.getListNotaMerch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
