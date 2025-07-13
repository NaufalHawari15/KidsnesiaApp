package com.naufal.kidsnesia.main_features.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.databinding.FragmentDashboardBinding
import com.naufal.kidsnesia.main_features.presentation.detail.detail_merch.DetailMerchActivity
import com.naufal.kidsnesia.purchase.presentation.cart.CartActivity
import org.koin.android.ext.android.get
import kotlin.math.abs

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel = get()
    private lateinit var productAdapter: MerchandiseAdapter
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup collapsing toolbar terlebih dahulu
        setupCollapsingToolbar()

        // Tambahan agar padding bottom sesuai tinggi BottomNavigationView
        val bottomPadding = resources.getDimensionPixelSize(R.dimen.bottom_nav_height)
        binding.recyclerViewMerchandise.setPadding(
            binding.recyclerViewMerchandise.paddingLeft,
            binding.recyclerViewMerchandise.paddingTop,
            binding.recyclerViewMerchandise.paddingRight,
            bottomPadding
        )

        // Setup click listeners untuk kedua cart button
        setupClickListeners()

        setupImageSlider()
        setupRecyclerView()
        observeProducts()

        viewModel.getProfile()

        viewModel.profile.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val namaUser = resource.data?.message?.namaPelanggan
                    binding.textUName.text = "Halo, $namaUser"
                }
                is Resource.Loading -> {
                    binding.textUName.text = "Memuat..."
                }
                is Resource.Error -> {
                    binding.textUName.text = "Halo, Pengguna"
                }
            }
        }
    }

    private fun setupCollapsingToolbar() {
        // Setup AppBarLayout offset listener untuk transisi smooth
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = abs(verticalOffset).toFloat() / totalScrollRange.toFloat()

            binding.toolbarTitle.alpha = percentage
            binding.headerContainer.alpha = 1f - percentage
            binding.logoKidsnesia.alpha = 1f - percentage

            val scale = 1f - (percentage * 0.3f)
            binding.logoKidsnesia.scaleX = scale
            binding.logoKidsnesia.scaleY = scale

            // Sembunyikan/munculkan toolbarCart sesuai status collapse
            binding.toolbarCart.alpha = percentage
            binding.imageCart.alpha = 1f - percentage
        })
    }

    private fun setupClickListeners() {
        // Cart click listeners untuk kedua button (expanded dan collapsed)
        binding.imageCart.setOnClickListener {
            navigateToCart()
        }

        binding.toolbarCart.setOnClickListener {
            navigateToCart()
        }
    }

    private fun navigateToCart() {
        val intent = Intent(requireContext(), CartActivity::class.java)
        startActivity(intent)
    }

    private fun setupImageSlider() {
        val imageList = listOf(
            R.drawable.foto_3d_printing,
            R.drawable.foto_aku_cinta_indonesia,
            R.drawable.foto_kreasi_sablon,
            R.drawable.foto_programmer_cilik,
            R.drawable.foto_sewa_pakaian,
            R.drawable.foto_studio
        )

        val sliderAdapter = ImageSliderAdapter(imageList)
        binding.imageSlider.adapter = sliderAdapter
        binding.imageSlider.offscreenPageLimit = 3

        // Enhanced page transformer dengan smooth scaling
        binding.imageSlider.setPageTransformer { page, position ->
            val absPosition = kotlin.math.abs(position)
            page.apply {
                scaleY = 0.85f + (1 - absPosition) * 0.15f
                scaleX = 0.85f + (1 - absPosition) * 0.15f
                alpha = 0.5f + (1 - absPosition) * 0.5f
            }
        }

        // Connect to dots indicator
        binding.dotsIndicator.setViewPager2(binding.imageSlider)

        // Auto-slide setup
        sliderHandler = Handler(Looper.getMainLooper())
        sliderRunnable = Runnable {
            val nextItem = (binding.imageSlider.currentItem + 1) % imageList.size
            binding.imageSlider.setCurrentItem(nextItem, true)
            sliderHandler.postDelayed(sliderRunnable, 4000) // 4 detik interval
        }

        sliderHandler.postDelayed(sliderRunnable, 4000)

        // Reset timer saat user swipe manual
        binding.imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 4000)
            }
        })
    }

    private fun setupRecyclerView() {
        val spanCount = if (resources.configuration.screenWidthDp >= 600) 3 else 2
        binding.recyclerViewMerchandise.layoutManager =
            GridLayoutManager(requireContext(), spanCount)

        // Add smooth scroll behavior
        binding.recyclerViewMerchandise.isNestedScrollingEnabled = false
        binding.recyclerViewMerchandise.setHasFixedSize(true)

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
        sliderHandler.removeCallbacks(sliderRunnable)
        _binding = null
    }
}