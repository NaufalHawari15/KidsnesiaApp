package com.naufal.kidsnesia.main_features.presentation.nota

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.databinding.ActivityNotaBinding

class NotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotaBinding
    private lateinit var pagerAdapter: NotaPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pagerAdapter = NotaPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Nota Event"
                1 -> "Nota Merchandise"
                else -> ""
            }
        }.attach()

        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        when (binding.viewPager.currentItem) {
            0 -> pagerAdapter.notaEventFragment.refreshCart()
            1 -> pagerAdapter.notaMerchFragment.refreshCart()
        }
    }
}