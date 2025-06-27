package com.naufal.kidsnesia.main_features.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.FragmentDashboardBinding
import com.naufal.kidsnesia.main_features.presentation.detail.detail_merch.DetailMerchActivity
import com.naufal.kidsnesia.purchase.presentation.cart.CartActivity
import org.koin.android.ext.android.get

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel = get()
    private lateinit var productAdapter: MerchandiseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageCart.setOnClickListener {
            val intent = Intent(requireContext(), CartActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
        observeProducts()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMerchandise.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeProducts() {
        viewModel.listProduct().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Tampilkan loading indicator jika perlu
                }
                is Resource.Success -> {
                    val productList = resource.data?.listMerchandise?.filterNotNull() ?: emptyList()
                    productAdapter = MerchandiseAdapter(productList) { product ->
                        val intent = Intent(requireContext(), DetailMerchActivity::class.java).apply {
                            putExtra(DetailMerchActivity.EXTRA_PRODUCT_ID, product.idMerchandise.toString()) // Konversi ke String
                        }
                        startActivity(intent)
                    }
                    binding.recyclerViewMerchandise.adapter = productAdapter
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Gagal memuat produk", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}