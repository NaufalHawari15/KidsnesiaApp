package com.naufal.kidsnesia.purchase.presentation.cart.merch

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
import com.naufal.kidsnesia.databinding.FragmentMerchCartBinding
import com.naufal.kidsnesia.purchase.presentation.cart.CartViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MerchCartFragment : Fragment() {
    private var _binding: FragmentMerchCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel = get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getListCartMerch()
        observeMerchCart()
    }

    private fun observeMerchCart() {
        lifecycleScope.launch {
            viewModel.cartMerch.collect { resource ->
                when (resource) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val list = resource.data?.listCartMerch?.filterNotNull() ?: emptyList()

                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()) // <-- Tambahkan ini
                        binding.recyclerView.adapter = MerchCartAdapter(list)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Gagal: ${resource.message}", Toast.LENGTH_SHORT).show()
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