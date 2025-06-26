package com.naufal.kidsnesia.purchase.presentation.cart.merch

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getListCartMerch()
        }

        viewModel.getListCartMerch()
        observeMerchCart()
    }

    private fun observeMerchCart() {
        lifecycleScope.launch {
            viewModel.cartMerch.collect { resource ->
                binding.swipeRefreshLayout.isRefreshing = false
                when (resource) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val list = resource.data?.listCartMerch?.filterNotNull() ?: emptyList()
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerView.adapter = MerchCartAdapter(list) { idPembelianMerch ->
                            val intent = Intent(requireContext(), DetailMerchCartActivity::class.java)
                            intent.putExtra("idPembelianMerch", idPembelianMerch)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Gagal: ${resource.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun refreshCart() {
        viewModel.getListCartEvent()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}