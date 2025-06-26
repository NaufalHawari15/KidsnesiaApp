package com.naufal.kidsnesia.purchase.presentation.cart

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.databinding.ActivityCartBinding
import com.naufal.kidsnesia.purchase.presentation.cart.event.EventCartFragment

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var pagerAdapter: CartPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pagerAdapter = CartPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Event"
                1 -> "Merchandise"
                else -> ""
            }
        }.attach()

        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        when (binding.viewPager.currentItem) {
            0 -> pagerAdapter.eventFragment.refreshCart()
            1 -> pagerAdapter.merchFragment.refreshCart()
        }
    }
}
