package com.naufal.kidsnesia.main_features.presentation.nota

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.naufal.kidsnesia.main_features.presentation.nota.nota_event.NotaEventFragment
import com.naufal.kidsnesia.main_features.presentation.nota.nota_merch.NotaMerchFragment

class NotaPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    val notaEventFragment = NotaEventFragment()
    val notaMerchFragment = NotaMerchFragment()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> notaEventFragment
            1 -> notaMerchFragment
            else -> Fragment()
        }
    }
}